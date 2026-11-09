package com.axin.system.service;

import com.axin.system.domain.TakeawayStore;

import java.util.List;

public interface ITakeawayStoreService {
    TakeawayStore selectTakeawayStoreById(Long id);

    List<TakeawayStore> selectTakeawayStoreList(TakeawayStore store);

    int insertTakeawayStore(TakeawayStore store);

    int updateTakeawayStore(TakeawayStore store);

    int deleteTakeawayStoreByIds(Long[] ids);

    boolean isStoreNameExists(String storeName, Long excludeId);
}

