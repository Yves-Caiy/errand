package com.axin.system.domain.vo;

import io.swagger.v3.oas.annotations.media.Schema;

import java.util.Date;

@Schema(name = "RunnerCandidateVO", description = "派单候选接单员（含统计与覆盖信息）")
public class RunnerCandidateVO {
    @Schema(title = "接单员 user_id")
    private Long userId;

    @Schema(title = "昵称")
    private String nickname;

    @Schema(title = "最大同时进行单数")
    private Integer maxConcurrent;

    @Schema(title = "运营人工加权")
    private Integer weightBoost;

    @Schema(title = "是否服务全部小区")
    private Integer isAllCommunity;

    @Schema(title = "是否覆盖收货小区 1是 0否")
    private Integer coverReceive;

    @Schema(title = "是否覆盖取餐店铺小区 1是 0否")
    private Integer coverStore;

    @Schema(title = "在途单数（全部类型）")
    private Integer inTransit;

    @Schema(title = "近30天已完成外卖单数")
    private Integer doneCnt;

    @Schema(title = "近30天已结外卖单数（完成+取消）")
    private Integer handledCnt;

    @Schema(title = "近30天外卖平均完成时长（分钟）")
    private Double avgMinutes;

    @Schema(title = "最近完成外卖时间")
    private Date lastFinishTime;

    @Schema(title = "连续拒绝/超时次数")
    private Integer rejectStreak;

    @Schema(title = "连续未入选轮次")
    private Integer dispatchMissRounds;

    public Long getUserId() { return userId; }
    public void setUserId(Long userId) { this.userId = userId; }
    public String getNickname() { return nickname; }
    public void setNickname(String nickname) { this.nickname = nickname; }
    public Integer getMaxConcurrent() { return maxConcurrent; }
    public void setMaxConcurrent(Integer maxConcurrent) { this.maxConcurrent = maxConcurrent; }
    public Integer getWeightBoost() { return weightBoost; }
    public void setWeightBoost(Integer weightBoost) { this.weightBoost = weightBoost; }
    public Integer getIsAllCommunity() { return isAllCommunity; }
    public void setIsAllCommunity(Integer isAllCommunity) { this.isAllCommunity = isAllCommunity; }
    public Integer getCoverReceive() { return coverReceive; }
    public void setCoverReceive(Integer coverReceive) { this.coverReceive = coverReceive; }
    public Integer getCoverStore() { return coverStore; }
    public void setCoverStore(Integer coverStore) { this.coverStore = coverStore; }
    public Integer getInTransit() { return inTransit; }
    public void setInTransit(Integer inTransit) { this.inTransit = inTransit; }
    public Integer getDoneCnt() { return doneCnt; }
    public void setDoneCnt(Integer doneCnt) { this.doneCnt = doneCnt; }
    public Integer getHandledCnt() { return handledCnt; }
    public void setHandledCnt(Integer handledCnt) { this.handledCnt = handledCnt; }
    public Double getAvgMinutes() { return avgMinutes; }
    public void setAvgMinutes(Double avgMinutes) { this.avgMinutes = avgMinutes; }
    public Date getLastFinishTime() { return lastFinishTime; }
    public void setLastFinishTime(Date lastFinishTime) { this.lastFinishTime = lastFinishTime; }
    public Integer getRejectStreak() { return rejectStreak; }
    public void setRejectStreak(Integer rejectStreak) { this.rejectStreak = rejectStreak; }
    public Integer getDispatchMissRounds() { return dispatchMissRounds; }
    public void setDispatchMissRounds(Integer dispatchMissRounds) { this.dispatchMissRounds = dispatchMissRounds; }
}
