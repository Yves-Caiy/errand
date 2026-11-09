package com.axin.system.domain;

import io.swagger.v3.oas.annotations.media.Schema;

import java.util.Date;

@Schema(name = "TakeawayStore", description = "外卖店铺实体")
public class TakeawayStore {
    @Schema(title = "主键")
    private Long id;

    @Schema(title = "关联小区id", requiredMode = Schema.RequiredMode.REQUIRED)
    private Long communityId;

    @Schema(title = "店铺名称", requiredMode = Schema.RequiredMode.REQUIRED)
    private String storeName;

    @Schema(title = "店铺头像URL")
    private String avatarUrl;

    @Schema(title = "店铺地址")
    private String address;

    @Schema(title = "联系方式")
    private String contact;

    @Schema(title = "是否启用（1启用 0禁用）")
    private Integer isEnabled;

    @Schema(title = "创建时间")
    private Date createTime;

    @Schema(title = "更新时间")
    private Date updateTime;

    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }
    public Long getCommunityId() { return communityId; }
    public void setCommunityId(Long communityId) { this.communityId = communityId; }
    public String getStoreName() { return storeName; }
    public void setStoreName(String storeName) { this.storeName = storeName; }
    public String getAvatarUrl() { return avatarUrl; }
    public void setAvatarUrl(String avatarUrl) { this.avatarUrl = avatarUrl; }
    public String getAddress() { return address; }
    public void setAddress(String address) { this.address = address; }
    public String getContact() { return contact; }
    public void setContact(String contact) { this.contact = contact; }
    public Integer getIsEnabled() { return isEnabled; }
    public void setIsEnabled(Integer isEnabled) { this.isEnabled = isEnabled; }
    public Date getCreateTime() { return createTime; }
    public void setCreateTime(Date createTime) { this.createTime = createTime; }
    public Date getUpdateTime() { return updateTime; }
    public void setUpdateTime(Date updateTime) { this.updateTime = updateTime; }
}

