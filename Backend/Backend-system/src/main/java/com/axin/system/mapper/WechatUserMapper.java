package com.axin.system.mapper;

import com.axin.system.domain.WechatUser;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

@Mapper
public interface WechatUserMapper {
    WechatUser selectWechatUserById(@Param("id") Long id);

    List<WechatUser> selectWechatUserList(WechatUser user);

    int updateEnabledByIds(@Param("ids") Long[] ids, @Param("enabled") Integer enabled);

    WechatUser selectByOpenid(@Param("openid") String openid);

    int insertWechatUser(WechatUser user);

    int updateWechatUser(WechatUser user);

    int updateAdminStatusByIds(@Param("ids") Long[] ids, @Param("isAdmin") Integer isAdmin);
}
