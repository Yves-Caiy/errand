package com.axin.system.mapper;

import com.axin.system.domain.Community;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

@Mapper
public interface CommunityMapper {
    Community selectCommunityById(@Param("id") Long id);

    Community selectCommunityByName(@Param("communityName") String communityName);

    List<Community> selectCommunityList(Community community);

    int insertCommunity(Community community);

    int updateCommunity(Community community);

    int deleteCommunityByIds(@Param("ids") Long[] ids);
}

