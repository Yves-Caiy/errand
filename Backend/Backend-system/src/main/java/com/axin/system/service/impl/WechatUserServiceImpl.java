package com.axin.system.service.impl;

import com.axin.system.domain.WechatUser;
import com.axin.system.mapper.WechatUserMapper;
import com.axin.system.service.IWechatUserService;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class WechatUserServiceImpl implements IWechatUserService {

    private final WechatUserMapper mapper;

    public WechatUserServiceImpl(WechatUserMapper mapper) {
        this.mapper = mapper;
    }

    @Override
    public WechatUser selectWechatUserById(Long id) {
        return mapper.selectWechatUserById(id);
    }

    @Override
    public List<WechatUser> selectWechatUserList(WechatUser user) {
        return mapper.selectWechatUserList(user);
    }

    @Override
    public int updateEnabledByIds(Long[] ids, Integer enabled) {
        return mapper.updateEnabledByIds(ids, enabled);
    }

    @Override
    public WechatUser selectByOpenid(String openid) {
        return mapper.selectByOpenid(openid);
    }

    @Override
    public int insertWechatUser(WechatUser user) {
        return mapper.insertWechatUser(user);
    }

    @Override
    public int updateWechatUser(WechatUser user) {
        return mapper.updateWechatUser(user);
    }

    @Override
    public int updateAdminStatusByIds(Long[] ids, Integer isAdmin) {
        return mapper.updateAdminStatusByIds(ids, isAdmin);
    }
}
