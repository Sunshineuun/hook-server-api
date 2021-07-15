package com.eju.hookserver.service.business.bk;

import com.eju.hookserver.mapper.dto.BkUser;

/**
 * 贝壳redis支持
 *
 * @author qiushengming
 */
public interface IBkRedisService {

    /**
     * 通过手机号码获取用户对象
     *
     * @param phoneNo 手机号码
     * @return BkUser
     */
    BkUser getUserByPhoneNo(String phoneNo);

    /**
     * 通过手机号码获取用户对象，并更新密码
     *
     * @param phoneNo 手机号码
     * @param pw      密码
     * @return BkUser
     */
    BkUser getUserByPhoneNo(String phoneNo, String pw);

    /**
     * 更新token
     *
     * @param user BkUser
     */
    void updateToken(BkUser user);
}
