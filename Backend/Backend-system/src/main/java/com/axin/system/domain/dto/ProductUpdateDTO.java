package com.axin.system.domain.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;

import java.math.BigDecimal;

@Schema(name = "ProductUpdateDTO", description = "商品规格更新入参")
public class ProductUpdateDTO {
    @Schema(title = "主键ID", requiredMode = Schema.RequiredMode.REQUIRED)
    @NotNull(message = "ID不能为空")
    private Long id;

    private Long typeId;

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
    private Long priceMin;
    private Long priceMax;
    private Integer isUniversalCustom;
    private Integer isEnabled;

    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }
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
