package com.axin.system.domain.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;

@Schema(name = "ServicePackageUpdateDTO", description = "服务套餐更新入参")
public class ServicePackageUpdateDTO {
    @Schema(title = "主键ID", requiredMode = Schema.RequiredMode.REQUIRED)
    @NotNull(message = "ID不能为空")
    private Long id;

    private Long typeId;

    @Size(max = 100, message = "套餐名称长度不能超过100")
    private String packageName;

    private Long price;
    private Long firstMonthPrice;
    private Integer durationType;
    private Integer durationDays;

    @Size(max = 200, message = "包含服务描述长度不能超过200")
    private String includeService;

    private Integer isEnabled;

    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }
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

