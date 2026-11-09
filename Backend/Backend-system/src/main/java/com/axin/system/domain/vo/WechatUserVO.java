package com.axin.system.domain.vo;

import io.swagger.v3.oas.annotations.media.Schema;

import java.util.Date;

@Schema(name = "WechatUserVO", description = "微信用户出参")
public class WechatUserVO {
    @Schema(title = "主键")
    private Long id;
    @Schema(title = "openid")
    private String openid;
    @Schema(title = "用户昵称")
    private String nickname;
    @Schema(title = "用户头像URL")
    private String avatarUrl;
    @Schema(title = "用户手机号")
    private String phone;
    @Schema(title = "关联小区名称")
    private String communityName;
    @Schema(title = "详细地址（小区和门牌号等）")
    private String detailAddress;
    @Schema(title = "累计佣金（分）")
    private Long totalCommission;
    @Schema(title = "待结算佣金（分）")
    private Long pendingCommission;
    @Schema(title = "已结算佣金（分）")
    private Long settledCommission;
    @Schema(title = "是否启用（1启用 0禁用）")
    private Integer isEnabled;
    @Schema(title = "是否为管理员（1是 0否）")
    private Integer isAdmin;
    @Schema(title = "创建时间")
    private Date createTime;
    @Schema(title = "更新时间")
    private Date updateTime;

    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }
    public String getOpenid() { return openid; }
    public void setOpenid(String openid) { this.openid = openid; }
    public String getNickname() { return nickname; }
    public void setNickname(String nickname) { this.nickname = nickname; }
    public String getAvatarUrl() { return avatarUrl; }
    public void setAvatarUrl(String avatarUrl) { this.avatarUrl = avatarUrl; }
    public String getPhone() { return phone; }
    public void setPhone(String phone) { this.phone = phone; }
    public String getCommunityName() { return communityName; }
    public void setCommunityName(String communityName) { this.communityName = communityName; }
    public String getDetailAddress() { return detailAddress; }
    public void setDetailAddress(String detailAddress) { this.detailAddress = detailAddress; }
    public Long getTotalCommission() { return totalCommission; }
    public void setTotalCommission(Long totalCommission) { this.totalCommission = totalCommission; }
    public Long getPendingCommission() { return pendingCommission; }
    public void setPendingCommission(Long pendingCommission) { this.pendingCommission = pendingCommission; }
    public Long getSettledCommission() { return settledCommission; }
    public void setSettledCommission(Long settledCommission) { this.settledCommission = settledCommission; }
    public Integer getIsEnabled() { return isEnabled; }
    public void setIsEnabled(Integer isEnabled) { this.isEnabled = isEnabled; }
    public Integer getIsAdmin() { return isAdmin; }
    public void setIsAdmin(Integer isAdmin) { this.isAdmin = isAdmin; }
    public Date getCreateTime() { return createTime; }
    public void setCreateTime(Date createTime) { this.createTime = createTime; }
    public Date getUpdateTime() { return updateTime; }
    public void setUpdateTime(Date updateTime) { this.updateTime = updateTime; }
}
