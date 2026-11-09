package com.axin.errand.controller;

import com.axin.common.core.controller.BaseController;
import com.axin.common.core.domain.AjaxResult;
import com.axin.errand.domain.MiniLoginUser;
import com.axin.errand.domain.dto.MiniappUpdateProfileDTO;
import com.axin.errand.service.MiniTokenService;
import com.axin.system.domain.WechatUser;
import com.axin.system.service.IWechatUserService;
import com.github.xiaoymin.knife4j.annotations.ApiOperationSupport;
import com.github.xiaoymin.knife4j.annotations.ApiSupport;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import com.axin.errand.domain.vo.MiniappUserProfileVO;
import com.axin.system.domain.Community;
import com.axin.system.service.ICommunityService;
import org.springframework.beans.BeanUtils;

@Validated
@Tag(name = "小程序用户接口")
@ApiSupport(author = "AXIN", order = 10)
@RestController
@RequestMapping("/miniapp/user")
public class MiniappUserController extends BaseController {

    @Autowired
    private IWechatUserService wechatUserService;

    @Autowired
    private ICommunityService communityService;

    @Autowired
    private MiniTokenService tokenService;

    @Operation(summary = "获取个人信息")
    @ApiOperationSupport(order = 10)
    @GetMapping("/profile")
    public AjaxResult getProfile(HttpServletRequest request) {
        MiniLoginUser loginUser = tokenService.getLoginUser(request);
        if (loginUser == null) {
            return error("未登录");
        }
        WechatUser user = wechatUserService.selectWechatUserById(loginUser.getUserId());
        
        MiniappUserProfileVO vo = new MiniappUserProfileVO();
        BeanUtils.copyProperties(user, vo);
        
        if (user.getCommunityId() != null) {
            Community community = communityService.selectCommunityById(user.getCommunityId());
            if (community != null) {
                vo.setCommunityName(community.getCommunityName());
            }
        }
        
        return success(vo);
    }

    @Operation(summary = "更新个人信息")
    @ApiOperationSupport(order = 20)
    @PutMapping("/profile")
    public AjaxResult updateProfile(HttpServletRequest request, @RequestBody MiniappUpdateProfileDTO dto) {
        MiniLoginUser loginUser = tokenService.getLoginUser(request);
        if (loginUser == null) {
            return error("未登录");
        }
        WechatUser user = new WechatUser();
        user.setId(loginUser.getUserId());
        user.setNickname(dto.getNickname());
        user.setAvatarUrl(dto.getAvatarUrl());
        user.setDetailAddress(dto.getDetailAddress());
        user.setCommunityId(dto.getCommunityId());
        wechatUserService.updateWechatUser(user);
        return success();
    }
}
