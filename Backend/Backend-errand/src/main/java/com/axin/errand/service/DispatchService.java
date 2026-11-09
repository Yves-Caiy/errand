package com.axin.errand.service;

import com.alibaba.fastjson2.JSON;
import com.alibaba.fastjson2.JSONObject;
import com.axin.common.exception.ServiceException;
import com.axin.system.domain.MiniappConfig;
import com.axin.system.domain.OrderDispatchLog;
import com.axin.system.domain.OrderItem;
import com.axin.system.domain.OrderMain;
import com.axin.system.domain.TakeawayStore;
import com.axin.system.domain.vo.RunnerCandidateVO;
import com.axin.system.service.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.*;

/**
 * 智能派单服务（仅外卖 order_type = 1）。
 *
 * 混合模式：先推送 Top N 抢单；超时由定时任务推进下一轮；轮次耗尽自动指派最高分接单员。
 * 算法见 design/smart-dispatch/smart-dispatch-design.md。
 */
@Service
public class DispatchService {

    private static final Logger log = LoggerFactory.getLogger(DispatchService.class);

    public static final int ST_PUSHED = 1;
    public static final int ST_ACCEPTED = 2;
    public static final int ST_TIMEOUT = 3;
    public static final int ST_REJECTED = 4;
    public static final int ST_ASSIGNED = 5;
    public static final int ST_OTHER_ACCEPTED = 9;

    public static final int TYPE_PUSH = 1;
    public static final int TYPE_ASSIGN = 2;

    // 默认权重（合计 100）
    private static final int W_COMM = 25;
    private static final int W_STORE = 15;
    private static final int W_LOAD = 25;
    private static final int W_DELIVER = 20;
    private static final int W_QUAL = 10;
    private static final int W_ACTIVE = 5;
    private static final int DEF_MIN_SAMPLE = 5;
    private static final int DEF_FAIRNESS_ROUNDS = 3;
    private static final int DEF_COOLDOWN_TIMES = 2;
    private static final int DEF_BASE_MINUTES = 30;
    private static final int DEF_TIMEOUT_SEC = 30;
    private static final int DEF_PUSH_COUNT = 3;
    private static final int DEF_MAX_ROUND = 3;
    private static final double FAIRNESS_BOOST = 5.0;

    @Autowired
    private IOrderService orderService;
    @Autowired
    private IRunnerProfileService runnerProfileService;
    @Autowired
    private IOrderDispatchLogService dispatchLogService;
    @Autowired
    private IMiniappConfigService miniappConfigService;
    @Autowired
    private ITakeawayStoreService takeawayStoreService;
    @Autowired
    private ICommunityService communityService;

    // ---------------------------------------------------------------- 闸门

    /** 该订单类型是否纳入了智能派单（默认仅外卖 = 1） */
    public boolean isDispatchTypeEnabled(Integer orderType) {
        MiniappConfig cfg = safeConfig();
        if (cfg == null || cfg.getDispatchEnabled() == null || cfg.getDispatchEnabled() != 1) {
            return false;
        }
        if (cfg.getTestMode() != null && cfg.getTestMode() == 1) {
            return false;
        }
        if (orderType == null) {
            return false;
        }
        String types = cfg.getDispatchOrderTypes();
        if (types == null || types.trim().isEmpty()) {
            types = "1";
        }
        for (String t : types.split(",")) {
            if (t.trim().equals(String.valueOf(orderType))) {
                return true;
            }
        }
        return false;
    }

    /** 支付成功后触发即时派单（第 1 轮推送）。 */
    public void triggerDispatch(Long orderId) {
        try {
            OrderMain order = orderService.selectOrderMainById(orderId);
            if (order == null) {
                return;
            }
            if (order.getRunnerId() != null
                    || order.getOrderStatus() == null || order.getOrderStatus() != 1) {
                return;
            }
            if (!isDispatchTypeEnabled(order.getOrderType())) {
                return;
            }
            // 已有派单记录则不再重复触发
            if (dispatchLogService.countRounds(orderId) > 0) {
                return;
            }
            dispatchRound(order, 1, TYPE_PUSH);
        } catch (Exception e) {
            log.warn("triggerDispatch failed, orderId={}", orderId, e);
        }
    }

    // ---------------------------------------------------------------- 单轮执行

    /**
     * 执行一轮派单。
     * @return 实际写入的派单记录条数（0 表示无可用候选或指派失败）
     */
    public int dispatchRound(OrderMain order, int round, int dispatchType) {
        MiniappConfig cfg = safeConfig();
        int timeoutSec = optInt(cfg == null ? null : cfg.getDispatchTimeoutSec(), DEF_TIMEOUT_SEC);
        int pushCount = optInt(cfg == null ? null : cfg.getDispatchPushCount(), DEF_PUSH_COUNT);
        int maxRound = optInt(cfg == null ? null : cfg.getDispatchMaxRound(), DEF_MAX_ROUND);
        ScoringParams params = parseParams(cfg == null ? null : cfg.getDispatchWeightJson());

        Long storeCommunityId = resolveStoreCommunityId(order.getId());
        List<RunnerCandidateVO> candidates = runnerProfileService.selectCandidates(
                order.getCommunityId(), storeCommunityId);

        Set<Long> alreadyPushed = new HashSet<>();
        Set<Long> rejected = new HashSet<>();
        for (OrderDispatchLog dl : dispatchLogService.selectLogsByOrderId(order.getId())) {
            alreadyPushed.add(dl.getRunnerUserId());
            if (dl.getStatus() != null && dl.getStatus() == ST_REJECTED) {
                rejected.add(dl.getRunnerUserId());
            }
        }

        boolean assigning = (dispatchType == TYPE_ASSIGN);
        List<Scored> ranked = score(order, candidates, params, alreadyPushed, rejected, !assigning);

        if (ranked.isEmpty()) {
            log.info("dispatch: no candidate for order {} (communityId={}, storeCommunityId={}, assign={})",
                    order.getOrderNo(), order.getCommunityId(), storeCommunityId, assigning);
            return 0;
        }

        if (assigning) {
            // 轮次耗尽 / 显式指派 → 指派给当前最高分
            Scored best = ranked.get(0);
            int rows = orderService.acceptOrder(order.getId(), best.c.getUserId());
            if (rows == 1) {
                // 本人本轮如有推送记录，一并收敛为「已接受」，避免市场页残留失效卡片
                dispatchLogService.markAccepted(order.getId(), best.c.getUserId());
                OrderDispatchLog logRow = new OrderDispatchLog();
                logRow.setOrderId(order.getId());
                logRow.setRunnerUserId(best.c.getUserId());
                logRow.setScore(BigDecimal.valueOf(best.score).setScale(2, RoundingMode.HALF_UP));
                logRow.setDispatchRound(round);
                logRow.setDispatchType(TYPE_ASSIGN);
                logRow.setStatus(ST_ASSIGNED);
                dispatchLogService.insertOrderDispatchLog(logRow);
                dispatchLogService.markOthersAccepted(order.getId(), best.c.getUserId());
                log.info("dispatch auto-assign order {} -> runner {} (score {})",
                        order.getOrderNo(), best.c.getUserId(), best.score);
                return 1;
            }
            log.info("dispatch auto-assign order {} failed, maybe accepted by other", order.getOrderNo());
            return 0;
        }

        // 推送：取排名最前的 pushCount 位（已推送者不再推送）
        int pushed = 0;
        for (Scored s : ranked) {
            if (pushed >= pushCount) {
                break;
            }
            OrderDispatchLog logRow = new OrderDispatchLog();
            logRow.setOrderId(order.getId());
            logRow.setRunnerUserId(s.c.getUserId());
            logRow.setScore(BigDecimal.valueOf(s.score).setScale(2, RoundingMode.HALF_UP));
            logRow.setDispatchRound(round);
            logRow.setDispatchType(TYPE_PUSH);
            logRow.setStatus(ST_PUSHED);
            dispatchLogService.insertOrderDispatchLog(logRow);
            pushed++;
        }
        log.info("dispatch round {} order {} pushed to {} runner(s), timeoutSec={}",
                round, order.getOrderNo(), pushed, timeoutSec);
        return pushed;
    }

    // ---------------------------------------------------------------- 定时兜底扫描

    /** 扫描超时未接的外卖订单：推进下一轮 / 轮次耗尽自动指派。 */
    public void scanTimeout() {
        List<Long> orderIds = dispatchLogService.selectPendingTakeoutOrderIds();
        if (orderIds == null || orderIds.isEmpty()) {
            return;
        }
        MiniappConfig cfg = safeConfig();
        int timeoutSec = optInt(cfg == null ? null : cfg.getDispatchTimeoutSec(), DEF_TIMEOUT_SEC);
        int maxRound = optInt(cfg == null ? null : cfg.getDispatchMaxRound(), DEF_MAX_ROUND);
        long now = System.currentTimeMillis();

        for (Long orderId : orderIds) {
            try {
                OrderMain order = orderService.selectOrderMainById(orderId);
                if (order == null || order.getRunnerId() != null
                        || order.getOrderStatus() == null || order.getOrderStatus() != 1) {
                    continue;
                }
                List<OrderDispatchLog> logs = dispatchLogService.selectLogsByOrderId(orderId);
                if (logs == null || logs.isEmpty()) {
                    continue;
                }
                int currentRound = 0;
                for (OrderDispatchLog dl : logs) {
                    if (dl.getDispatchRound() != null && dl.getDispatchRound() > currentRound) {
                        currentRound = dl.getDispatchRound();
                    }
                }
                // 统计当前轮：是否还有人处于「待接受」，以及本轮开始时间
                Date roundStart = null;
                Date roundLatest = null;
                boolean pendingExists = false;
                for (OrderDispatchLog dl : logs) {
                    if (dl.getDispatchRound() == null || dl.getDispatchRound() != currentRound) {
                        continue;
                    }
                    if (dl.getStatus() != null
                            && (dl.getStatus() == ST_PUSHED || dl.getStatus() == ST_ASSIGNED)) {
                        pendingExists = true;
                    }
                    if (dl.getCreateTime() != null) {
                        if (roundStart == null || dl.getCreateTime().before(roundStart)) {
                            roundStart = dl.getCreateTime();
                        }
                        if (roundLatest == null || dl.getCreateTime().after(roundLatest)) {
                            roundLatest = dl.getCreateTime();
                        }
                    }
                }
                // 本轮已全部解决（超时/拒绝）时用最晚时间做节流，避免每 15 秒重复推进
                Date gate = pendingExists ? roundStart : roundLatest;
                if (gate == null) {
                    continue;
                }
                if (now - gate.getTime() < timeoutSec * 1000L) {
                    continue; // 未到推进时间
                }
                if (pendingExists) {
                    // 仍有待接受的推送 → 标记本轮超时
                    dispatchLogService.updateStatusByOrderRound(orderId, currentRound, ST_TIMEOUT);
                }
                int nextRound = currentRound + 1;
                if (nextRound <= maxRound) {
                    log.info("dispatch timeout, order {} round {} -> next round {}",
                            order.getOrderNo(), currentRound, nextRound);
                    int pushed = dispatchRound(order, nextRound, TYPE_PUSH);
                    if (pushed == 0) {
                        // 没有新的候选可推送 → 直接进入指派，避免订单卡住
                        log.info("dispatch no new candidate, order {} -> auto assign", order.getOrderNo());
                        dispatchRound(order, nextRound, TYPE_ASSIGN);
                    }
                } else {
                    log.info("dispatch exhausted, order {} round {} -> auto assign", order.getOrderNo(), currentRound);
                    dispatchRound(order, currentRound, TYPE_ASSIGN);
                }
            } catch (Exception e) {
                log.warn("scanTimeout failed, orderId={}", orderId, e);
            }
        }
    }

    // ---------------------------------------------------------------- 接单 / 拒单

    /** 条件更新接单，成功后收敛派单记录。 */
    public int acceptOrder(Long orderId, Long runnerUserId) {
        int rows = orderService.acceptOrder(orderId, runnerUserId);
        if (rows == 1) {
            dispatchLogService.markAccepted(orderId, runnerUserId);
            dispatchLogService.markOthersAccepted(orderId, runnerUserId);
        }
        return rows;
    }

    /** 拒单：标记为已拒绝并立即把订单推进到下一轮。 */
    public void rejectOrder(Long orderId, Long runnerUserId) {
        List<OrderDispatchLog> logs = dispatchLogService.selectLogsByOrderId(orderId);
        boolean matched = false;
        if (logs != null) {
            for (OrderDispatchLog dl : logs) {
                if (runnerUserId.equals(dl.getRunnerUserId())
                        && dl.getStatus() != null && dl.getStatus() == ST_PUSHED) {
                    dispatchLogService.updateStatus(dl.getId(), ST_REJECTED);
                    matched = true;
                }
            }
        }
        if (!matched) {
            throw new ServiceException("没有可拒绝的派单");
        }
        OrderMain order = orderService.selectOrderMainById(orderId);
        if (order == null || order.getRunnerId() != null
                || order.getOrderStatus() == null || order.getOrderStatus() != 1) {
            return;
        }
        MiniappConfig cfg = safeConfig();
        int maxRound = optInt(cfg == null ? null : cfg.getDispatchMaxRound(), DEF_MAX_ROUND);
        int currentRound = dispatchLogService.countRounds(orderId);
        if (currentRound >= maxRound) {
            dispatchRound(order, currentRound, TYPE_ASSIGN);
        } else {
            int pushed = dispatchRound(order, currentRound + 1, TYPE_PUSH);
            if (pushed == 0) {
                dispatchRound(order, currentRound + 1, TYPE_ASSIGN);
            }
        }
    }

    // ---------------------------------------------------------------- 查询

    /** 指派给我的列表（含剩余倒计时秒数）。 */
    public List<Map<String, Object>> pendingForRunner(Long runnerUserId) {
        MiniappConfig cfg = safeConfig();
        int timeoutSec = optInt(cfg == null ? null : cfg.getDispatchTimeoutSec(), DEF_TIMEOUT_SEC);
        List<Map<String, Object>> result = new ArrayList<>();
        OrderDispatchLog query = new OrderDispatchLog();
        query.setRunnerUserId(runnerUserId);
        List<OrderDispatchLog> logs = dispatchLogService.selectOrderDispatchLogList(query);
        if (logs == null) {
            return result;
        }
        long now = System.currentTimeMillis();
        for (OrderDispatchLog dl : logs) {
            if (dl.getStatus() == null || (dl.getStatus() != ST_PUSHED && dl.getStatus() != ST_ASSIGNED)) {
                continue;
            }
            OrderMain order = orderService.selectOrderMainById(dl.getOrderId());
            if (order == null || order.getRunnerId() != null
                    || order.getOrderStatus() == null || order.getOrderStatus() != 1) {
                continue;
            }
            Map<String, Object> m = new HashMap<>();
            m.put("orderId", order.getId());
            m.put("orderNo", order.getOrderNo());
            m.put("orderType", order.getOrderType());
            m.put("commissionAmount", order.getCommissionAmount());
            m.put("actualAmount", order.getActualAmount());
            m.put("detailAddress", order.getDetailAddress());
            m.put("note", order.getNote());
            m.put("dispatchRound", dl.getDispatchRound());
            m.put("dispatchType", dl.getDispatchType());
            m.put("score", dl.getScore());
            m.put("status", dl.getStatus());
            m.put("pushTime", dl.getCreateTime());
            long expireAt = dl.getCreateTime() == null ? now : dl.getCreateTime().getTime() + timeoutSec * 1000L;
            m.put("remainSeconds", Math.max(0, (expireAt - now) / 1000));
            if (order.getCommunityId() != null) {
                com.axin.system.domain.Community c = communityService.selectCommunityById(order.getCommunityId());
                if (c != null) {
                    m.put("communityName", c.getCommunityName());
                }
            }
            result.add(m);
        }
        return result;
    }

    // ---------------------------------------------------------------- 来单提醒

    /** 未读来单提醒列表（A 推送抢单 + B 系统指派）。 */
    public Map<String, Object> noticeList(Long runnerUserId) {
        List<Map<String, Object>> items = collectNotices(runnerUserId);
        Map<String, Object> res = new LinkedHashMap<>();
        res.put("unreadCount", items.size());
        res.put("items", items);
        return res;
    }

    /** 未读来单数（供小程序轮询红点）。 */
    public int noticeUnread(Long runnerUserId) {
        return collectNotices(runnerUserId).size();
    }

    /** 标记来单提醒已读：logIds 为空表示该接单员全部未读记录。 */
    public int noticeRead(Long runnerUserId, List<Long> logIds) {
        return dispatchLogService.markNoticeRead(runnerUserId, logIds);
    }

    /** 组装未读提醒：记录须对本人生效，且订单仍处于需处理状态。 */
    private List<Map<String, Object>> collectNotices(Long runnerUserId) {
        MiniappConfig cfg = safeConfig();
        int timeoutSec = optInt(cfg == null ? null : cfg.getDispatchTimeoutSec(), DEF_TIMEOUT_SEC);
        long now = System.currentTimeMillis();
        List<Map<String, Object>> result = new ArrayList<>();
        OrderDispatchLog query = new OrderDispatchLog();
        query.setRunnerUserId(runnerUserId);
        List<OrderDispatchLog> logs = dispatchLogService.selectOrderDispatchLogList(query);
        if (logs == null) {
            return result;
        }
        for (OrderDispatchLog dl : logs) {
            if (dl.getStatus() == null) {
                continue;
            }
            if (dl.getIsRead() != null && dl.getIsRead() == 1) {
                continue;
            }
            boolean isPush = dl.getStatus() == ST_PUSHED;
            boolean isAssign = dl.getStatus() == ST_ASSIGNED;
            if (!isPush && !isAssign) {
                continue;
            }
            OrderMain order = orderService.selectOrderMainById(dl.getOrderId());
            if (order == null || order.getOrderStatus() == null) {
                continue;
            }
            if (isPush) {
                // 推送单：订单尚未被任何人接走
                if (order.getOrderStatus() != 1 || order.getRunnerId() != null) {
                    continue;
                }
            } else {
                // 指派单：订单已归属本人且仍在进行中
                if (order.getOrderStatus() != 2 || !runnerUserId.equals(order.getRunnerId())) {
                    continue;
                }
            }
            Map<String, Object> m = new LinkedHashMap<>();
            m.put("logId", dl.getId());
            m.put("orderId", order.getId());
            m.put("orderNo", order.getOrderNo());
            m.put("orderType", order.getOrderType());
            m.put("type", isPush ? "push" : "assign");
            m.put("dispatchRound", dl.getDispatchRound());
            m.put("dispatchType", dl.getDispatchType());
            m.put("score", dl.getScore());
            m.put("commissionAmount", order.getCommissionAmount());
            m.put("actualAmount", order.getActualAmount());
            m.put("detailAddress", order.getDetailAddress());
            m.put("note", order.getNote());
            m.put("createTime", dl.getCreateTime());
            long remain = 0;
            if (isPush) {
                long expireAt = dl.getCreateTime() == null ? now : dl.getCreateTime().getTime() + timeoutSec * 1000L;
                remain = Math.max(0, (expireAt - now) / 1000);
            }
            m.put("remainSeconds", remain);
            if (order.getCommunityId() != null) {
                com.axin.system.domain.Community c = communityService.selectCommunityById(order.getCommunityId());
                if (c != null) {
                    m.put("communityName", c.getCommunityName());
                }
            }
            result.add(m);
        }
        return result;
    }

    // ---------------------------------------------------------------- 打分

    private static class Scored {
        RunnerCandidateVO c;
        double score;
        Map<String, Object> detail;
    }

    private static class ScoringParams {
        int comm = W_COMM, store = W_STORE, load = W_LOAD, deliver = W_DELIVER, qual = W_QUAL, active = W_ACTIVE;
        int minSample = DEF_MIN_SAMPLE, fairnessRounds = DEF_FAIRNESS_ROUNDS,
                cooldownTimes = DEF_COOLDOWN_TIMES, baseMinutes = DEF_BASE_MINUTES;
    }

    private ScoringParams parseParams(String json) {
        ScoringParams p = new ScoringParams();
        if (json == null || json.trim().isEmpty()) {
            return p;
        }
        try {
            JSONObject o = JSON.parseObject(json);
            p.comm = o.getIntValue("comm", p.comm);
            p.store = o.getIntValue("store", p.store);
            p.load = o.getIntValue("load", p.load);
            p.deliver = o.getIntValue("deliver", p.deliver);
            p.qual = o.getIntValue("qual", p.qual);
            p.active = o.getIntValue("active", p.active);
            p.minSample = o.getIntValue("minSample", p.minSample);
            p.fairnessRounds = o.getIntValue("fairnessRounds", p.fairnessRounds);
            p.cooldownTimes = o.getIntValue("cooldownTimes", p.cooldownTimes);
            p.baseMinutes = o.getIntValue("baseMinutes", p.baseMinutes);
        } catch (Exception e) {
            log.warn("parse dispatch weights failed, use defaults. json={}", json);
        }
        return p;
    }

    private List<Scored> score(OrderMain order, List<RunnerCandidateVO> candidates,
                               ScoringParams p, Set<Long> alreadyPushed, Set<Long> rejectedIds,
                               boolean excludePushed) {
        List<Scored> list = new ArrayList<>();
        if (candidates == null) {
            return list;
        }
        Long orderUserId = order.getUserId();
        for (RunnerCandidateVO c : candidates) {
            if (c.getUserId() == null) {
                continue;
            }
            // 硬条件：非本人下单、未拒绝过本单、未超产能、无冷却、未在本单前面轮次推送过
            if (c.getUserId().equals(orderUserId)) {
                continue;
            }
            if (rejectedIds != null && rejectedIds.contains(c.getUserId())) {
                continue;
            }
            int inTransit = c.getInTransit() == null ? 0 : c.getInTransit();
            int maxConcurrent = c.getMaxConcurrent() == null ? 3 : c.getMaxConcurrent();
            if (maxConcurrent > 0 && inTransit >= maxConcurrent) {
                continue;
            }
            int rejectStreak = c.getRejectStreak() == null ? 0 : c.getRejectStreak();
            if (rejectStreak >= p.cooldownTimes) {
                continue;
            }
            if (excludePushed && alreadyPushed.contains(c.getUserId())) {
                continue;
            }
            list.add(compute(c, p));
        }
        list.sort((a, b) -> Double.compare(b.score, a.score));
        return list;
    }

    /** 计算单维度得分与总分（派单与调试预览共用同一套公式） */
    private Scored compute(RunnerCandidateVO c, ScoringParams p) {
        int inTransit = c.getInTransit() == null ? 0 : c.getInTransit();
        int maxConcurrent = c.getMaxConcurrent() == null ? 3 : c.getMaxConcurrent();

        double sComm = flag(c.getCoverReceive());
        double sStore = flag(c.getCoverStore());
        double sLoad = clamp(1.0 - (double) inTransit / Math.max(1, maxConcurrent));

        int done = c.getDoneCnt() == null ? 0 : c.getDoneCnt();
        int handled = c.getHandledCnt() == null ? 0 : c.getHandledCnt();
        Double avg = c.getAvgMinutes();

        double sDeliver = 0.5;
        if (avg != null && done >= p.minSample && p.baseMinutes > 0) {
            sDeliver = clamp(1.0 - avg / p.baseMinutes);
        }
        double sQual = 0.5;
        if (handled >= p.minSample) {
            sQual = clamp((double) done / handled);
        }
        double sActive = 0.5;
        if (c.getLastFinishTime() != null) {
            long days = (System.currentTimeMillis() - c.getLastFinishTime().getTime()) / (24L * 3600 * 1000);
            sActive = clamp(1.0 - (double) days / 30.0);
        }

        double total = sComm * p.comm + sStore * p.store + sLoad * p.load
                + sDeliver * p.deliver + sQual * p.qual + sActive * p.active;

        int boost = c.getWeightBoost() == null ? 0 : c.getWeightBoost();
        total += boost;
        int missRounds = c.getDispatchMissRounds() == null ? 0 : c.getDispatchMissRounds();
        boolean fairness = missRounds >= p.fairnessRounds;
        if (fairness) {
            total += FAIRNESS_BOOST;
        }

        Scored s = new Scored();
        s.c = c;
        s.score = total;
        Map<String, Object> d = new LinkedHashMap<>();
        d.put("inTransit", inTransit);
        d.put("maxConcurrent", maxConcurrent);
        d.put("s_comm", round2(sComm));
        d.put("s_store", round2(sStore));
        d.put("s_load", round2(sLoad));
        d.put("s_deliver", round2(sDeliver));
        d.put("s_qual", round2(sQual));
        d.put("s_active", round2(sActive));
        d.put("doneCnt", done);
        d.put("handledCnt", handled);
        d.put("avgMinutes", avg);
        d.put("weightBoost", boost);
        d.put("fairnessBoost", fairness ? FAIRNESS_BOOST : 0.0);
        d.put("total", round2(total));
        s.detail = d;
        return s;
    }

    /** 调试/运营预览：列出候选与逐维度得分（不写库、不改单） */
    public List<Map<String, Object>> previewCandidates(Long orderId) {
        List<Map<String, Object>> result = new ArrayList<>();
        OrderMain order = orderService.selectOrderMainById(orderId);
        if (order == null) {
            return result;
        }
        MiniappConfig cfg = safeConfig();
        ScoringParams p = parseParams(cfg == null ? null : cfg.getDispatchWeightJson());
        Long storeCommunityId = resolveStoreCommunityId(orderId);
        List<RunnerCandidateVO> candidates =
                runnerProfileService.selectCandidates(order.getCommunityId(), storeCommunityId);
        Set<Long> alreadyPushed = new HashSet<>();
        Set<Long> rejected = new HashSet<>();
        for (OrderDispatchLog dl : dispatchLogService.selectLogsByOrderId(orderId)) {
            alreadyPushed.add(dl.getRunnerUserId());
            if (dl.getStatus() != null && dl.getStatus() == ST_REJECTED) {
                rejected.add(dl.getRunnerUserId());
            }
        }
        List<Scored> ranked = score(order, candidates, p, alreadyPushed, rejected, false);
        int rank = 1;
        for (Scored s : ranked) {
            Map<String, Object> m = new LinkedHashMap<>();
            m.put("rank", rank++);
            m.put("userId", s.c.getUserId());
            m.put("nickname", s.c.getNickname());
            m.put("score", round2(s.score));
            m.put("detail", s.detail);
            result.add(m);
        }
        return result;
    }

    private static double round2(double v) {
        return Math.round(v * 100.0) / 100.0;
    }

    private Long resolveStoreCommunityId(Long orderId) {
        List<OrderItem> items = orderService.selectOrderItemsByOrderId(orderId);
        if (items == null) {
            return null;
        }
        for (OrderItem it : items) {
            if (it.getStoreId() != null) {
                TakeawayStore store = takeawayStoreService.selectTakeawayStoreById(it.getStoreId());
                if (store != null && store.getCommunityId() != null) {
                    return store.getCommunityId();
                }
            }
        }
        return null;
    }

    private MiniappConfig safeConfig() {
        try {
            return miniappConfigService.getCurrent();
        } catch (Exception e) {
            return null;
        }
    }

    private static int optInt(Integer v, int def) {
        return v == null ? def : v;
    }

    private static double flag(Integer v) {
        return v != null && v == 1 ? 1.0 : 0.0;
    }

    private static double clamp(double v) {
        if (v < 0) return 0.0;
        if (v > 1) return 1.0;
        return v;
    }
}
