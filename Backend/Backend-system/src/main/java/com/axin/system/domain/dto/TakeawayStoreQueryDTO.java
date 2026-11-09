package com.axin.system.domain.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.Size;

@Schema(name = "TakeawayStoreQueryDTO", description = "外卖店铺查询条件")
public class TakeawayStoreQueryDTO {
    @Schema(title = "关联小区id")
    private Long communityId;

    @Schema(title = "店铺名称(模糊)")
    @Size(max = 100, message = "店铺名称长度不能超过100")
    private String storeName;

    @Schema(title = "是否启用（1启用 0禁用）")
    private Integer isEnabled;

    public Long getCommunityId() { return communityId; }
    public void setCommunityId(Long communityId) { this.communityId = communityId; }
    public String getStoreName() { return storeName; }
    public void setStoreName(String storeName) { this.storeName = storeName; }
    public Integer getIsEnabled() { return isEnabled; }
    public void setIsEnabled(Integer isEnabled) { this.isEnabled = isEnabled; }
}

