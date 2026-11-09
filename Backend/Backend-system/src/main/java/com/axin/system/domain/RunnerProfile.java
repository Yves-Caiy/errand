package com.axin.system.domain;

import io.swagger.v3.oas.annotations.media.Schema;

import java.util.Date;

@Schema(name = "RunnerProfile", description = "接单员档案实体")
public class RunnerProfile {
    @Schema(title = "主键")
    private Long id;

    @Schema(title = "关联 wechat_user.id")
    private Long userId;

    @Schema(title = "是否接单员 1是 0否")
    private Integer isRunner;

    @Schema(title = "接单状态 1接单中 0休息中")
    private Integer onlineStatus;

    @Schema(title = "是否服务全部小区 1是 0否")
    private Integer isAllCommunity;

    @Schema(title = "最大同时进行单数")
    private Integer maxConcurrent;

    @Schema(title = "可接类型，逗号分隔")
    private String skills;

    @Schema(title = "运营人工加权")
    private Integer weightBoost;

    @Schema(title = "连续拒绝/超时次数")
    private Integer rejectStreak;

    @Schema(title = "连续未入选轮次")
    private Integer dispatchMissRounds;

    @Schema(title = "最近被派单时间")
    private Date lastAssignedTime;

    @Schema(title = "创建时间")
    private Date createTime;

    @Schema(title = "更新时间")
    private Date updateTime;

    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }
    public Long getUserId() { return userId; }
    public void setUserId(Long userId) { this.userId = userId; }
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
    public Integer getRejectStreak() { return rejectStreak; }
    public void setRejectStreak(Integer rejectStreak) { this.rejectStreak = rejectStreak; }
    public Integer getDispatchMissRounds() { return dispatchMissRounds; }
    public void setDispatchMissRounds(Integer dispatchMissRounds) { this.dispatchMissRounds = dispatchMissRounds; }
    public Date getLastAssignedTime() { return lastAssignedTime; }
    public void setLastAssignedTime(Date lastAssignedTime) { this.lastAssignedTime = lastAssignedTime; }
    public Date getCreateTime() { return createTime; }
    public void setCreateTime(Date createTime) { this.createTime = createTime; }
    public Date getUpdateTime() { return updateTime; }
    public void setUpdateTime(Date updateTime) { this.updateTime = updateTime; }
}
