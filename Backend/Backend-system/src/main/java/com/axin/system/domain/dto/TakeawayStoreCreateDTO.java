package com.axin.system.domain.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;

@Schema(name = "TakeawayStoreCreateDTO", description = "外卖店铺创建入参")
public class TakeawayStoreCreateDTO {
    @Schema(title = "关联小区id", requiredMode = Schema.RequiredMode.REQUIRED)
    @NotNull(message = "小区id不能为空")
    private Long communityId;

    @Schema(title = "店铺名称", requiredMode = Schema.RequiredMode.REQUIRED)
    @NotBlank(message = "店铺名称不能为空")
    @Size(max = 100, message = "店铺名称长度不能超过100")
    private String storeName;

    @Schema(title = "店铺头像URL")
    @Size(max = 255, message = "头像URL长度不能超过255")
    private String avatarUrl;

    @Schema(title = "店铺地址")
    @Size(max = 200, message = "地址长度不能超过200")
    private String address;

    @Schema(title = "联系电话（手机号）")
    @Size(max = 11, message = "联系电话长度不能超过11")
    private String contact;

    @Schema(title = "是否启用（1启用 0禁用）")
    private Integer isEnabled;

    public Long getCommunityId() { return communityId; }
    public void setCommunityId(Long communityId) { this.communityId = communityId; }
    public String getStoreName() { return storeName; }
    public void setStoreName(String storeName) { this.storeName = storeName; }
    public String getAvatarUrl() { return avatarUrl; }
    public void setAvatarUrl(String avatarUrl) { this.avatarUrl = avatarUrl; }
    public String getAddress() { return address; }
    public void setAddress(String address) { this.address = address; }
    public String getContact() { return contact; }
    public void setContact(String contact) { this.contact = contact; }
    public Integer getIsEnabled() { return isEnabled; }
    public void setIsEnabled(Integer isEnabled) { this.isEnabled = isEnabled; }
}
