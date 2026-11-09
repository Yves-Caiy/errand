package com.axin.system.domain;

import io.swagger.v3.oas.annotations.media.Schema;

import java.math.BigDecimal;
import java.util.Date;

@Schema(name = "OrderDispatchLog", description = "派单记录实体")
public class OrderDispatchLog {
    @Schema(title = "主键")
    private Long id;

    @Schema(title = "关联 order_main.id")
    private Long orderId;

    @Schema(title = "被派/被推送的接单员 user_id")
    private Long runnerUserId;

    @Schema(title = "该轮得分")
    private BigDecimal score;

    @Schema(title = "第几轮")
    private Integer dispatchRound;

    @Schema(title = "1=推送抢单 2=系统指派")
    private Integer dispatchType;

    @Schema(title = "1=已推送 2=已接受 3=超时未接 4=已拒绝 5=被指派 9=他人已接")
    private Integer status;

    @Schema(title = "接单员是否已读提醒 0=未读 1=已读")
    private Integer isRead;

    @Schema(title = "创建时间")
    private Date createTime;

    @Schema(title = "更新时间")
    private Date updateTime;

    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }
    public Long getOrderId() { return orderId; }
    public void setOrderId(Long orderId) { this.orderId = orderId; }
    public Long getRunnerUserId() { return runnerUserId; }
    public void setRunnerUserId(Long runnerUserId) { this.runnerUserId = runnerUserId; }
    public BigDecimal getScore() { return score; }
    public void setScore(BigDecimal score) { this.score = score; }
    public Integer getDispatchRound() { return dispatchRound; }
    public void setDispatchRound(Integer dispatchRound) { this.dispatchRound = dispatchRound; }
    public Integer getDispatchType() { return dispatchType; }
    public void setDispatchType(Integer dispatchType) { this.dispatchType = dispatchType; }
    public Integer getStatus() { return status; }
    public void setStatus(Integer status) { this.status = status; }
    public Integer getIsRead() { return isRead; }
    public void setIsRead(Integer isRead) { this.isRead = isRead; }
    public Date getCreateTime() { return createTime; }
    public void setCreateTime(Date createTime) { this.createTime = createTime; }
    public Date getUpdateTime() { return updateTime; }
    public void setUpdateTime(Date updateTime) { this.updateTime = updateTime; }
}
