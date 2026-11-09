package com.axin.system.domain.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.Size;

@Schema(name = "ServicePackageQueryDTO", description = "服务套餐查询条件")
public class ServicePackageQueryDTO {
    @Schema(title = "类型id")
    private Long typeId;

    @Schema(title = "套餐名称(模糊)")
    @Size(max = 100, message = "套餐名称长度不能超过100")
    private String packageName;

    @Schema(title = "时长类型（1包月 2包年）")
    private Integer durationType;

    @Schema(title = "是否启用（1启用 0禁用）")
    private Integer isEnabled;

    public Long getTypeId() { return typeId; }
    public void setTypeId(Long typeId) { this.typeId = typeId; }
    public String getPackageName() { return packageName; }
    public void setPackageName(String packageName) { this.packageName = packageName; }
    public Integer getDurationType() { return durationType; }
    public void setDurationType(Integer durationType) { this.durationType = durationType; }
    public Integer getIsEnabled() { return isEnabled; }
    public void setIsEnabled(Integer isEnabled) { this.isEnabled = isEnabled; }
}

