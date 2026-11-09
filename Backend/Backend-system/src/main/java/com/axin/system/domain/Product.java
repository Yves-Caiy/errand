package com.axin.system.domain;

import io.swagger.v3.oas.annotations.media.Schema;

import java.math.BigDecimal;
import java.util.Date;

@Schema(name = "Product", description = "商品规格实体")
public class Product {
    @Schema(title = "主键")
    private Long id;

    @Schema(title = "类型id", requiredMode = Schema.RequiredMode.REQUIRED)
    private Long typeId;

    @Schema(title = "规格/服务名称", requiredMode = Schema.RequiredMode.REQUIRED)
    private String specName;

    @Schema(title = "最小重量（kg）")
    private BigDecimal weightMin;

    @Schema(title = "最大重量（kg）")
    private BigDecimal weightMax;

    @Schema(title = "距离（公里）")
    private BigDecimal distance;

    @Schema(title = "金额范围最小值（分）")
    private Long amountMin;

    @Schema(title = "金额范围最大值（分）")
    private Long amountMax;

    @Schema(title = "时长")
    private Integer duration;

    @Schema(title = "时长单位（小时/天/月）")
    private String durationUnit;

    @Schema(title = "最低价格（分）", requiredMode = Schema.RequiredMode.REQUIRED)
    private Long priceMin;

    @Schema(title = "最高价格（分）", requiredMode = Schema.RequiredMode.REQUIRED)
    private Long priceMax;

    @Schema(title = "是否为万能服务的用户自定义")
    private Integer isUniversalCustom;

    @Schema(title = "是否启用（1启用 0禁用）")
    private Integer isEnabled;

    @Schema(title = "创建时间")
    private Date createTime;

    @Schema(title = "更新时间")
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
