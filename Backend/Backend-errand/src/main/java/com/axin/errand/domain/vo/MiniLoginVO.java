package com.axin.errand.domain.vo;

import io.swagger.v3.oas.annotations.media.Schema;

@Schema(name = "MiniLoginVO")
public class MiniLoginVO {
    @Schema(title = "令牌")
    private String token;
    @Schema(title = "过期毫秒时间")
    private Long expireTime;

    public String getToken() { return token; }
    public void setToken(String token) { this.token = token; }
    public Long getExpireTime() { return expireTime; }
    public void setExpireTime(Long expireTime) { this.expireTime = expireTime; }
}

