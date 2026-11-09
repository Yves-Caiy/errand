package com.axin.system.domain.dto;

import io.swagger.v3.oas.annotations.media.Schema;

@Schema(name = "OrderItemCreateDTO")
public class OrderItemCreateDTO {
    @Schema(title = "订单项类型：1外卖 2超市 3套餐")
    private Integer itemType;
    @Schema(title = "服务套餐ID")
    private Long packageId;
    @Schema(title = "商品ID")
    private Long productId;
    @Schema(title = "商品/套餐名称快照")
    private String productName;
    @Schema(title = "数量")
    private Integer quantity;
    @Schema(title = "单价（分）")
    private Long unitPrice;
    @Schema(title = "小计金额（分）")
    private Long subtotal;
    @Schema(title = "店铺ID")
    private Long storeId;

    public Integer getItemType() { return itemType; }
    public void setItemType(Integer itemType) { this.itemType = itemType; }
    public Long getPackageId() { return packageId; }
    public void setPackageId(Long packageId) { this.packageId = packageId; }
    public Long getProductId() { return productId; }
    public void setProductId(Long productId) { this.productId = productId; }
    public String getProductName() { return productName; }
    public void setProductName(String productName) { this.productName = productName; }
    public Integer getQuantity() { return quantity; }
    public void setQuantity(Integer quantity) { this.quantity = quantity; }
    public Long getUnitPrice() { return unitPrice; }
    public void setUnitPrice(Long unitPrice) { this.unitPrice = unitPrice; }
    public Long getSubtotal() { return subtotal; }
    public void setSubtotal(Long subtotal) { this.subtotal = subtotal; }
    public Long getStoreId() { return storeId; }
    public void setStoreId(Long storeId) { this.storeId = storeId; }
}
