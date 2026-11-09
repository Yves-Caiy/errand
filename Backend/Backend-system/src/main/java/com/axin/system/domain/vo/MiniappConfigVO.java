package com.axin.system.domain.vo;

import io.swagger.v3.oas.annotations.media.Schema;

import java.util.Date;

@Schema(name = "MiniappConfigVO", description = "小程序配置出参")
public class MiniappConfigVO {
    @Schema(title = "主键")
    private Long id;
    @Schema(title = "提现比率（百分比0-100）")
    private Integer withdrawRate;
    @Schema(title = "首页轮播图JSON数组")
    private String carouselImages;
    @Schema(title = "隐私政策内容")
    private String privacyPolicy;
    @Schema(title = "公告通知内容")
    private String noticeContent;
    @Schema(title = "是否启用（1启用 0禁用）")
    private Integer isEnabled;
    @Schema(title = "测试模式（1开启 0关闭）")
    private Integer testMode;
    @Schema(title = "智能派单开关 1开 0关")
    private Integer dispatchEnabled;
    @Schema(title = "启用派单的订单类型，逗号分隔")
    private String dispatchOrderTypes;
    @Schema(title = "每轮推送人数")
    private Integer dispatchPushCount;
    @Schema(title = "每轮等待秒数")
    private Integer dispatchTimeoutSec;
    @Schema(title = "最大派单轮次")
    private Integer dispatchMaxRound;
    @Schema(title = "派单权重与阈值JSON")
    private String dispatchWeightJson;
    @Schema(title = "创建时间")
    private Date createTime;
    @Schema(title = "更新时间")
    private Date updateTime;

    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }
    public Integer getWithdrawRate() { return withdrawRate; }
    public void setWithdrawRate(Integer withdrawRate) { this.withdrawRate = withdrawRate; }
    public String getCarouselImages() { return carouselImages; }
    public void setCarouselImages(String carouselImages) { this.carouselImages = carouselImages; }
    public String getPrivacyPolicy() { return privacyPolicy; }
    public void setPrivacyPolicy(String privacyPolicy) { this.privacyPolicy = privacyPolicy; }
    public String getNoticeContent() { return noticeContent; }
    public void setNoticeContent(String noticeContent) { this.noticeContent = noticeContent; }
    public Integer getIsEnabled() { return isEnabled; }
    public void setIsEnabled(Integer isEnabled) { this.isEnabled = isEnabled; }
    public Integer getTestMode() { return testMode; }
    public void setTestMode(Integer testMode) { this.testMode = testMode; }
    public Integer getDispatchEnabled() { return dispatchEnabled; }
    public void setDispatchEnabled(Integer dispatchEnabled) { this.dispatchEnabled = dispatchEnabled; }
    public String getDispatchOrderTypes() { return dispatchOrderTypes; }
    public void setDispatchOrderTypes(String dispatchOrderTypes) { this.dispatchOrderTypes = dispatchOrderTypes; }
    public Integer getDispatchPushCount() { return dispatchPushCount; }
    public void setDispatchPushCount(Integer dispatchPushCount) { this.dispatchPushCount = dispatchPushCount; }
    public Integer getDispatchTimeoutSec() { return dispatchTimeoutSec; }
    public void setDispatchTimeoutSec(Integer dispatchTimeoutSec) { this.dispatchTimeoutSec = dispatchTimeoutSec; }
    public Integer getDispatchMaxRound() { return dispatchMaxRound; }
    public void setDispatchMaxRound(Integer dispatchMaxRound) { this.dispatchMaxRound = dispatchMaxRound; }
    public String getDispatchWeightJson() { return dispatchWeightJson; }
    public void setDispatchWeightJson(String dispatchWeightJson) { this.dispatchWeightJson = dispatchWeightJson; }
    public Date getCreateTime() { return createTime; }
    public void setCreateTime(Date createTime) { this.createTime = createTime; }
    public Date getUpdateTime() { return updateTime; }
    public void setUpdateTime(Date updateTime) { this.updateTime = updateTime; }
}

