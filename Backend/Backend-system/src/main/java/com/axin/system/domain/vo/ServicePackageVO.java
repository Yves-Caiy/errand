package com.axin.system.domain.vo;

import io.swagger.v3.oas.annotations.media.Schema;

import java.util.Date;

@Schema(name = "ServicePackageVO", description = "服务套餐出参")
public class ServicePackageVO {
    @Schema(title = "主键")
    private Long id;
    @Schema(title = "类型id")
    private Long typeId;
    @Schema(title = "套餐名称")
    private String packageName;
    @Schema(title = "套餐价格（分）")
    private Long price;
    @Schema(title = "首月优惠价（分）")
    private Long firstMonthPrice;
    @Schema(title = "时长类型（1包月 2包年）")
    private Integer durationType;
    @Schema(title = "套餐时长（天）")
    private Integer durationDays;
    @Schema(title = "包含服务描述")
    private String includeService;
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
    public Date getCreateTime() { return createTime; }
    public void setCreateTime(Date createTime) { this.createTime = createTime; }
    public Date getUpdateTime() { return updateTime; }
    public void setUpdateTime(Date updateTime) { this.updateTime = updateTime; }
}

