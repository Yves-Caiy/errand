package com.axin.system.service.impl;

import com.axin.system.domain.CommunityProductType;
import com.axin.system.mapper.CommunityProductTypeMapper;
import com.axin.system.service.ICommunityProductTypeService;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class CommunityProductTypeServiceImpl implements ICommunityProductTypeService {

    private final CommunityProductTypeMapper mapper;

    public CommunityProductTypeServiceImpl(CommunityProductTypeMapper mapper) {
        this.mapper = mapper;
    }

    @Override
    public CommunityProductType selectCommunityProductTypeById(Long id) {
        return mapper.selectCommunityProductTypeById(id);
    }

    @Override
    public List<CommunityProductType> selectCommunityProductTypeList(CommunityProductType cpt) {
        return mapper.selectCommunityProductTypeList(cpt);
    }

    @Override
    public int insertCommunityProductType(CommunityProductType cpt) {
        return mapper.insertCommunityProductType(cpt);
    }

    @Override
    public int updateCommunityProductType(CommunityProductType cpt) {
        return mapper.updateCommunityProductType(cpt);
    }

    @Override
    public int deleteCommunityProductTypeByIds(Long[] ids) {
        return mapper.deleteCommunityProductTypeByIds(ids);
    }

    @Override
    public boolean isCommunityTypeExists(Long communityId, Long productTypeId, Long excludeId) {
        CommunityProductType existed = mapper.selectByCommunityAndType(communityId, productTypeId);
        if (existed == null) {
            return false;
        }
        if (excludeId == null) {
            return true;
        }
        return !excludeId.equals(existed.getId());
    }
}

