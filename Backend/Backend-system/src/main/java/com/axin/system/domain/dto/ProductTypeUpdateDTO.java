package com.axin.system.domain.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;

@Schema(name = "ProductTypeUpdateDTO", description = "商品类型更新入参")
public class ProductTypeUpdateDTO {
    @Schema(title = "主键ID", requiredMode = Schema.RequiredMode.REQUIRED)
    @NotNull(message = "ID不能为空")
    private Long id;

    @Schema(title = "模块名称")
    @Size(max = 50, message = "模块名称长度不能超过50")
    private String typeName;

    @Schema(title = "模块描述")
    @Size(max = 200, message = "模块描述长度不能超过200")
    private String typeDesc;

    @Schema(title = "模块图标URL")
    @Size(max = 255, message = "图标URL长度不能超过255")
    private String iconUrl;

    @Schema(title = "是否可增删改（1是 0否）")
    private Integer isManageable;

    @Schema(title = "是否启用（1启用 0禁用）")
    private Integer isEnabled;

    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }
    public String getTypeName() { return typeName; }
    public void setTypeName(String typeName) { this.typeName = typeName; }
    public String getTypeDesc() { return typeDesc; }
    public void setTypeDesc(String typeDesc) { this.typeDesc = typeDesc; }
    public String getIconUrl() { return iconUrl; }
    public void setIconUrl(String iconUrl) { this.iconUrl = iconUrl; }
    public Integer getIsManageable() { return isManageable; }
    public void setIsManageable(Integer isManageable) { this.isManageable = isManageable; }
    public Integer getIsEnabled() { return isEnabled; }
    public void setIsEnabled(Integer isEnabled) { this.isEnabled = isEnabled; }
}

