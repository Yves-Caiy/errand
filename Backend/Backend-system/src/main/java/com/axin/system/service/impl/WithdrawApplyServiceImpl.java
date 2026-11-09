package com.axin.system.service.impl;

import com.axin.system.domain.WithdrawApply;
import com.axin.system.mapper.WithdrawApplyMapper;
import com.axin.system.service.IWithdrawApplyService;
import org.springframework.stereotype.Service;
import java.util.List;

@Service
public class WithdrawApplyServiceImpl implements IWithdrawApplyService {

    private final WithdrawApplyMapper mapper;

    public WithdrawApplyServiceImpl(WithdrawApplyMapper mapper) {
        this.mapper = mapper;
    }

    @Override
    public WithdrawApply selectWithdrawApplyById(Long id) {
        return mapper.selectWithdrawApplyById(id);
    }

    @Override
    public List<WithdrawApply> selectWithdrawApplyList(WithdrawApply apply) {
        return mapper.selectWithdrawApplyList(apply);
    }

    @Override
    public int insertWithdrawApply(WithdrawApply apply) {
        return mapper.insertWithdrawApply(apply);
    }

    @Override
    public int updateWithdrawApply(WithdrawApply apply) {
        return mapper.updateWithdrawApply(apply);
    }

    @Override
    public int deleteWithdrawApplyByIds(Long[] ids) {
        return mapper.deleteWithdrawApplyByIds(ids);
    }
}

