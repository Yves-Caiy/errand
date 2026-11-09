package com.axin.system.domain.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.Size;

@Schema(name = "CommunityQueryDTO", description = "小区查询条件")
public class CommunityQueryDTO {
    @Schema(title = "小区名称(模糊)")
    @Size(max = 100, message = "小区名称长度不能超过100")
    private String communityName;

    @Schema(title = "地址(模糊)")
    @Size(max = 200, message = "地址长度不能超过200")
    private String address;

    @Schema(title = "是否启用（1启用 0禁用）")
    private Integer isEnabled;

    public String getCommunityName() { return communityName; }
    public void setCommunityName(String communityName) { this.communityName = communityName; }
    public String getAddress() { return address; }
    public void setAddress(String address) { this.address = address; }
    public Integer getIsEnabled() { return isEnabled; }
    public void setIsEnabled(Integer isEnabled) { this.isEnabled = isEnabled; }
}

