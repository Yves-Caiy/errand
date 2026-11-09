package com.axin.system.mapper;

import com.axin.system.domain.WithdrawApply;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import java.util.List;

@Mapper
public interface WithdrawApplyMapper {
    WithdrawApply selectWithdrawApplyById(@Param("id") Long id);
    List<WithdrawApply> selectWithdrawApplyList(WithdrawApply apply);
    int insertWithdrawApply(WithdrawApply apply);
    int updateWithdrawApply(WithdrawApply apply);
    int deleteWithdrawApplyByIds(@Param("ids") Long[] ids);
}

