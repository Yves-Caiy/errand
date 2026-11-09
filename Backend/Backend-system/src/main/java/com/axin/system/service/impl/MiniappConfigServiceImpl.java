package com.axin.system.service.impl;

import com.axin.system.domain.MiniappConfig;
import com.axin.system.mapper.MiniappConfigMapper;
import com.axin.system.service.IMiniappConfigService;
import org.springframework.stereotype.Service;

@Service
public class MiniappConfigServiceImpl implements IMiniappConfigService {

    private final MiniappConfigMapper mapper;

    public MiniappConfigServiceImpl(MiniappConfigMapper mapper) {
        this.mapper = mapper;
    }

    @Override
    public MiniappConfig getCurrent() {
        return mapper.selectLatestMiniappConfig();
    }

    @Override
    public int saveOrUpdate(MiniappConfig config) {
        MiniappConfig existed = mapper.selectLatestMiniappConfig();
        if (existed == null) {
            return mapper.insertMiniappConfig(config);
        } else {
            config.setId(existed.getId());
            return mapper.updateMiniappConfig(config);
        }
    }
}

