package com.axin.system.service.impl;

import com.axin.system.domain.TakeawayStore;
import com.axin.system.mapper.TakeawayStoreMapper;
import com.axin.system.service.ITakeawayStoreService;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class TakeawayStoreServiceImpl implements ITakeawayStoreService {

    private final TakeawayStoreMapper mapper;

    public TakeawayStoreServiceImpl(TakeawayStoreMapper mapper) {
        this.mapper = mapper;
    }

    @Override
    public TakeawayStore selectTakeawayStoreById(Long id) {
        return mapper.selectTakeawayStoreById(id);
    }

    @Override
    public List<TakeawayStore> selectTakeawayStoreList(TakeawayStore store) {
        return mapper.selectTakeawayStoreList(store);
    }

    @Override
    public int insertTakeawayStore(TakeawayStore store) {
        return mapper.insertTakeawayStore(store);
    }

    @Override
    public int updateTakeawayStore(TakeawayStore store) {
        return mapper.updateTakeawayStore(store);
    }

    @Override
    public int deleteTakeawayStoreByIds(Long[] ids) {
        return mapper.deleteTakeawayStoreByIds(ids);
    }

    @Override
    public boolean isStoreNameExists(String storeName, Long excludeId) {
        TakeawayStore existed = mapper.selectTakeawayStoreByName(storeName);
        if (existed == null) {
            return false;
        }
        if (excludeId == null) {
            return true;
        }
        return !excludeId.equals(existed.getId());
    }
}

