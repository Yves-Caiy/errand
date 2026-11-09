package com.axin.web.controller.errand;

import com.axin.common.core.controller.BaseController;
import com.axin.common.core.domain.AjaxResult;
import com.axin.common.core.page.TableDataInfo;
import com.axin.errand.service.DispatchService;
import com.axin.system.domain.OrderDispatchLog;
import com.axin.system.domain.OrderMain;
import com.axin.system.domain.WechatUser;
import com.axin.system.service.IOrderDispatchLogService;
import com.axin.system.service.IOrderService;
import com.axin.system.service.IWechatUserService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

@Validated
@Tag(name = "后台智能派单", description = "派单预览、手动触发、派单记录")
@RestController
@RequestMapping("/errand/dispatch")
public class DispatchController extends BaseController {

    @Autowired
    private DispatchService dispatchService;

    @Autowired
    private IOrderDispatchLogService dispatchLogService;

    @Autowired
    private IOrderService orderService;

    @Autowired
    private IWechatUserService wechatUserService;

    @Operation(summary = "候选与打分预览（不写库）")
    @PreAuthorize("@ss.hasPermi('errand:dispatch:list')")
    @GetMapping("/candidates")
    public AjaxResult candidates(@RequestParam Long orderId) {
        return success(dispatchService.previewCandidates(orderId));
    }

    @Operation(summary = "手动触发派单（重新走第 1 轮推送）")
    @PreAuthorize("@ss.hasPermi('errand:dispatch:trigger')")
    @PostMapping("/trigger")
    public AjaxResult trigger(@RequestParam Long orderId) {
        dispatchService.triggerDispatch(orderId);
        return success(true);
    }

    @Operation(summary = "执行一轮派单（调试：指定轮次与类型）")
    @PreAuthorize("@ss.hasPermi('errand:dispatch:trigger')")
    @PostMapping("/round")
    public AjaxResult round(@RequestParam Long orderId,
                            @RequestParam(defaultValue = "1") int round,
                            @RequestParam(defaultValue = "1") int dispatchType) {
        OrderMain order = orderService.selectOrderMainById(orderId);
        if (order == null) {
            return error("订单不存在");
        }
        dispatchService.dispatchRound(order, round, dispatchType);
        return success(true);
    }

    @Operation(summary = "派单记录列表")
    @PreAuthorize("@ss.hasPermi('errand:dispatch:list')")
    @GetMapping("/log/list")
    public TableDataInfo logList(OrderDispatchLog query) {
        startPage();
        List<OrderDispatchLog> list = dispatchLogService.selectOrderDispatchLogList(query);
        TableDataInfo data = getDataTable(list);
        List<Map<String, Object>> rows = new ArrayList<>();
        for (OrderDispatchLog dl : list) {
            rows.add(toRow(dl));
        }
        data.setRows(rows);
        return data;
    }

    @Operation(summary = "某订单的完整派单轨迹")
    @PreAuthorize("@ss.hasPermi('errand:dispatch:list')")
    @GetMapping("/log/{orderId}")
    public AjaxResult logByOrder(@PathVariable Long orderId) {
        List<OrderDispatchLog> list = dispatchLogService.selectLogsByOrderId(orderId);
        List<Map<String, Object>> rows = new ArrayList<>();
        for (OrderDispatchLog dl : list) {
            rows.add(toRow(dl));
        }
        return success(rows);
    }

    private Map<String, Object> toRow(OrderDispatchLog dl) {
        Map<String, Object> m = new LinkedHashMap<>();
        m.put("id", dl.getId());
        m.put("orderId", dl.getOrderId());
        m.put("runnerUserId", dl.getRunnerUserId());
        m.put("score", dl.getScore());
        m.put("dispatchRound", dl.getDispatchRound());
        m.put("dispatchType", dl.getDispatchType());
        m.put("status", dl.getStatus());
        m.put("createTime", dl.getCreateTime());
        m.put("updateTime", dl.getUpdateTime());
        OrderMain o = orderService.selectOrderMainById(dl.getOrderId());
        if (o != null) {
            m.put("orderNo", o.getOrderNo());
            m.put("orderType", o.getOrderType());
        }
        WechatUser u = dl.getRunnerUserId() == null ? null
                : wechatUserService.selectWechatUserById(dl.getRunnerUserId());
        if (u != null) {
            m.put("runnerNickname", u.getNickname());
        }
        return m;
    }
}
