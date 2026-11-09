package com.axin.errand.service;

import com.alibaba.fastjson2.JSON;
import com.alibaba.fastjson2.JSONObject;
import com.axin.common.core.redis.RedisCache;
import com.axin.common.utils.StringUtils;
import com.axin.common.utils.http.HttpUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Service;

import java.util.concurrent.TimeUnit;

@Service
public class MiniappWxService {
    @Value("${miniapp.appid:wxde017136cabccf1d}")
    private String appid;
    @Value("${miniapp.secret:4fe4d48a31199735ab80056ec332bd8f}")
    private String secret;

    @Autowired
    private RedisCache redisCache;

    public String getOpenidByCode(String code) {
        String url = "https://api.weixin.qq.com/sns/jscode2session";
        String params = "appid=" + appid + "&secret=" + secret + "&js_code=" + code + "&grant_type=authorization_code";
        String resp = HttpUtils.sendGet(url, params);
        JSONObject obj = JSON.parseObject(resp);
        return obj.getString("openid");
    }

    public String getAccessToken() {
        String cacheKey = "MINIAPP_ACCESS_TOKEN";
        String cached = redisCache.getCacheObject(cacheKey);
        if (StringUtils.isNotEmpty(cached)) {
            return cached;
        }
        String url = "https://api.weixin.qq.com/cgi-bin/token";
        String params = "grant_type=client_credential&appid=" + appid + "&secret=" + secret;
        String resp = HttpUtils.sendGet(url, params);
        JSONObject obj = JSON.parseObject(resp);
        String token = obj.getString("access_token");
        Integer expiresIn = obj.getInteger("expires_in");
        if (StringUtils.isNotEmpty(token)) {
            redisCache.setCacheObject(cacheKey, token, expiresIn != null ? expiresIn - 60 : 7000, TimeUnit.SECONDS);
        }
        return token;
    }

    public String getPhoneNumber(String code) {
        String accessToken = getAccessToken();
        String url = "https://api.weixin.qq.com/wxa/business/getuserphonenumber?access_token=" + accessToken;
        String body = "{\"code\":\"" + code + "\"}";
        String resp = HttpUtils.sendPost(url, body, MediaType.APPLICATION_JSON_VALUE);
        JSONObject obj = JSON.parseObject(resp);
        Integer errcode = obj.getInteger("errcode");
        if (errcode != null && errcode != 0) {
            return null;
        }
        JSONObject phoneInfo = obj.getJSONObject("phone_info");
        if (phoneInfo == null) {
            return null;
        }
        String pure = phoneInfo.getString("purePhoneNumber");
        String full = phoneInfo.getString("phoneNumber");
        return StringUtils.isNotEmpty(pure) ? pure : full;
    }
}
