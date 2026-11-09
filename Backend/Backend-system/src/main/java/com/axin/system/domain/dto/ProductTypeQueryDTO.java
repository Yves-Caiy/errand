package com.axin.system.domain.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.Size;

@Schema(name = "ProductTypeQueryDTO", description = "商品类型查询条件")
public class ProductTypeQueryDTO {
    @Schema(title = "模块名称(模糊)")
    @Size(max = 50, message = "模块名称长度不能超过50")
    private String typeName;

    @Schema(title = "是否可增删改（1是 0否）")
    private Integer isManageable;

    @Schema(title = "是否启用（1启用 0禁用）")
    private Integer isEnabled;

    public String getTypeName() { return typeName; }
    public void setTypeName(String typeName) { this.typeName = typeName; }
    public Integer getIsManageable() { return isManageable; }
    public void setIsManageable(Integer isManageable) { this.isManageable = isManageable; }
    public Integer getIsEnabled() { return isEnabled; }
    public void setIsEnabled(Integer isEnabled) { this.isEnabled = isEnabled; }
}

