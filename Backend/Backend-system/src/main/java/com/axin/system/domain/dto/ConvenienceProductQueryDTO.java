package com.axin.system.domain.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.Size;

@Schema(name = "ConvenienceProductQueryDTO", description = "便民超市商品查询条件")
public class ConvenienceProductQueryDTO {
    @Schema(title = "商品名称(模糊)")
    @Size(max = 100, message = "商品名称长度不能超过100")
    private String productName;

    @Schema(title = "超市商品类型")
    @Size(max = 50, message = "商品类型长度不能超过50")
    private String productType;

    @Schema(title = "是否启用（1启用 0禁用）")
    private Integer isEnabled;

    public String getProductName() { return productName; }
    public void setProductName(String productName) { this.productName = productName; }
    public String getProductType() { return productType; }
    public void setProductType(String productType) { this.productType = productType; }
    public Integer getIsEnabled() { return isEnabled; }
    public void setIsEnabled(Integer isEnabled) { this.isEnabled = isEnabled; }
}

