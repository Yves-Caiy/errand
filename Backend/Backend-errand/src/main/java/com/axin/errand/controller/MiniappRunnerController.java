package com.axin.errand.controller;

import com.axin.common.core.controller.BaseController;
import com.axin.common.core.domain.AjaxResult;
import com.axin.errand.domain.MiniLoginUser;
import com.axin.errand.service.DispatchService;
import com.axin.errand.service.MiniTokenService;
import com.axin.system.domain.RunnerProfile;
import com.axin.system.service.IRunnerProfileService;
import com.github.xiaoymin.knife4j.annotations.ApiOperationSupport;
import com.github.xiaoymin.knife4j.annotations.ApiSupport;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Validated
@Tag(name = "小程序接单员接口")
@ApiSupport(author = "AXIN", order = 8)
@RestController
@RequestMapping("/miniapp/runner")
public class MiniappRunnerController extends BaseController {

    @Autowired
    private MiniTokenService tokenService;

    @Autowired
    private IRunnerProfileService runnerProfileService;

    @Autowired
    private DispatchService dispatchService;

    @Operation(summary = "我的接单员档案")
    @ApiOperationSupport(order = 10)
    @GetMapping("/profile")
    public AjaxResult profile(HttpServletRequest request) {
        MiniLoginUser loginUser = tokenService.getLoginUser(request);
        if (loginUser == null) {
            return error("未登录");
        }
        RunnerProfile profile = runnerProfileService.getByUserId(loginUser.getUserId());
        Map<String, Object> res = new HashMap<>();
        res.put("isRunner", profile != null && profile.getIsRunner() != null ? profile.getIsRunner() : 0);
        res.put("onlineStatus", profile == null ? 0 : profile.getOnlineStatus());
        res.put("maxConcurrent", profile == null ? 3 : profile.getMaxConcurrent());
        res.put("isAllCommunity", profile == null ? 0 : profile.getIsAllCommunity());
        res.put("skills", profile == null ? null : profile.getSkills());
        res.put("communityIds", runnerProfileService.selectCommunityIdsByRunner(loginUser.getUserId()));
        return success(res);
    }

    @Operation(summary = "切换接单状态")
    @ApiOperationSupport(order = 20)
    @PostMapping("/online")
    public AjaxResult toggleOnline(HttpServletRequest request, @RequestBody Map<String, Object> body) {
        MiniLoginUser loginUser = tokenService.getLoginUser(request);
        if (loginUser == null) {
            return error("未登录");
        }
        Object v = body.get("onlineStatus");
        int onlineStatus = 0;
        if (v != null) {
            onlineStatus = Integer.parseInt(String.valueOf(v));
        }
        RunnerProfile profile = new RunnerProfile();
        profile.setUserId(loginUser.getUserId());
        profile.setIsRunner(1);
        profile.setOnlineStatus(onlineStatus);
        runnerProfileService.saveRunnerProfile(profile);
        return success(true);
    }

    @Operation(summary = "指派给我的列表")
    @ApiOperationSupport(order = 30)
    @GetMapping("/dispatch/pending")
    public AjaxResult pending(HttpServletRequest request) {
        MiniLoginUser loginUser = tokenService.getLoginUser(request);
        if (loginUser == null) {
            return error("未登录");
        }
        List<Map<String, Object>> list = dispatchService.pendingForRunner(loginUser.getUserId());
        return success(list);
    }

    @Operation(summary = "接受派单")
    @ApiOperationSupport(order = 40)
    @PostMapping("/dispatch/accept")
    public AjaxResult accept(HttpServletRequest request, @RequestParam("orderId") Long orderId) {
        MiniLoginUser loginUser = tokenService.getLoginUser(request);
        if (loginUser == null) {
            return error("未登录");
        }
        int rows = dispatchService.acceptOrder(orderId, loginUser.getUserId());
        if (rows == 0) {
            return error("订单已被其他人接单");
        }
        return success(true);
    }

    @Operation(summary = "拒绝派单")
    @ApiOperationSupport(order = 50)
    @PostMapping("/dispatch/reject")
    public AjaxResult reject(HttpServletRequest request, @RequestParam("orderId") Long orderId,
                            @RequestParam(value = "reason", required = false) String reason) {
        MiniLoginUser loginUser = tokenService.getLoginUser(request);
        if (loginUser == null) {
            return error("未登录");
        }
        dispatchService.rejectOrder(orderId, loginUser.getUserId());
        return success(true);
    }

    @Operation(summary = "未读来单提醒列表")
    @ApiOperationSupport(order = 60)
    @GetMapping("/notice/list")
    public AjaxResult noticeList(HttpServletRequest request) {
        MiniLoginUser loginUser = tokenService.getLoginUser(request);
        if (loginUser == null) {
            return error("未登录");
        }
        return success(dispatchService.noticeList(loginUser.getUserId()));
    }

    @Operation(summary = "未读来单数")
    @ApiOperationSupport(order = 61)
    @GetMapping("/notice/unread")
    public AjaxResult noticeUnread(HttpServletRequest request) {
        MiniLoginUser loginUser = tokenService.getLoginUser(request);
        if (loginUser == null) {
            return error("未登录");
        }
        Map<String, Object> res = new HashMap<>();
        res.put("count", dispatchService.noticeUnread(loginUser.getUserId()));
        return success(res);
    }

    @Operation(summary = "标记来单提醒已读")
    @ApiOperationSupport(order = 62)
    @PostMapping("/notice/read")
    public AjaxResult noticeRead(HttpServletRequest request,
                                @RequestBody(required = false) Map<String, Object> body) {
        MiniLoginUser loginUser = tokenService.getLoginUser(request);
        if (loginUser == null) {
            return error("未登录");
        }
        List<Long> logIds = new ArrayList<>();
        Object raw = body == null ? null : body.get("logIds");
        if (raw instanceof List) {
            for (Object o : (List<?>) raw) {
                if (o != null) {
                    logIds.add(Long.parseLong(String.valueOf(o)));
                }
            }
        }
        dispatchService.noticeRead(loginUser.getUserId(), logIds);
        return success(true);
    }
}
