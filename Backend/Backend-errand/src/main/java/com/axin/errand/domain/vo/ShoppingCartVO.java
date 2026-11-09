package com.axin.errand.domain.vo;

import io.swagger.v3.oas.annotations.media.Schema;

@Schema(name = "ShoppingCartVO", description = "购物车VO")
public class ShoppingCartVO {
    @Schema(title = "购物车ID")
    private Long id;
    @Schema(title = "商品类型")
    private Integer productType;
    @Schema(title = "商品ID")
    private Long productId;
    @Schema(title = "店铺ID")
    private Long storeId;
    @Schema(title = "数量")
    private Integer quantity;
    @Schema(title = "是否选中")
    private Integer isSelected;

    @Schema(title = "商品名称")
    private String productName;
    @Schema(title = "商品价格（分）")
    private Long price;
    @Schema(title = "商品图片")
    private String imageUrl;
    @Schema(title = "店铺名称")
    private String storeName;

    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }
    public Integer getProductType() { return productType; }
    public void setProductType(Integer productType) { this.productType = productType; }
    public Long getProductId() { return productId; }
    public void setProductId(Long productId) { this.productId = productId; }
    public Long getStoreId() { return storeId; }
    public void setStoreId(Long storeId) { this.storeId = storeId; }
    public Integer getQuantity() { return quantity; }
    public void setQuantity(Integer quantity) { this.quantity = quantity; }
    public Integer getIsSelected() { return isSelected; }
    public void setIsSelected(Integer isSelected) { this.isSelected = isSelected; }
    public String getProductName() { return productName; }
    public void setProductName(String productName) { this.productName = productName; }
    public Long getPrice() { return price; }
    public void setPrice(Long price) { this.price = price; }
    public String getImageUrl() { return imageUrl; }
    public void setImageUrl(String imageUrl) { this.imageUrl = imageUrl; }
    public String getStoreName() { return storeName; }
    public void setStoreName(String storeName) { this.storeName = storeName; }
}
