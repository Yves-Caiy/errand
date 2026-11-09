package com.axin.system.domain;

import io.swagger.v3.oas.annotations.media.Schema;

import java.util.Date;

@Schema(name = "OrderMain", description = "订单主实体")
public class OrderMain {
    @Schema(title = "主键")
    private Long id;
    @Schema(title = "订单编号（唯一）")
    private String orderNo;
    @Schema(title = "下单用户ID")
    private Long userId;
    @Schema(title = "接单员ID")
    private Long runnerId;
    @Schema(title = "小区ID")
    private Long communityId;
    @Schema(title = "详细配送地址")
    private String detailAddress;
    @Schema(title = "订单类型：1外卖 2超市 3套餐 4其他")
    private Integer orderType;
    @Schema(title = "其他服务类型")
    private String serviceType;
    @Schema(title = "起始地")
    private String startAddress;
    @Schema(title = "目的地")
    private String endAddress;
    @Schema(title = "配送地址")
    private String deliveryAddress;
    @Schema(title = "物品/需求描述")
    private String itemDesc;
    @Schema(title = "预算金额（分）")
    private Long budget;
    @Schema(title = "服务规格名称")
    private String specName;
    @Schema(title = "微信号")
    private String wechatId;
    @Schema(title = "时长类型(小时/天)")
    private String durationType;
    @Schema(title = "时长数值")
    private Integer durationValue;
    @Schema(title = "订单总金额（分）")
    private Long totalAmount;
    @Schema(title = "优惠金额（分）")
    private Long discountAmount;
    @Schema(title = "实付金额（分）")
    private Long actualAmount;
    @Schema(title = "订单佣金（分）")
    private Long commissionAmount;
    @Schema(title = "订单状态")
    private Integer orderStatus;
    @Schema(title = "支付状态")
    private Integer payStatus;
    @Schema(title = "支付方式：1微信 2线下")
    private Integer payMethod;
    @Schema(title = "支付时间")
    private Date payTime;
    @Schema(title = "完成时间")
    private Date finishTime;
    @Schema(title = "取消原因")
    private String cancelReason;
    @Schema(title = "用户备注")
    private String note;
    @Schema(title = "创建时间")
    private Date createTime;
    @Schema(title = "更新时间")
    private Date updateTime;

    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }
    public String getOrderNo() { return orderNo; }
    public void setOrderNo(String orderNo) { this.orderNo = orderNo; }
    public Long getUserId() { return userId; }
    public void setUserId(Long userId) { this.userId = userId; }
    public Long getRunnerId() { return runnerId; }
    public void setRunnerId(Long runnerId) { this.runnerId = runnerId; }
    public Long getCommunityId() { return communityId; }
    public void setCommunityId(Long communityId) { this.communityId = communityId; }
    public String getDetailAddress() { return detailAddress; }
    public void setDetailAddress(String detailAddress) { this.detailAddress = detailAddress; }
    public Integer getOrderType() { return orderType; }
    public void setOrderType(Integer orderType) { this.orderType = orderType; }
    public String getServiceType() { return serviceType; }
    public void setServiceType(String serviceType) { this.serviceType = serviceType; }
    public String getStartAddress() { return startAddress; }
    public void setStartAddress(String startAddress) { this.startAddress = startAddress; }
    public String getEndAddress() { return endAddress; }
    public void setEndAddress(String endAddress) { this.endAddress = endAddress; }
    public String getDeliveryAddress() { return deliveryAddress; }
    public void setDeliveryAddress(String deliveryAddress) { this.deliveryAddress = deliveryAddress; }
    public String getItemDesc() { return itemDesc; }
    public void setItemDesc(String itemDesc) { this.itemDesc = itemDesc; }
    public Long getBudget() { return budget; }
    public void setBudget(Long budget) { this.budget = budget; }
    public String getSpecName() { return specName; }
    public void setSpecName(String specName) { this.specName = specName; }
    public String getWechatId() { return wechatId; }
    public void setWechatId(String wechatId) { this.wechatId = wechatId; }
    public String getDurationType() { return durationType; }
    public void setDurationType(String durationType) { this.durationType = durationType; }
    public Integer getDurationValue() { return durationValue; }
    public void setDurationValue(Integer durationValue) { this.durationValue = durationValue; }
    public Long getTotalAmount() { return totalAmount; }
    public void setTotalAmount(Long totalAmount) { this.totalAmount = totalAmount; }
    public Long getDiscountAmount() { return discountAmount; }
    public void setDiscountAmount(Long discountAmount) { this.discountAmount = discountAmount; }
    public Long getActualAmount() { return actualAmount; }
    public void setActualAmount(Long actualAmount) { this.actualAmount = actualAmount; }
    public Long getCommissionAmount() { return commissionAmount; }
    public void setCommissionAmount(Long commissionAmount) { this.commissionAmount = commissionAmount; }
    public Integer getOrderStatus() { return orderStatus; }
    public void setOrderStatus(Integer orderStatus) { this.orderStatus = orderStatus; }
    public Integer getPayStatus() { return payStatus; }
    public void setPayStatus(Integer payStatus) { this.payStatus = payStatus; }
    public Integer getPayMethod() { return payMethod; }
    public void setPayMethod(Integer payMethod) { this.payMethod = payMethod; }
    public Date getPayTime() { return payTime; }
    public void setPayTime(Date payTime) { this.payTime = payTime; }
    public Date getFinishTime() { return finishTime; }
    public void setFinishTime(Date finishTime) { this.finishTime = finishTime; }
    public String getCancelReason() { return cancelReason; }
    public void setCancelReason(String cancelReason) { this.cancelReason = cancelReason; }
    public String getNote() { return note; }
    public void setNote(String note) { this.note = note; }
    public Date getCreateTime() { return createTime; }
    public void setCreateTime(Date createTime) { this.createTime = createTime; }
    public Date getUpdateTime() { return updateTime; }
    public void setUpdateTime(Date updateTime) { this.updateTime = updateTime; }
}
