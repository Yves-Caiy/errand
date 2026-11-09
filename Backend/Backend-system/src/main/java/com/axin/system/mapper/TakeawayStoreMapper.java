package com.axin.system.mapper;

import com.axin.system.domain.TakeawayStore;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

@Mapper
public interface TakeawayStoreMapper {
    TakeawayStore selectTakeawayStoreById(@Param("id") Long id);

    TakeawayStore selectTakeawayStoreByName(@Param("storeName") String storeName);

    List<TakeawayStore> selectTakeawayStoreList(TakeawayStore store);

    int insertTakeawayStore(TakeawayStore store);

    int updateTakeawayStore(TakeawayStore store);

    int deleteTakeawayStoreByIds(@Param("ids") Long[] ids);
}

