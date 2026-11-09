package com.axin.errand.domain.vo;

import com.fasterxml.jackson.annotation.JsonFormat;
import io.swagger.v3.oas.annotations.media.Schema;
import java.util.Date;

@Schema(name = "MyServicePackageVO", description = "我的服务套餐")
public class MyServicePackageVO {
    @Schema(title = "订单ID")
    private Long orderId;
    @Schema(title = "套餐ID")
    private Long packageId;
    @Schema(title = "套餐名称")
    private String packageName;
    @Schema(title = "包含服务")
    private String includeService;
    @Schema(title = "购买时间")
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private Date buyTime;
    @Schema(title = "过期时间")
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private Date expireTime;
    @Schema(title = "状态：1生效中 0已过期")
    private Integer status;

    public Long getOrderId() { return orderId; }
    public void setOrderId(Long orderId) { this.orderId = orderId; }
    public Long getPackageId() { return packageId; }
    public void setPackageId(Long packageId) { this.packageId = packageId; }
    public String getPackageName() { return packageName; }
    public void setPackageName(String packageName) { this.packageName = packageName; }
    public String getIncludeService() { return includeService; }
    public void setIncludeService(String includeService) { this.includeService = includeService; }
    public Date getBuyTime() { return buyTime; }
    public void setBuyTime(Date buyTime) { this.buyTime = buyTime; }
    public Date getExpireTime() { return expireTime; }
    public void setExpireTime(Date expireTime) { this.expireTime = expireTime; }
    public Integer getStatus() { return status; }
    public void setStatus(Integer status) { this.status = status; }
}
