package com.axin.errand.domain.dto;

import io.swagger.v3.oas.annotations.media.Schema;

@Schema(name = "MiniappUpdateProfileDTO")
public class MiniappUpdateProfileDTO {
    @Schema(title = "昵称")
    private String nickname;
    @Schema(title = "头像")
    private String avatarUrl;
    @Schema(title = "详细地址")
    private String detailAddress;

    @Schema(title = "小区id")
    private Long communityId;

    public String getNickname() { return nickname; }
    public void setNickname(String nickname) { this.nickname = nickname; }
    public String getAvatarUrl() { return avatarUrl; }
    public void setAvatarUrl(String avatarUrl) { this.avatarUrl = avatarUrl; }
    public String getDetailAddress() { return detailAddress; }
    public void setDetailAddress(String detailAddress) { this.detailAddress = detailAddress; }
    public Long getCommunityId() { return communityId; }
    public void setCommunityId(Long communityId) { this.communityId = communityId; }
}
