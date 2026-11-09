package com.axin.system.domain.vo;

import io.swagger.v3.oas.annotations.media.Schema;

import java.util.Date;

@Schema(name = "RunnerProfileListVO", description = "接单员管理列表行")
public class RunnerProfileListVO {
    @Schema(title = "主键")
    private Long id;
    @Schema(title = "接单员 user_id")
    private Long userId;
    @Schema(title = "昵称")
    private String nickname;
    @Schema(title = "头像")
    private String avatarUrl;
    @Schema(title = "手机号")
    private String phone;
    @Schema(title = "是否接单员 1是 0否")
    private Integer isRunner;
    @Schema(title = "接单状态 1接单中 0休息中")
    private Integer onlineStatus;
    @Schema(title = "是否服务全部小区")
    private Integer isAllCommunity;
    @Schema(title = "最大同时进行单数")
    private Integer maxConcurrent;
    @Schema(title = "可接类型")
    private String skills;
    @Schema(title = "运营人工加权")
    private Integer weightBoost;
    @Schema(title = "服务小区名称，逗号拼接")
    private String communityNames;
    @Schema(title = "服务小区ID，逗号拼接")
    private String communityIds;
    @Schema(title = "创建时间")
    private Date createTime;
    @Schema(title = "更新时间")
    private Date updateTime;

    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }
    public Long getUserId() { return userId; }
    public void setUserId(Long userId) { this.userId = userId; }
    public String getNickname() { return nickname; }
    public void setNickname(String nickname) { this.nickname = nickname; }
    public String getAvatarUrl() { return avatarUrl; }
    public void setAvatarUrl(String avatarUrl) { this.avatarUrl = avatarUrl; }
    public String getPhone() { return phone; }
    public void setPhone(String phone) { this.phone = phone; }
    public Integer getIsRunner() { return isRunner; }
    public void setIsRunner(Integer isRunner) { this.isRunner = isRunner; }
    public Integer getOnlineStatus() { return onlineStatus; }
    public void setOnlineStatus(Integer onlineStatus) { this.onlineStatus = onlineStatus; }
    public Integer getIsAllCommunity() { return isAllCommunity; }
    public void setIsAllCommunity(Integer isAllCommunity) { this.isAllCommunity = isAllCommunity; }
    public Integer getMaxConcurrent() { return maxConcurrent; }
    public void setMaxConcurrent(Integer maxConcurrent) { this.maxConcurrent = maxConcurrent; }
    public String getSkills() { return skills; }
    public void setSkills(String skills) { this.skills = skills; }
    public Integer getWeightBoost() { return weightBoost; }
    public void setWeightBoost(Integer weightBoost) { this.weightBoost = weightBoost; }
    public String getCommunityNames() { return communityNames; }
    public void setCommunityNames(String communityNames) { this.communityNames = communityNames; }
    public String getCommunityIds() { return communityIds; }
    public void setCommunityIds(String communityIds) { this.communityIds = communityIds; }
    public Date getCreateTime() { return createTime; }
    public void setCreateTime(Date createTime) { this.createTime = createTime; }
    public Date getUpdateTime() { return updateTime; }
    public void setUpdateTime(Date updateTime) { this.updateTime = updateTime; }
}
