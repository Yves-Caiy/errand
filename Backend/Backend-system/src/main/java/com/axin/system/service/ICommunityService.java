package com.axin.system.service;

import com.axin.system.domain.Community;

import java.util.List;

public interface ICommunityService {
    Community selectCommunityById(Long id);

    List<Community> selectCommunityList(Community community);

    int insertCommunity(Community community);

    int updateCommunity(Community community);

    int deleteCommunityByIds(Long[] ids);

    boolean isCommunityNameExists(String communityName, Long excludeId);
}

