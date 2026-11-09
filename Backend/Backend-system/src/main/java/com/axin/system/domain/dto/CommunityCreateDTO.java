package com.axin.system.domain.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;

@Schema(name = "CommunityCreateDTO", description = "小区创建入参")
public class CommunityCreateDTO {
    @Schema(title = "小区名称", requiredMode = Schema.RequiredMode.REQUIRED)
    @NotBlank(message = "小区名称不能为空")
    @Size(max = 100, message = "小区名称长度不能超过100")
    private String communityName;

    @Schema(title = "小区详细地址")
    @Size(max = 200, message = "地址长度不能超过200")
    private String address;

    @Schema(title = "小区联系人")
    @Size(max = 50, message = "联系人长度不能超过50")
    private String contactPerson;

    @Schema(title = "联系电话")
    @Size(max = 11, message = "联系电话最长11字符")
    @Pattern(regexp = "^[0-9-+()]*$", message = "联系电话格式不正确")
    private String contactPhone;

    @Schema(title = "是否启用（1启用 0禁用）")
    private Integer isEnabled;

    public String getCommunityName() { return communityName; }
    public void setCommunityName(String communityName) { this.communityName = communityName; }
    public String getAddress() { return address; }
    public void setAddress(String address) { this.address = address; }
    public String getContactPerson() { return contactPerson; }
    public void setContactPerson(String contactPerson) { this.contactPerson = contactPerson; }
    public String getContactPhone() { return contactPhone; }
    public void setContactPhone(String contactPhone) { this.contactPhone = contactPhone; }
    public Integer getIsEnabled() { return isEnabled; }
    public void setIsEnabled(Integer isEnabled) { this.isEnabled = isEnabled; }
}

