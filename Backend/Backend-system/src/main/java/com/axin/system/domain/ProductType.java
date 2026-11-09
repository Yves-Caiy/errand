package com.axin.system.domain;

import io.swagger.v3.oas.annotations.media.Schema;

import java.util.Date;

@Schema(name = "ProductType", description = "商品类型实体")
public class ProductType {
    @Schema(title = "主键")
    private Long id;

    @Schema(title = "模块名称")
    private String typeName;

    @Schema(title = "模块描述")
    private String typeDesc;

    @Schema(title = "模块图标URL")
    private String iconUrl;

    @Schema(title = "是否可增删改（1是 0否）")
    private Integer isManageable;

    @Schema(title = "是否启用（1启用 0禁用）")
    private Integer isEnabled;

    @Schema(title = "创建时间")
    private Date createTime;

    @Schema(title = "更新时间")
    private Date updateTime;

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

    public Date getCreateTime() { return createTime; }
    public void setCreateTime(Date createTime) { this.createTime = createTime; }

    public Date getUpdateTime() { return updateTime; }
    public void setUpdateTime(Date updateTime) { this.updateTime = updateTime; }
}

