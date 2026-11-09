package com.axin.system.domain.vo;

import io.swagger.v3.oas.annotations.media.Schema;

import java.util.Date;

@Schema(name = "ConvenienceProductVO", description = "便民超市商品出参")
public class ConvenienceProductVO {
    @Schema(title = "主键")
    private Long id;
    @Schema(title = "商品名称")
    private String productName;
    @Schema(title = "商品价格（分）")
    private Long price;
    @Schema(title = "商品图片URL")
    private String imageUrl;
    @Schema(title = "超市商品类型")
    private String productType;
    @Schema(title = "是否启用（1启用 0禁用）")
    private Integer isEnabled;
    @Schema(title = "创建时间")
    private Date createTime;
    @Schema(title = "更新时间")
    private Date updateTime;

    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }
    public String getProductName() { return productName; }
    public void setProductName(String productName) { this.productName = productName; }
    public Long getPrice() { return price; }
    public void setPrice(Long price) { this.price = price; }
    public String getImageUrl() { return imageUrl; }
    public void setImageUrl(String imageUrl) { this.imageUrl = imageUrl; }
    public String getProductType() { return productType; }
    public void setProductType(String productType) { this.productType = productType; }
    public Integer getIsEnabled() { return isEnabled; }
    public void setIsEnabled(Integer isEnabled) { this.isEnabled = isEnabled; }
    public Date getCreateTime() { return createTime; }
    public void setCreateTime(Date createTime) { this.createTime = createTime; }
    public Date getUpdateTime() { return updateTime; }
    public void setUpdateTime(Date updateTime) { this.updateTime = updateTime; }
}
