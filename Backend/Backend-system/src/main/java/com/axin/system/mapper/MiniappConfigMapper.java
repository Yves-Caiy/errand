package com.axin.system.mapper;

import com.axin.system.domain.MiniappConfig;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

@Mapper
public interface MiniappConfigMapper {
    MiniappConfig selectMiniappConfigById(@Param("id") Long id);

    List<MiniappConfig> selectMiniappConfigList(MiniappConfig config);

    MiniappConfig selectLatestMiniappConfig();

    int insertMiniappConfig(MiniappConfig config);

    int updateMiniappConfig(MiniappConfig config);

    int deleteMiniappConfigByIds(@Param("ids") Long[] ids);
}

