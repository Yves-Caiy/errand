package com.axin.system.service;

import com.axin.system.domain.MiniappConfig;

public interface IMiniappConfigService {
    MiniappConfig getCurrent();

    int saveOrUpdate(MiniappConfig config);
}

