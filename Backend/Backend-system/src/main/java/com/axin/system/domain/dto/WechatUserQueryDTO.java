package com.axin.system.domain.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.Size;

@Schema(name = "WechatUserQueryDTO", description = "微信用户查询条件")
public class WechatUserQueryDTO {
    @Schema(title = "openid")
    @Size(max = 50, message = "openid长度不能超过50")
    private String openid;

    @Schema(title = "昵称(模糊)")
    @Size(max = 50, message = "昵称长度不能超过50")
    private String nickname;

    @Schema(title = "手机号")
    @Size(max = 11, message = "手机号长度不能超过11")
    private String phone;

    @Schema(title = "小区id")
    private Long communityId;

    @Schema(title = "是否启用")
    private Integer isEnabled;

    public String getOpenid() { return openid; }
    public void setOpenid(String openid) { this.openid = openid; }
    public String getNickname() { return nickname; }
    public void setNickname(String nickname) { this.nickname = nickname; }
    public String getPhone() { return phone; }
    public void setPhone(String phone) { this.phone = phone; }
    public Long getCommunityId() { return communityId; }
    public void setCommunityId(Long communityId) { this.communityId = communityId; }
    public Integer getIsEnabled() { return isEnabled; }
    public void setIsEnabled(Integer isEnabled) { this.isEnabled = isEnabled; }
}
