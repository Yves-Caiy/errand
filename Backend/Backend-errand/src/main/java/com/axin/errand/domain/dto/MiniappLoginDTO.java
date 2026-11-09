package com.axin.errand.domain.dto;

import io.swagger.v3.oas.annotations.media.Schema;

@Schema(name = "MiniappLoginDTO")
public class MiniappLoginDTO {
    @Schema(title = "登录code")
    private String code;
    @Schema(title = "昵称")
    private String nickname;
    @Schema(title = "头像")
    private String avatarUrl;

    public String getCode() { return code; }
    public void setCode(String code) { this.code = code; }
    public String getNickname() { return nickname; }
    public void setNickname(String nickname) { this.nickname = nickname; }
    public String getAvatarUrl() { return avatarUrl; }
    public void setAvatarUrl(String avatarUrl) { this.avatarUrl = avatarUrl; }
}

