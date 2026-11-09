package com.axin.system.mapper;

import com.axin.system.domain.CommunityProductType;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

@Mapper
public interface CommunityProductTypeMapper {
    CommunityProductType selectCommunityProductTypeById(@Param("id") Long id);

    List<CommunityProductType> selectCommunityProductTypeList(CommunityProductType cpt);

    int insertCommunityProductType(CommunityProductType cpt);

    int updateCommunityProductType(CommunityProductType cpt);

    int deleteCommunityProductTypeByIds(@Param("ids") Long[] ids);

    CommunityProductType selectByCommunityAndType(@Param("communityId") Long communityId,
                                                  @Param("productTypeId") Long productTypeId);
}

