package com.axin.system.domain.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;


@Schema(name = "ConvenienceProductCreateDTO", description = "便民超市商品创建入参")
public class ConvenienceProductCreateDTO {
    @Schema(title = "商品名称", requiredMode = Schema.RequiredMode.REQUIRED)
    @NotBlank(message = "商品名称不能为空")
    @Size(max = 100, message = "商品名称长度不能超过100")
    private String productName;

    @Schema(title = "商品价格（分）", requiredMode = Schema.RequiredMode.REQUIRED)
    @NotNull(message = "商品价格不能为空")
    private Long price;

    @Schema(title = "商品图片URL")
    @Size(max = 255, message = "图片URL长度不能超过255")
    private String imageUrl;

    @Schema(title = "超市商品类型")
    @Size(max = 50, message = "商品类型长度不能超过50")
    private String productType;

    @Schema(title = "是否启用（1启用 0禁用）")
    private Integer isEnabled;

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
}
