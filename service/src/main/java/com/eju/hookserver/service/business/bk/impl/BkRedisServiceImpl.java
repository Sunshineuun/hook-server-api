package com.eju.hookserver.service.business.bk.impl;

import cn.hutool.core.util.IdUtil;
import com.alibaba.fastjson.JSONObject;
import com.eju.hookserver.common.utils.StringUtils;
import com.eju.hookserver.mapper.dto.BkUser;
import com.eju.hookserver.service.business.bk.IBkRedisService;
import org.springframework.data.redis.core.ValueOperations;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;

import static com.eju.hookserver.common.constant.BkRedisKeyConstant.BK_USER_RKEY;

/**
 * @author qiushengming
 */
@Service
public class BkRedisServiceImpl implements IBkRedisService {

    @Resource
    private ValueOperations<String, String> valueOperations;

    @Override
    public BkUser getUserByPhoneNo(String phoneNo) {
        return getUserByPhoneNo(phoneNo, null);
    }

    @Override
    public BkUser getUserByPhoneNo(String phoneNo, String pw) {
        String userKey = BK_USER_RKEY + phoneNo;
        String userStr = valueOperations.get(userKey);
        BkUser user;

        if (StringUtils.isBlank(userStr)) {
            user = BkUser.builder()
                    .phoneNo(phoneNo)
                    .deviceId(IdUtil.simpleUUID())
                    .build();
            valueOperations.set(userKey, JSONObject.toJSONString(user));
        } else {
            user = JSONObject.parseObject(userStr, BkUser.class);
        }

        if (StringUtils.isNotBlank(pw)) {
            user.setPassword(pw);
            valueOperations.set(userKey, JSONObject.toJSONString(user));
        }

        return user;
    }

    @Override
    public void updateToken(BkUser user) {
        String userKey = BK_USER_RKEY + user.getPhoneNo();
        valueOperations.set(userKey, JSONObject.toJSONString(user));
    }
}
