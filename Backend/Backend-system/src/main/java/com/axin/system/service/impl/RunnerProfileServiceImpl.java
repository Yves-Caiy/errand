package com.axin.system.service.impl;

import com.axin.system.domain.RunnerProfile;
import com.axin.system.domain.vo.RunnerCandidateVO;
import com.axin.system.mapper.RunnerProfileMapper;
import com.axin.system.service.IRunnerProfileService;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
public class RunnerProfileServiceImpl implements IRunnerProfileService {

    private final RunnerProfileMapper runnerProfileMapper;

    public RunnerProfileServiceImpl(RunnerProfileMapper runnerProfileMapper) {
        this.runnerProfileMapper = runnerProfileMapper;
    }

    @Override
    public RunnerProfile getByUserId(Long userId) {
        return runnerProfileMapper.selectRunnerProfileByUserId(userId);
    }

    @Override
    public List<RunnerProfile> selectRunnerProfileList(RunnerProfile query) {
        return runnerProfileMapper.selectRunnerProfileList(query);
    }

    @Override
    public List<RunnerCandidateVO> selectCandidates(Long receiveCommunityId, Long storeCommunityId) {
        return runnerProfileMapper.selectCandidates(receiveCommunityId, storeCommunityId);
    }

    @Override
    public List<Long> selectCommunityIdsByRunner(Long runnerUserId) {
        return runnerProfileMapper.selectCommunityIdsByRunner(runnerUserId);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public int setRunnerCommunities(Long runnerUserId, java.util.List<Long> communityIds) {
        runnerProfileMapper.deleteRunnerCommunityByRunner(runnerUserId);
        if (communityIds == null || communityIds.isEmpty()) {
            return 0;
        }
        return runnerProfileMapper.batchInsertRunnerCommunity(runnerUserId, communityIds);
    }

    @Override
    public int saveRunnerProfile(RunnerProfile profile) {
        RunnerProfile existed = runnerProfileMapper.selectRunnerProfileByUserId(profile.getUserId());
        if (existed == null) {
            if (profile.getIsRunner() == null) profile.setIsRunner(1);
            if (profile.getOnlineStatus() == null) profile.setOnlineStatus(1);
            if (profile.getMaxConcurrent() == null) profile.setMaxConcurrent(3);
            if (profile.getIsAllCommunity() == null) profile.setIsAllCommunity(0);
            if (profile.getWeightBoost() == null) profile.setWeightBoost(0);
            return runnerProfileMapper.insertRunnerProfile(profile);
        }
        profile.setUserId(existed.getUserId());
        return runnerProfileMapper.updateRunnerProfile(profile);
    }

    @Override
    public int updateRunnerProfile(RunnerProfile profile) {
        return runnerProfileMapper.updateRunnerProfile(profile);
    }
}
