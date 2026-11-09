package com.axin.system.service;

import com.axin.system.domain.RunnerProfile;
import com.axin.system.domain.vo.RunnerCandidateVO;

import java.util.List;

public interface IRunnerProfileService {
    RunnerProfile getByUserId(Long userId);

    List<RunnerProfile> selectRunnerProfileList(RunnerProfile query);

    List<RunnerCandidateVO> selectCandidates(Long receiveCommunityId, Long storeCommunityId);

    List<Long> selectCommunityIdsByRunner(Long runnerUserId);

    int setRunnerCommunities(Long runnerUserId, List<Long> communityIds);

    int saveRunnerProfile(RunnerProfile profile);

    int updateRunnerProfile(RunnerProfile profile);
}
