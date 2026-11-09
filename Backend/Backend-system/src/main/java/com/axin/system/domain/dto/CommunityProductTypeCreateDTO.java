package com.axin.system.domain.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotNull;

@Schema(name = "CommunityProductTypeCreateDTO", description = "小区-商品类型关联创建入参")
public class CommunityProductTypeCreateDTO {
    @Schema(title = "小区id", requiredMode = Schema.RequiredMode.REQUIRED)
    @NotNull(message = "小区id不能为空")
    private Long communityId;

    @Schema(title = "商品类型id", requiredMode = Schema.RequiredMode.REQUIRED)
    @NotNull(message = "商品类型id不能为空")
    private Long productTypeId;

    @Schema(title = "是否启用（1启用 0禁用）")
    private Integer isEnabled;

    @Schema(title = "排序序号（控制模块展示顺序）")
    private Integer sortOrder;

    public Long getCommunityId() { return communityId; }
    public void setCommunityId(Long communityId) { this.communityId = communityId; }
    public Long getProductTypeId() { return productTypeId; }
    public void setProductTypeId(Long productTypeId) { this.productTypeId = productTypeId; }
    public Integer getIsEnabled() { return isEnabled; }
    public void setIsEnabled(Integer isEnabled) { this.isEnabled = isEnabled; }
    public Integer getSortOrder() { return sortOrder; }
    public void setSortOrder(Integer sortOrder) { this.sortOrder = sortOrder; }
}
