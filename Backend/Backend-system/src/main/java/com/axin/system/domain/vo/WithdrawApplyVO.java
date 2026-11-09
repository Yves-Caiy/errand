package com.axin.system.domain.vo;

import io.swagger.v3.oas.annotations.media.Schema;
import com.axin.common.annotation.Excel;
import com.axin.common.annotation.Excel.ColumnType;
import com.axin.common.annotation.Excel.Type;
import java.util.Date;

@Schema(name = "WithdrawApplyVO", description = "提现申请出参")
public class WithdrawApplyVO {
    @Schema(title = "主键")
    private Long id;
    @Schema(title = "用户ID")
    private Long userId;
    @Schema(title = "用户昵称")
    @Excel(name = "用户昵称", type = Type.EXPORT)
    private String userNickname;
    @Schema(title = "联系方式（手机号）")
    @Excel(name = "联系方式", type = Type.EXPORT)
    private String userPhone;
    @Schema(title = "提现金额（分）")
    @Excel(name = "提现金额（分）", type = Type.EXPORT, cellType = ColumnType.NUMERIC)
    private Long amount;
    @Schema(title = "到账金额（分）")
    @Excel(name = "到账金额（分）", type = Type.EXPORT, cellType = ColumnType.NUMERIC)
    private Long creditedAmount;
    @Schema(title = "费率（%）")
    @Excel(name = "比率(%)", type = Type.EXPORT, cellType = ColumnType.NUMERIC)
    private Integer rate;
    @Schema(title = "状态：0申请中 1审核通过 2审核拒绝 3已打款")
    @Excel(name = "状态", readConverterExp = "0=申请中,1=审核通过,2=审核拒绝,3=已打款", type = Type.EXPORT)
    private Integer status;
    @Schema(title = "打款状态：0未打款 1已打款")
    @Excel(name = "打款状态", readConverterExp = "0=未打款,1=已打款", type = Type.EXPORT)
    private Integer payStatus;
    @Schema(title = "备注")
    @Excel(name = "备注", type = Type.EXPORT)
    private String remark;
    @Schema(title = "创建时间")
    @Excel(name = "创建时间", width = 30, dateFormat = "yyyy-MM-dd HH:mm:ss", type = Type.EXPORT)
    private Date createTime;
    @Schema(title = "更新时间")
    @Excel(name = "更新时间", width = 30, dateFormat = "yyyy-MM-dd HH:mm:ss", type = Type.EXPORT)
    private Date updateTime;
    @Schema(title = "用户openid")
    @Excel(name = "openid", type = Type.EXPORT, cellType = ColumnType.TEXT, width = 40)
    private String userOpenid;

    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }
    public Long getUserId() { return userId; }
    public void setUserId(Long userId) { this.userId = userId; }
    public String getUserNickname() { return userNickname; }
    public void setUserNickname(String userNickname) { this.userNickname = userNickname; }
    public String getUserPhone() { return userPhone; }
    public void setUserPhone(String userPhone) { this.userPhone = userPhone; }
    public Long getAmount() { return amount; }
    public void setAmount(Long amount) { this.amount = amount; }
    public Long getCreditedAmount() { return creditedAmount; }
    public void setCreditedAmount(Long creditedAmount) { this.creditedAmount = creditedAmount; }
    public Integer getRate() { return rate; }
    public void setRate(Integer rate) { this.rate = rate; }
    public Integer getStatus() { return status; }
    public void setStatus(Integer status) { this.status = status; }
    public Integer getPayStatus() { return payStatus; }
    public void setPayStatus(Integer payStatus) { this.payStatus = payStatus; }
    public String getRemark() { return remark; }
    public void setRemark(String remark) { this.remark = remark; }
    public Date getCreateTime() { return createTime; }
    public void setCreateTime(Date createTime) { this.createTime = createTime; }
    public Date getUpdateTime() { return updateTime; }
    public void setUpdateTime(Date updateTime) { this.updateTime = updateTime; }
    public String getUserOpenid() { return userOpenid; }
    public void setUserOpenid(String userOpenid) { this.userOpenid = userOpenid; }
}
