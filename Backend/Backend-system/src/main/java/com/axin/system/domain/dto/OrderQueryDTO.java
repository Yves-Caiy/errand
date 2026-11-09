package com.axin.system.domain.dto;

import io.swagger.v3.oas.annotations.media.Schema;

@Schema(name = "OrderQueryDTO", description = "订单查询条件")
public class OrderQueryDTO {
    @Schema(title = "订单编号")
    private String orderNo;
    @Schema(title = "下单用户ID")
    private Long userId;
    @Schema(title = "接单员ID")
    private Long runnerId;
    @Schema(title = "小区ID")
    private Long communityId;
    @Schema(title = "订单类型")
    private Integer orderType;
    @Schema(title = "订单状态")
    private Integer orderStatus;
    @Schema(title = "支付状态")
    private Integer payStatus;
    @Schema(title = "其他服务类型")
    private String serviceType;

    public String getOrderNo() { return orderNo; }
    public void setOrderNo(String orderNo) { this.orderNo = orderNo; }
    public Long getUserId() { return userId; }
    public void setUserId(Long userId) { this.userId = userId; }
    public Long getRunnerId() { return runnerId; }
    public void setRunnerId(Long runnerId) { this.runnerId = runnerId; }
    public Long getCommunityId() { return communityId; }
    public void setCommunityId(Long communityId) { this.communityId = communityId; }
    public Integer getOrderType() { return orderType; }
    public void setOrderType(Integer orderType) { this.orderType = orderType; }
    public Integer getOrderStatus() { return orderStatus; }
    public void setOrderStatus(Integer orderStatus) { this.orderStatus = orderStatus; }
    public Integer getPayStatus() { return payStatus; }
    public void setPayStatus(Integer payStatus) { this.payStatus = payStatus; }
    public String getServiceType() { return serviceType; }
    public void setServiceType(String serviceType) { this.serviceType = serviceType; }
}
