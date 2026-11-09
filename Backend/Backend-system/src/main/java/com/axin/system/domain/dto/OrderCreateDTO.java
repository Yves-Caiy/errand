package com.axin.system.domain.dto;

import io.swagger.v3.oas.annotations.media.Schema;

import java.util.List;

@Schema(name = "OrderCreateDTO")
public class OrderCreateDTO {
    @Schema(title = "订单类型：1外卖 2超市 3套餐 4其他")
    private Integer orderType;
    @Schema(title = "小区ID")
    private Long communityId;
    @Schema(title = "详细配送地址")
    private String detailAddress;
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
    @Schema(title = "时长类型")
    private String durationType;
    @Schema(title = "时长数值")
    private Integer durationValue;
    @Schema(title = "优惠金额（分）")
    private Long discountAmount;
    @Schema(title = "订单总金额（分）")
    private Long totalAmount;
    @Schema(title = "实付金额（分）")
    private Long actualAmount;
    @Schema(title = "佣金金额（分）")
    private Long commissionAmount;
    @Schema(title = "支付方式：1微信 2线下")
    private Integer payMethod;
    @Schema(title = "订单项列表，仅订单类型1/2/3需要")
    private List<OrderItemCreateDTO> items;
    @Schema(title = "备注")
    private String note;

    public Integer getOrderType() { return orderType; }
    public void setOrderType(Integer orderType) { this.orderType = orderType; }
    public Long getCommunityId() { return communityId; }
    public void setCommunityId(Long communityId) { this.communityId = communityId; }
    public String getDetailAddress() { return detailAddress; }
    public void setDetailAddress(String detailAddress) { this.detailAddress = detailAddress; }
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
    public Long getDiscountAmount() { return discountAmount; }
    public void setDiscountAmount(Long discountAmount) { this.discountAmount = discountAmount; }
    public Long getTotalAmount() { return totalAmount; }
    public void setTotalAmount(Long totalAmount) { this.totalAmount = totalAmount; }
    public Long getActualAmount() { return actualAmount; }
    public void setActualAmount(Long actualAmount) { this.actualAmount = actualAmount; }
    public Long getCommissionAmount() { return commissionAmount; }
    public void setCommissionAmount(Long commissionAmount) { this.commissionAmount = commissionAmount; }
    public Integer getPayMethod() { return payMethod; }
    public void setPayMethod(Integer payMethod) { this.payMethod = payMethod; }
    public List<OrderItemCreateDTO> getItems() { return items; }
    public void setItems(List<OrderItemCreateDTO> items) { this.items = items; }
    public String getNote() { return note; }
    public void setNote(String note) { this.note = note; }
}
