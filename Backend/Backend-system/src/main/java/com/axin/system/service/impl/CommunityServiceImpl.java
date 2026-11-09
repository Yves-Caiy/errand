package com.axin.system.service.impl;

import com.axin.system.domain.Community;
import com.axin.system.mapper.CommunityMapper;
import com.axin.system.service.ICommunityService;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class CommunityServiceImpl implements ICommunityService {

    private final CommunityMapper communityMapper;

    public CommunityServiceImpl(CommunityMapper communityMapper) {
        this.communityMapper = communityMapper;
    }

    @Override
    public Community selectCommunityById(Long id) {
        return communityMapper.selectCommunityById(id);
    }

    @Override
    public List<Community> selectCommunityList(Community community) {
        return communityMapper.selectCommunityList(community);
    }

    @Override
    public int insertCommunity(Community community) {
        return communityMapper.insertCommunity(community);
    }

    @Override
    public int updateCommunity(Community community) {
        return communityMapper.updateCommunity(community);
    }

    @Override
    public int deleteCommunityByIds(Long[] ids) {
        return communityMapper.deleteCommunityByIds(ids);
    }

    @Override
    public boolean isCommunityNameExists(String communityName, Long excludeId) {
        Community existed = communityMapper.selectCommunityByName(communityName);
        if (existed == null) {
            return false;
        }
        if (excludeId == null) {
            return true;
        }
        return !excludeId.equals(existed.getId());
    }
}

