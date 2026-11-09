package com.axin.system.service;

import com.axin.system.domain.WithdrawApply;
import java.util.List;

public interface IWithdrawApplyService {
    WithdrawApply selectWithdrawApplyById(Long id);
    List<WithdrawApply> selectWithdrawApplyList(WithdrawApply apply);
    int insertWithdrawApply(WithdrawApply apply);
    int updateWithdrawApply(WithdrawApply apply);
    int deleteWithdrawApplyByIds(Long[] ids);
}

