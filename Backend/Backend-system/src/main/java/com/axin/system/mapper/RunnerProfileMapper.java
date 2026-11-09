package com.axin.system.mapper;

import com.axin.system.domain.RunnerProfile;
import com.axin.system.domain.vo.RunnerCandidateVO;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

@Mapper
public interface RunnerProfileMapper {
    RunnerProfile selectRunnerProfileById(@Param("id") Long id);

    RunnerProfile selectRunnerProfileByUserId(@Param("userId") Long userId);

    List<RunnerProfile> selectRunnerProfileList(RunnerProfile query);

    int insertRunnerProfile(RunnerProfile profile);

    int updateRunnerProfile(RunnerProfile profile);

    List<Long> selectCommunityIdsByRunner(@Param("runnerUserId") Long runnerUserId);

    int deleteRunnerCommunityByRunner(@Param("runnerUserId") Long runnerUserId);

    int batchInsertRunnerCommunity(@Param("runnerUserId") Long runnerUserId,
                                   @Param("communityIds") List<Long> communityIds);

    /**
     * 派单候选池：启用账号 + 是接单员 + 接单中 + 覆盖收货小区（含全部小区）
     * 同时带出在途/质量/时效统计
     */
    List<RunnerCandidateVO> selectCandidates(@Param("receiveCommunityId") Long receiveCommunityId,
                                             @Param("storeCommunityId") Long storeCommunityId);
}
