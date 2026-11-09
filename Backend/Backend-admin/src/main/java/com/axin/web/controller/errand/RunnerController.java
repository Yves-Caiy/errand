package com.axin.web.controller.errand;

import com.axin.common.annotation.Log;
import com.axin.common.core.controller.BaseController;
import com.axin.common.core.domain.AjaxResult;
import com.axin.common.core.page.TableDataInfo;
import com.axin.common.enums.BusinessType;
import com.axin.system.domain.Community;
import com.axin.system.domain.RunnerProfile;
import com.axin.system.domain.WechatUser;
import com.axin.system.domain.vo.RunnerProfileListVO;
import com.axin.system.service.ICommunityService;
import com.axin.system.service.IRunnerProfileService;
import com.axin.system.service.IWechatUserService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Validated
@Tag(name = "后台接单员管理", description = "接单员档案与服务范围管理")
@RestController
@RequestMapping("/errand/runner")
public class RunnerController extends BaseController {

    @Autowired
    private IRunnerProfileService runnerProfileService;

    @Autowired
    private IWechatUserService wechatUserService;

    @Autowired
    private ICommunityService communityService;

    @Operation(summary = "接单员列表")
    @PreAuthorize("@ss.hasPermi('errand:runner:list')")
    @GetMapping("/list")
    public TableDataInfo list(RunnerProfile query) {
        startPage();
        List<RunnerProfile> list = runnerProfileService.selectRunnerProfileList(query);
        TableDataInfo data = getDataTable(list);
        List<RunnerProfileListVO> voList = new ArrayList<>();
        for (RunnerProfile p : list) {
            voList.add(toVO(p));
        }
        data.setRows(voList);
        return data;
    }

    @Operation(summary = "接单员详情")
    @PreAuthorize("@ss.hasPermi('errand:runner:query')")
    @GetMapping("/{userId}")
    public AjaxResult getInfo(@PathVariable Long userId) {
        RunnerProfile p = runnerProfileService.getByUserId(userId);
        Map<String, Object> res = new HashMap<>();
        res.put("profile", p == null ? toVO(buildDefault(userId)) : toVO(p));
        res.put("communityIds", runnerProfileService.selectCommunityIdsByRunner(userId));
        return success(res);
    }

    @Operation(summary = "新增或修改接单员档案")
    @PreAuthorize("@ss.hasPermi('errand:runner:edit')")
    @Log(title = "接单员管理", businessType = BusinessType.UPDATE)
    @PostMapping
    public AjaxResult save(@RequestBody RunnerProfile profile) {
        if (profile.getUserId() == null) {
            return error("userId 不能为空");
        }
        return toAjax(runnerProfileService.saveRunnerProfile(profile));
    }

    @Operation(summary = "设置接单员服务小区")
    @PreAuthorize("@ss.hasPermi('errand:runner:edit')")
    @Log(title = "接单员管理", businessType = BusinessType.UPDATE)
    @PostMapping("/community")
    public AjaxResult setCommunity(@RequestBody Map<String, Object> body) {
        Object uid = body.get("userId");
        if (uid == null) {
            return error("userId 不能为空");
        }
        List<Long> ids = new ArrayList<>();
        Object raw = body.get("communityIds");
        if (raw instanceof List) {
            for (Object o : (List<?>) raw) {
                if (o != null) {
                    ids.add(Long.parseLong(String.valueOf(o)));
                }
            }
        }
        return toAjax(runnerProfileService.setRunnerCommunities(Long.parseLong(String.valueOf(uid)), ids));
    }

    @Operation(summary = "启用/停用接单员")
    @PreAuthorize("@ss.hasPermi('errand:runner:edit')")
    @Log(title = "接单员管理", businessType = BusinessType.UPDATE)
    @PutMapping("/status")
    public AjaxResult changeStatus(@RequestBody Map<String, Object> body) {
        Object uid = body.get("userId");
        Object st = body.get("isRunner");
        if (uid == null) {
            return error("userId 不能为空");
        }
        RunnerProfile p = new RunnerProfile();
        p.setUserId(Long.parseLong(String.valueOf(uid)));
        p.setIsRunner(st == null ? 1 : Integer.parseInt(String.valueOf(st)));
        return toAjax(runnerProfileService.saveRunnerProfile(p));
    }

    private RunnerProfile buildDefault(Long userId) {
        RunnerProfile p = new RunnerProfile();
        p.setUserId(userId);
        p.setIsRunner(0);
        p.setOnlineStatus(0);
        p.setIsAllCommunity(0);
        p.setMaxConcurrent(3);
        p.setWeightBoost(0);
        return p;
    }

    private RunnerProfileListVO toVO(RunnerProfile p) {
        RunnerProfileListVO vo = new RunnerProfileListVO();
        vo.setId(p.getId());
        vo.setUserId(p.getUserId());
        vo.setIsRunner(p.getIsRunner());
        vo.setOnlineStatus(p.getOnlineStatus());
        vo.setIsAllCommunity(p.getIsAllCommunity());
        vo.setMaxConcurrent(p.getMaxConcurrent());
        vo.setSkills(p.getSkills());
        vo.setWeightBoost(p.getWeightBoost());
        vo.setCreateTime(p.getCreateTime());
        vo.setUpdateTime(p.getUpdateTime());
        if (p.getUserId() != null) {
            WechatUser u = wechatUserService.selectWechatUserById(p.getUserId());
            if (u != null) {
                vo.setNickname(u.getNickname());
                vo.setAvatarUrl(u.getAvatarUrl());
                vo.setPhone(u.getPhone());
            }
            List<Long> cids = runnerProfileService.selectCommunityIdsByRunner(p.getUserId());
            if (cids != null && !cids.isEmpty()) {
                StringBuilder names = new StringBuilder();
                StringBuilder idStr = new StringBuilder();
                for (Long cid : cids) {
                    Community c = communityService.selectCommunityById(cid);
                    if (names.length() > 0) {
                        names.append(",");
                        idStr.append(",");
                    }
                    names.append(c == null ? cid : c.getCommunityName());
                    idStr.append(cid);
                }
                vo.setCommunityNames(names.toString());
                vo.setCommunityIds(idStr.toString());
            }
        }
        return vo;
    }
}
