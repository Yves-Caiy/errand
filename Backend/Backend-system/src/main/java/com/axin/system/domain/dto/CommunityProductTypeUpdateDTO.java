package com.axin.system.domain.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotNull;

@Schema(name = "CommunityProductTypeUpdateDTO", description = "小区-商品类型关联更新入参")
public class CommunityProductTypeUpdateDTO {
    @Schema(title = "主键ID", requiredMode = Schema.RequiredMode.REQUIRED)
    @NotNull(message = "ID不能为空")
    private Long id;

    @Schema(title = "小区id")
    private Long communityId;

    @Schema(title = "商品类型id")
    private Long productTypeId;

    @Schema(title = "是否启用（1启用 0禁用）")
    private Integer isEnabled;

    @Schema(title = "排序序号（控制模块展示顺序）")
    private Integer sortOrder;

    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }
    public Long getCommunityId() { return communityId; }
    public void setCommunityId(Long communityId) { this.communityId = communityId; }
    public Long getProductTypeId() { return productTypeId; }
    public void setProductTypeId(Long productTypeId) { this.productTypeId = productTypeId; }
    public Integer getIsEnabled() { return isEnabled; }
    public void setIsEnabled(Integer isEnabled) { this.isEnabled = isEnabled; }
    public Integer getSortOrder() { return sortOrder; }
    public void setSortOrder(Integer sortOrder) { this.sortOrder = sortOrder; }
}
