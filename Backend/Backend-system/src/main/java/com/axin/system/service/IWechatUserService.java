package com.axin.system.service;

import com.axin.system.domain.WechatUser;

import java.util.List;

public interface IWechatUserService {
    WechatUser selectWechatUserById(Long id);

    List<WechatUser> selectWechatUserList(WechatUser user);

    int updateEnabledByIds(Long[] ids, Integer enabled);

    WechatUser selectByOpenid(String openid);

    int insertWechatUser(WechatUser user);

    int updateWechatUser(WechatUser user);

    int updateAdminStatusByIds(Long[] ids, Integer isAdmin);
}
