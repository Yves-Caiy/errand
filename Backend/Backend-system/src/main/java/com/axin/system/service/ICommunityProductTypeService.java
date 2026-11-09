package com.axin.system.service;

import com.axin.system.domain.CommunityProductType;

import java.util.List;

public interface ICommunityProductTypeService {
    CommunityProductType selectCommunityProductTypeById(Long id);

    List<CommunityProductType> selectCommunityProductTypeList(CommunityProductType cpt);

    int insertCommunityProductType(CommunityProductType cpt);

    int updateCommunityProductType(CommunityProductType cpt);

    int deleteCommunityProductTypeByIds(Long[] ids);

    boolean isCommunityTypeExists(Long communityId, Long productTypeId, Long excludeId);
}

