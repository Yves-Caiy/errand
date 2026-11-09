package com.axin.errand.service;

import com.axin.common.core.redis.RedisCache;
import com.axin.common.utils.StringUtils;
import com.axin.errand.domain.MiniLoginUser;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.util.UUID;
import java.util.concurrent.TimeUnit;

@Component
public class MiniTokenService {
    private static final String KEY_PREFIX = "MINIAPP_LOGIN_TOKEN:";

    @Value("${miniapp.token.header:Mini-Token}")
    private String header;

    @Value("${miniapp.token.expireTime:43200}")
    private int expireTime;

    @Autowired
    private RedisCache redisCache;

    @Schema(title = "创建令牌")
    public String createToken(MiniLoginUser user) {
        String token = UUID.randomUUID().toString().replaceAll("-", "");
        user.setToken(token);
        long now = System.currentTimeMillis();
        user.setLoginTime(now);
        user.setExpireTime(now + expireTime * 60L * 1000L);
        redisCache.setCacheObject(KEY_PREFIX + token, user, expireTime, TimeUnit.MINUTES);
        return token;
    }

    @Schema(title = "从请求获取登录用户")
    public MiniLoginUser getLoginUser(HttpServletRequest request) {
        String token = request.getHeader(header);
        if (StringUtils.isEmpty(token)) {
            return null;
        }
        return redisCache.getCacheObject(KEY_PREFIX + token);
    }
}
