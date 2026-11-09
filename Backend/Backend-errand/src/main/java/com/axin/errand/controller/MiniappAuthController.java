package com.axin.errand.controller;

import com.axin.common.annotation.Anonymous;
import com.axin.common.core.controller.BaseController;
import com.axin.common.core.domain.AjaxResult;
import com.axin.common.utils.StringUtils;
import com.axin.errand.domain.MiniLoginUser;
import com.axin.errand.domain.dto.MiniappLoginDTO;
import com.axin.errand.domain.dto.PhoneAuthDTO;
import com.axin.errand.domain.vo.MiniLoginVO;
import com.axin.errand.service.MiniTokenService;
import com.axin.errand.service.MiniappWxService;
import com.axin.system.domain.WechatUser;
import com.axin.system.service.IWechatUserService;
import com.github.xiaoymin.knife4j.annotations.ApiOperationSupport;
import com.github.xiaoymin.knife4j.annotations.ApiSupport;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@Validated
@Tag(name = "小程序认证接口")
@ApiSupport(author = "AXIN", order = 1)
@RestController
@RequestMapping("/miniapp/auth")
public class MiniappAuthController extends BaseController {

    @Autowired
    private MiniTokenService tokenService;

    @Autowired
    private MiniappWxService wxService;

    @Autowired
    private IWechatUserService wechatUserService;

    @Operation(summary = "登录")
    @ApiOperationSupport(order = 10)
    @Anonymous
    @PostMapping("/login")
    public AjaxResult login(@Valid @RequestBody MiniappLoginDTO dto) {
        String openid = wxService.getOpenidByCode(dto.getCode());
        if (StringUtils.isEmpty(openid)) {
            return error("登录失败");
        }
        WechatUser user = wechatUserService.selectByOpenid(openid);
        if (user == null) {
            user = new WechatUser();
            user.setOpenid(openid);
            user.setNickname(dto.getNickname());
            user.setAvatarUrl(dto.getAvatarUrl());
            user.setIsEnabled(1);
            wechatUserService.insertWechatUser(user);
        } else {
            if (dto.getNickname() != null || dto.getAvatarUrl() != null) {
                WechatUser upd = new WechatUser();
                upd.setId(user.getId());
                upd.setNickname(dto.getNickname());
                upd.setAvatarUrl(dto.getAvatarUrl());
                wechatUserService.updateWechatUser(upd);
                user = wechatUserService.selectWechatUserById(user.getId());
            }
            if (user.getIsEnabled() != null && user.getIsEnabled() == 0) {
                return error("账号已被禁用");
            }
        }
        MiniLoginUser loginUser = new MiniLoginUser();
        loginUser.setUserId(user.getId());
        loginUser.setOpenid(user.getOpenid());
        loginUser.setNickname(user.getNickname());
        loginUser.setAvatarUrl(user.getAvatarUrl());
        loginUser.setPhone(user.getPhone());
        loginUser.setCommunityId(user.getCommunityId());
        String token = tokenService.createToken(loginUser);
        MiniLoginVO vo = new MiniLoginVO();
        vo.setToken(token);
        vo.setExpireTime(loginUser.getExpireTime());
        return success(vo);
    }

    @Operation(summary = "手机号授权")
    @ApiOperationSupport(order = 30)
    @PostMapping("/phone")
    public AjaxResult phone(HttpServletRequest request, @Valid @RequestBody PhoneAuthDTO dto) {
        MiniLoginUser loginUser = tokenService.getLoginUser(request);
        if (loginUser == null) {
            return error("未登录");
        }
        String phone = wxService.getPhoneNumber(dto.getCode());
        if (StringUtils.isEmpty(phone)) {
            return error("获取手机号失败");
        }
        WechatUser upd = new WechatUser();
        upd.setId(loginUser.getUserId());
        upd.setPhone(phone);
        wechatUserService.updateWechatUser(upd);
        return success(phone);
    }
}
