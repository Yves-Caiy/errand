package com.axin.errand.interceptor;

import com.alibaba.fastjson2.JSON;
import com.axin.common.core.domain.AjaxResult;
import com.axin.common.utils.ServletUtils;
import com.axin.errand.domain.MiniLoginUser;
import com.axin.errand.service.MiniTokenService;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.HandlerInterceptor;

@Component
public class MiniappAuthInterceptor implements HandlerInterceptor {
    @Autowired
    private MiniTokenService tokenService;

    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
        MiniLoginUser loginUser = tokenService.getLoginUser(request);
        if (loginUser == null || loginUser.getExpireTime() == null || loginUser.getExpireTime() < System.currentTimeMillis()) {
            ServletUtils.renderString(response, JSON.toJSONString(AjaxResult.error("未登录或登录已过期")));
            return false;
        }
        request.setAttribute("MINI_LOGIN_USER", loginUser);
        return true;
    }
}

