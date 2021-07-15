package com.eju.hookserver.service.business.bk.app.login.impl;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.eju.hookserver.common.em.RequestMethodEnum;
import com.eju.hookserver.common.utils.BeikeUtils;
import com.eju.hookserver.common.utils.JSONUtils;
import com.eju.hookserver.common.utils.StringUtils;
import com.eju.hookserver.mapper.dto.BkRequestDto;
import com.eju.hookserver.mapper.dto.BkResponseDto;
import com.eju.hookserver.service.business.bk.app.BkBaseExecute;
import com.eju.hookserver.service.business.bk.app.login.ILoginByVerifyCodeService;
import com.eju.houseparent.common.model.BusinessException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.Map;

import static com.eju.hookserver.common.constant.head.BkHttpHeadConstant.AUTHORIZATION;

/**
 * @author qiushengming
 */
@Slf4j
@Service
public class LoginByVerifyCodeServiceImpl
        extends BkBaseExecute
        implements ILoginByVerifyCodeService {
    private static final String URL_TEMPLATE = "https://app.api.ke.com/user/account/loginbyverifycode";
    private static final String MOBILE_PHONE_NO = "mobile_phone_no";
    private static final String PIC_VERIFY_CODE = "pic_verify_code";
    private static final String VERIFY_CODE = "verify_code";
    private static final String REQUEST_TS = "request_ts";

    @Override
    protected void buildingUrl(BkRequestDto requestDto) {
        requestDto.setUrl(URL_TEMPLATE);
        requestDto.setRequestMethod(RequestMethodEnum.POST_FORM);

        Map<String, String> requestParam = requestDto.getRequestParam();
        requestParam.put(REQUEST_TS, Long.toString(System.currentTimeMillis()));
        requestParam.put(MOBILE_PHONE_NO, requestDto.getUser().getPhoneNo());

        String[] keys = new String[]{MOBILE_PHONE_NO, VERIFY_CODE};
        for (String key : keys) {
            if (requestParam.containsKey(key)) {
                String value = requestParam.get(key).trim();
                if (!StringUtils.isNotBlank(value)) {
                    throw new BusinessException("关键数据存在为空。", key);
                }
            }
        }
    }

    @Override
    protected void parser(BkRequestDto requestDto, BkResponseDto responseDto) {
        JSONObject json = JSON.parseObject(requestDto.getResponseStr());
        Integer errno = json.getInteger("errno");
        if (errno == 0) {
            String accessToken = JSONUtils.getStringByKey(json, "data.access_token");
            requestDto.getUser().setToken(accessToken);
            responseDto.setResult(json);
        }
    }

    @Override
    protected void buildingHeader(BkRequestDto dto) {
        super.buildingHeader(dto);
        Map<String, String> baseHead = new HashMap<>(16);
        baseHead.put(AUTHORIZATION, BeikeUtils.authorization(dto.getUrl(), dto.getRequestParam()));
        dto.getHead().putAll(baseHead);
    }
}
