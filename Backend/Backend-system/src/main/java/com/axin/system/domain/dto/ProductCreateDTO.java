package com.axin.system.domain.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;

import java.math.BigDecimal;

@Schema(name = "ProductCreateDTO", description = "商品规格创建入参")
public class ProductCreateDTO {
    @Schema(title = "类型id", requiredMode = Schema.RequiredMode.REQUIRED)
    @NotNull(message = "类型id不能为空")
    private Long typeId;

    @Schema(title = "规格/服务名称", requiredMode = Schema.RequiredMode.REQUIRED)
    @NotBlank(message = "规格名称不能为空")
    @Size(max = 100, message = "规格名称长度不能超过100")
    private String specName;

    private BigDecimal weightMin;
    private BigDecimal weightMax;
    private BigDecimal distance;
    private Long amountMin;
    private Long amountMax;
    private Integer duration;
    @Size(max = 5, message = "时长单位长度不能超过5")
    private String durationUnit;

    @Schema(title = "最低价格（分）", requiredMode = Schema.RequiredMode.REQUIRED)
    @NotNull(message = "最低价格不能为空")
    private Long priceMin;

    @Schema(title = "最高价格（分）", requiredMode = Schema.RequiredMode.REQUIRED)
    @NotNull(message = "最高价格不能为空")
    private Long priceMax;

    private Integer isUniversalCustom;
    private Integer isEnabled;

    public Long getTypeId() { return typeId; }
    public void setTypeId(Long typeId) { this.typeId = typeId; }
    public String getSpecName() { return specName; }
    public void setSpecName(String specName) { this.specName = specName; }
    public BigDecimal getWeightMin() { return weightMin; }
    public void setWeightMin(BigDecimal weightMin) { this.weightMin = weightMin; }
    public BigDecimal getWeightMax() { return weightMax; }
    public void setWeightMax(BigDecimal weightMax) { this.weightMax = weightMax; }
    public BigDecimal getDistance() { return distance; }
    public void setDistance(BigDecimal distance) { this.distance = distance; }
    public Long getAmountMin() { return amountMin; }
    public void setAmountMin(Long amountMin) { this.amountMin = amountMin; }
    public Long getAmountMax() { return amountMax; }
    public void setAmountMax(Long amountMax) { this.amountMax = amountMax; }
    public Integer getDuration() { return duration; }
    public void setDuration(Integer duration) { this.duration = duration; }
    public String getDurationUnit() { return durationUnit; }
    public void setDurationUnit(String durationUnit) { this.durationUnit = durationUnit; }
    public Long getPriceMin() { return priceMin; }
    public void setPriceMin(Long priceMin) { this.priceMin = priceMin; }
    public Long getPriceMax() { return priceMax; }
    public void setPriceMax(Long priceMax) { this.priceMax = priceMax; }
    public Integer getIsUniversalCustom() { return isUniversalCustom; }
    public void setIsUniversalCustom(Integer isUniversalCustom) { this.isUniversalCustom = isUniversalCustom; }
    public Integer getIsEnabled() { return isEnabled; }
    public void setIsEnabled(Integer isEnabled) { this.isEnabled = isEnabled; }
}
