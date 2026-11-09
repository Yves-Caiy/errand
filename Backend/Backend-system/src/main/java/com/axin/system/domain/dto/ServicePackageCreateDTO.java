package com.axin.system.domain.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;

@Schema(name = "ServicePackageCreateDTO", description = "服务套餐创建入参")
public class ServicePackageCreateDTO {
    @Schema(title = "类型id", requiredMode = Schema.RequiredMode.REQUIRED)
    @NotNull(message = "类型id不能为空")
    private Long typeId;

    @Schema(title = "套餐名称", requiredMode = Schema.RequiredMode.REQUIRED)
    @NotBlank(message = "套餐名称不能为空")
    @Size(max = 100, message = "套餐名称长度不能超过100")
    private String packageName;

    @Schema(title = "套餐价格（分）", requiredMode = Schema.RequiredMode.REQUIRED)
    @NotNull(message = "套餐价格不能为空")
    private Long price;

    @Schema(title = "首月优惠价（分）")
    private Long firstMonthPrice;

    @Schema(title = "时长类型（1包月 2包年）", requiredMode = Schema.RequiredMode.REQUIRED)
    @NotNull(message = "时长类型不能为空")
    private Integer durationType;

    @Schema(title = "套餐时长（天）", requiredMode = Schema.RequiredMode.REQUIRED)
    @NotNull(message = "套餐时长不能为空")
    private Integer durationDays;

    @Schema(title = "包含服务描述")
    @Size(max = 200, message = "包含服务描述长度不能超过200")
    private String includeService;

    @Schema(title = "是否启用（1启用 0禁用）")
    private Integer isEnabled;

    public Long getTypeId() { return typeId; }
    public void setTypeId(Long typeId) { this.typeId = typeId; }
    public String getPackageName() { return packageName; }
    public void setPackageName(String packageName) { this.packageName = packageName; }
    public Long getPrice() { return price; }
    public void setPrice(Long price) { this.price = price; }
    public Long getFirstMonthPrice() { return firstMonthPrice; }
    public void setFirstMonthPrice(Long firstMonthPrice) { this.firstMonthPrice = firstMonthPrice; }
    public Integer getDurationType() { return durationType; }
    public void setDurationType(Integer durationType) { this.durationType = durationType; }
    public Integer getDurationDays() { return durationDays; }
    public void setDurationDays(Integer durationDays) { this.durationDays = durationDays; }
    public String getIncludeService() { return includeService; }
    public void setIncludeService(String includeService) { this.includeService = includeService; }
    public Integer getIsEnabled() { return isEnabled; }
    public void setIsEnabled(Integer isEnabled) { this.isEnabled = isEnabled; }
}

