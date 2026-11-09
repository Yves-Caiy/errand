package com.axin.system.domain.vo;

import io.swagger.v3.oas.annotations.media.Schema;

import java.math.BigDecimal;
import java.util.Date;

@Schema(name = "ProductVO", description = "商品规格出参")
public class ProductVO {
    @Schema(title = "主键")
    private Long id;
    @Schema(title = "类型id")
    private Long typeId;
    @Schema(title = "规格/服务名称")
    private String specName;
    private BigDecimal weightMin;
    private BigDecimal weightMax;
    private BigDecimal distance;
    private Long amountMin;
    private Long amountMax;
    private Integer duration;
    private String durationUnit;
    private Long priceMin;
    private Long priceMax;
    private Integer isUniversalCustom;
    private Integer isEnabled;
    private Date createTime;
    private Date updateTime;

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
    public Date getCreateTime() { return createTime; }
    public void setCreateTime(Date createTime) { this.createTime = createTime; }
    public Date getUpdateTime() { return updateTime; }
    public void setUpdateTime(Date updateTime) { this.updateTime = updateTime; }
}
