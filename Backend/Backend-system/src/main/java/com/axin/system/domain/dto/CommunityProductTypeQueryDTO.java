package com.axin.system.domain.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotNull;

@Schema(name = "CommunityProductTypeQueryDTO", description = "小区-商品类型关联查询条件")
public class CommunityProductTypeQueryDTO {
    @Schema(title = "小区id")
    private Long communityId;

    @Schema(title = "商品类型id")
    private Long productTypeId;

    @Schema(title = "是否启用（1启用 0禁用）")
    private Integer isEnabled;

    public Long getCommunityId() { return communityId; }
    public void setCommunityId(Long communityId) { this.communityId = communityId; }
    public Long getProductTypeId() { return productTypeId; }
    public void setProductTypeId(Long productTypeId) { this.productTypeId = productTypeId; }
    public Integer getIsEnabled() { return isEnabled; }
    public void setIsEnabled(Integer isEnabled) { this.isEnabled = isEnabled; }
}

