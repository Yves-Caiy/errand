package com.axin.web.controller.errand;

import com.axin.common.core.controller.BaseController;
import com.axin.common.core.page.TableDataInfo;
import com.axin.common.core.domain.AjaxResult;
import com.axin.common.annotation.Log;
import com.axin.common.enums.BusinessType;
import com.axin.common.utils.poi.ExcelUtil;
import com.axin.system.domain.WithdrawApply;
import com.axin.system.service.IWithdrawApplyService;
import com.axin.system.service.IWechatUserService;
import com.axin.system.domain.WechatUser;
import com.axin.system.domain.vo.WithdrawApplyVO;
import jakarta.servlet.http.HttpServletResponse;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.stream.Collectors;
import java.util.Date;

@Validated
@Tag(name = "后台提现申请管理", description = "提现申请查询、审核接口（占位）")
@RestController
@RequestMapping("/errand/withdrawApply")
public class WithdrawApplyController extends BaseController {

    @Autowired
    private IWithdrawApplyService withdrawApplyService;
    @Autowired
    private IWechatUserService wechatUserService;

    @Operation(summary = "分页查询提现申请列表")
    @PreAuthorize("@ss.hasPermi('errand:withdrawApply:list')")
    @GetMapping("/list")
    public TableDataInfo list(@RequestParam(value = "userId", required = false) Long userId,
                              @RequestParam(value = "status", required = false) Integer status,
                              @RequestParam(value = "openid", required = false) String openid,
                              @RequestParam(value = "payStatus", required = false) Integer payStatus) {
        startPage();
        WithdrawApply query = new WithdrawApply();
        query.setStatus(status);
        if (openid != null && !openid.isBlank()) {
            WechatUser uu = wechatUserService.selectByOpenid(openid);
            Long uid = uu == null ? null : uu.getId();
            query.setUserId(uid);
        } else {
            query.setUserId(userId);
        }
        query.setPayStatus(payStatus);
        List<WithdrawApply> list = withdrawApplyService.selectWithdrawApplyList(query);
        TableDataInfo data = getDataTable(list);
        List<WithdrawApplyVO> voList = list.stream().map(e -> {
            WithdrawApplyVO vo = new WithdrawApplyVO();
            vo.setId(e.getId());
            vo.setUserId(e.getUserId());
            vo.setAmount(e.getAmount());
            vo.setCreditedAmount(e.getCreditedAmount());
            vo.setRate(e.getRate());
            vo.setStatus(e.getStatus());
            vo.setPayStatus(e.getPayStatus());
            vo.setRemark(e.getRemark());
            vo.setCreateTime(e.getCreateTime());
            vo.setUpdateTime(e.getUpdateTime());
            if (e.getUserId() != null) {
                WechatUser u = wechatUserService.selectWechatUserById(e.getUserId());
                if (u != null) {
                    vo.setUserNickname(u.getNickname());
                    vo.setUserPhone(u.getPhone());
                    vo.setUserOpenid(u.getOpenid());
                }
            }
            return vo;
        }).collect(Collectors.toList());
        data.setRows(voList);
        return data;
    }

    @Operation(summary = "审核通过（占位）")
    @PreAuthorize("@ss.hasPermi('errand:withdrawApply:approve')")
    @PutMapping("/{id}/approve")
    public AjaxResult approve(@Parameter(description = "主键ID") @PathVariable Long id) {
        WithdrawApply existed = withdrawApplyService.selectWithdrawApplyById(id);
        if (existed == null) {
            return error("记录不存在");
        }
        WithdrawApply upd = new WithdrawApply();
        upd.setId(id);
        upd.setStatus(1);
        upd.setUpdateTime(new Date());
        int rows = withdrawApplyService.updateWithdrawApply(upd);
        return toAjax(rows);
    }

    @Operation(summary = "审核拒绝（占位）")
    @PreAuthorize("@ss.hasPermi('errand:withdrawApply:reject')")
    @PutMapping("/{id}/reject")
    public AjaxResult reject(@Parameter(description = "主键ID") @PathVariable Long id) {
        WithdrawApply existed = withdrawApplyService.selectWithdrawApplyById(id);
        if (existed == null) {
            return error("记录不存在");
        }
        Long userId = existed.getUserId();
        Long amount = existed.getAmount();
        WithdrawApply upd = new WithdrawApply();
        upd.setId(id);
        upd.setStatus(2);
        upd.setUpdateTime(new Date());
        int rows = withdrawApplyService.updateWithdrawApply(upd);
        if (rows > 0 && userId != null && amount != null && amount > 0L) {
            WechatUser u = wechatUserService.selectWechatUserById(userId);
            long current = u == null || u.getPendingCommission() == null ? 0L : u.getPendingCommission();
            long updated = current + amount;
            WechatUser updUser = new WechatUser();
            updUser.setId(userId);
            updUser.setPendingCommission(updated);
            wechatUserService.updateWechatUser(updUser);
        }
        return toAjax(rows);
    }

    @Operation(summary = "确认打款")
    @PreAuthorize("@ss.hasPermi('errand:withdrawApply:pay')")
    @PutMapping("/{id}/pay")
    public AjaxResult confirmPay(@Parameter(description = "主键ID") @PathVariable Long id) {
        WithdrawApply existed = withdrawApplyService.selectWithdrawApplyById(id);
        if (existed == null) {
            return error("记录不存在");
        }
        WithdrawApply upd = new WithdrawApply();
        upd.setId(id);
        upd.setStatus(3);
        upd.setPayStatus(1);
        upd.setUpdateTime(new Date());
        int rows = withdrawApplyService.updateWithdrawApply(upd);
        return toAjax(rows);
    }

    @Log(title = "提现申请管理", businessType = BusinessType.EXPORT)
    @PreAuthorize("@ss.hasPermi('errand:withdrawApply:export')")
    @PostMapping("/export")
    public void export(HttpServletResponse response) {
        WithdrawApply query = new WithdrawApply();
        query.setStatus(1);
        query.setPayStatus(0);
        List<WithdrawApply> list = withdrawApplyService.selectWithdrawApplyList(query);
        List<WithdrawApplyVO> voList = list.stream().map(e -> {
            WithdrawApplyVO vo = new WithdrawApplyVO();
            vo.setId(e.getId());
            vo.setUserId(e.getUserId());
            vo.setAmount(e.getAmount());
            vo.setCreditedAmount(e.getCreditedAmount());
            vo.setRate(e.getRate());
            vo.setStatus(e.getStatus());
            vo.setPayStatus(e.getPayStatus());
            vo.setRemark(e.getRemark());
            vo.setCreateTime(e.getCreateTime());
            vo.setUpdateTime(e.getUpdateTime());
            if (e.getUserId() != null) {
                WechatUser u = wechatUserService.selectWechatUserById(e.getUserId());
                if (u != null) {
                    vo.setUserNickname(u.getNickname());
                    vo.setUserPhone(u.getPhone());
                    vo.setUserOpenid(u.getOpenid());
                }
            }
            return vo;
        }).collect(Collectors.toList());
        ExcelUtil<WithdrawApplyVO> util = new ExcelUtil<>(WithdrawApplyVO.class);
        util.exportExcel(response, voList, "申请通过未打款列表");
    }
}
