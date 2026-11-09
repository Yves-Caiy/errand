package com.axin.system.domain.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.Size;

@Schema(name = "ProductQueryDTO", description = "商品规格查询条件")
public class ProductQueryDTO {
    @Schema(title = "类型id")
    private Long typeId;

    @Schema(title = "规格/服务名称(模糊)")
    @Size(max = 100, message = "规格名称长度不能超过100")
    private String specName;

    @Schema(title = "是否为万能服务用户自定义（1是 0否）")
    private Integer isUniversalCustom;

    @Schema(title = "是否启用（1启用 0禁用）")
    private Integer isEnabled;

    public Long getTypeId() { return typeId; }
    public void setTypeId(Long typeId) { this.typeId = typeId; }
    public String getSpecName() { return specName; }
    public void setSpecName(String specName) { this.specName = specName; }
    public Integer getIsUniversalCustom() { return isUniversalCustom; }
    public void setIsUniversalCustom(Integer isUniversalCustom) { this.isUniversalCustom = isUniversalCustom; }
    public Integer getIsEnabled() { return isEnabled; }
    public void setIsEnabled(Integer isEnabled) { this.isEnabled = isEnabled; }
}

