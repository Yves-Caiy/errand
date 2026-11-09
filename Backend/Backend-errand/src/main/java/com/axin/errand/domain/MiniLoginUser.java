package com.axin.errand.domain;

import io.swagger.v3.oas.annotations.media.Schema;

@Schema(name = "MiniLoginUser")
public class MiniLoginUser {
    @Schema(title = "令牌")
    private String token;
    @Schema(title = "用户ID")
    private Long userId;
    @Schema(title = "openid")
    private String openid;
    @Schema(title = "昵称")
    private String nickname;
    @Schema(title = "头像")
    private String avatarUrl;
    @Schema(title = "手机号")
    private String phone;
    @Schema(title = "小区id")
    private Long communityId;
    @Schema(title = "登录时间毫秒")
    private Long loginTime;
    @Schema(title = "过期时间毫秒")
    private Long expireTime;

    public String getToken() { return token; }
    public void setToken(String token) { this.token = token; }
    public Long getUserId() { return userId; }
    public void setUserId(Long userId) { this.userId = userId; }
    public String getOpenid() { return openid; }
    public void setOpenid(String openid) { this.openid = openid; }
    public String getNickname() { return nickname; }
    public void setNickname(String nickname) { this.nickname = nickname; }
    public String getAvatarUrl() { return avatarUrl; }
    public void setAvatarUrl(String avatarUrl) { this.avatarUrl = avatarUrl; }
    public String getPhone() { return phone; }
    public void setPhone(String phone) { this.phone = phone; }
    public Long getCommunityId() { return communityId; }
    public void setCommunityId(Long communityId) { this.communityId = communityId; }
    public Long getLoginTime() { return loginTime; }
    public void setLoginTime(Long loginTime) { this.loginTime = loginTime; }
    public Long getExpireTime() { return expireTime; }
    public void setExpireTime(Long expireTime) { this.expireTime = expireTime; }
}
