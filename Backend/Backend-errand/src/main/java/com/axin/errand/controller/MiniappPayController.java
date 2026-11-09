package com.axin.errand.controller;

import com.axin.common.core.controller.BaseController;
import com.axin.common.core.domain.AjaxResult;
import com.axin.errand.domain.MiniLoginUser;
import com.axin.errand.service.MiniTokenService;
import com.axin.errand.service.MiniappPayService;
import com.github.xiaoymin.knife4j.annotations.ApiOperationSupport;
import com.github.xiaoymin.knife4j.annotations.ApiSupport;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Map;
import java.util.HashMap;
import com.axin.system.service.IMiniappConfigService;
import com.axin.system.domain.MiniappConfig;
import com.axin.system.service.IWithdrawApplyService;
import com.axin.system.domain.WithdrawApply;
import java.util.Date;
import com.axin.system.service.IWechatUserService;
import com.axin.system.domain.WechatUser;

@Tag(name = "小程序支付接口")
@ApiSupport(author = "AXIN", order = 8)
@RestController
@RequestMapping("/miniapp/pay")
public class MiniappPayController extends BaseController {

    @Autowired
    private MiniappPayService payService;

    @Autowired
    private MiniTokenService tokenService;
    
    @Autowired
    private IMiniappConfigService miniappConfigService;
    
    @Autowired
    private IWithdrawApplyService withdrawApplyService;
    
    @Autowired
    private IWechatUserService wechatUserService;

    @Operation(summary = "发起支付（模拟支付，直接标记订单已支付）")
    @ApiOperationSupport(order = 1)
    @PostMapping("/prepay")
    public AjaxResult prepay(@RequestBody Map<String, Object> params, HttpServletRequest request) {
        Object orderNoObj = params == null ? null : params.get("orderNo");
        String orderNo = orderNoObj == null ? null : String.valueOf(orderNoObj);
        if (orderNo == null || orderNo.isBlank()) {
            return error("订单号不能为空");
        }
        MiniLoginUser loginUser = tokenService.getLoginUser(request);
        if (loginUser == null) {
            return error("未登录");
        }
        return payService.prepay(orderNo.trim(), loginUser.getUserId());
    }
    
    @Operation(summary = "查询我的提现申请列表")
    @ApiOperationSupport(order = 2)
    @GetMapping("/withdraw/list")
    public AjaxResult withdrawList(HttpServletRequest request, @RequestParam(value = "status", required = false) Integer status) {
        MiniLoginUser loginUser = tokenService.getLoginUser(request);
        if (loginUser == null) {
            return error("未登录");
        }
        startPage();
        WithdrawApply query = new WithdrawApply();
        query.setUserId(loginUser.getUserId());
        query.setStatus(status);
        java.util.List<WithdrawApply> list = withdrawApplyService.selectWithdrawApplyList(query);
        AjaxResult ajax = AjaxResult.success();
        ajax.put("rows", list);
        ajax.put("total", new com.github.pagehelper.PageInfo(list).getTotal());
        return ajax;
    }
    
    @Operation(summary = "提交提现申请")
    @ApiOperationSupport(order = 3)
    @PostMapping("/withdraw/apply")
    public AjaxResult withdrawApply(@RequestBody Map<String, Object> params, HttpServletRequest request) {
        MiniLoginUser loginUser = tokenService.getLoginUser(request);
        if (loginUser == null) {
            return error("未登录");
        }
        Object amountObj = params == null ? null : params.get("amount");
        Long amount = amountObj == null ? null : (amountObj instanceof Number ? ((Number) amountObj).longValue() : Long.valueOf(String.valueOf(amountObj)));
        if (amount == null || amount <= 0L) {
            return error("提现金额异常");
        }
        MiniappConfig cfg = miniappConfigService.getCurrent();
        int rate = cfg == null || cfg.getWithdrawRate() == null ? 0 : cfg.getWithdrawRate();
        long credited = amount * (100 - rate) / 100;
        WithdrawApply apply = new WithdrawApply();
        apply.setUserId(loginUser.getUserId());
        apply.setAmount(amount);
        apply.setCreditedAmount(credited);
        apply.setRate(rate);
        apply.setStatus(0);
        apply.setCreateTime(new Date());
        apply.setUpdateTime(new Date());
        withdrawApplyService.insertWithdrawApply(apply);
        WechatUser user = wechatUserService.selectWechatUserById(loginUser.getUserId());
        long currentPending = user == null || user.getPendingCommission() == null ? 0L : user.getPendingCommission();
        long newPending = currentPending - amount;
        if (newPending < 0L) newPending = 0L;
        WechatUser updUser = new WechatUser();
        updUser.setId(loginUser.getUserId());
        updUser.setPendingCommission(newPending);
        wechatUserService.updateWechatUser(updUser);
        Map<String, Object> data = new HashMap<>();
        data.put("amount", amount);
        data.put("creditedAmount", credited);
        data.put("rate", rate);
        data.put("applyId", apply.getId());
        return success(data);
    }
}
