package com.eju.hookserver.service.business.bk.app.login.impl;

import com.eju.hookserver.common.em.RequestMethodEnum;
import com.eju.hookserver.common.utils.StringUtils;
import com.eju.hookserver.mapper.dto.BkRequestDto;
import com.eju.hookserver.mapper.dto.BkResponseDto;
import com.eju.hookserver.service.business.bk.app.BkBaseExecute;
import com.eju.hookserver.service.business.bk.app.login.IPicVerifyCodeService;
import com.eju.houseparent.common.model.BusinessException;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.Map;

import static com.eju.hookserver.common.constant.head.HttpHeadConstant.*;

/**
 * 图形验证码获取校验
 * 面向URL:/user/VerifyCode/GeneratePicture?device_id={}&radom={}
 *
 * @author qiushengming
 */
@Service
public class PicVerifyCodeServiceServiceImpl
        extends BkBaseExecute
        implements IPicVerifyCodeService {
    private static final String URL_TEMPLATE = "%s/user/VerifyCode/GeneratePicture?device_id=%s&radom=%s";
    private static final String DEVICE_ID = "device_id";

    @Override
    protected void buildingUrl(BkRequestDto requestDto) {
        Map<String, String> requestParam = requestDto.getRequestParam();

        requestParam.put(DEVICE_ID, requestDto.getUser().getDeviceId());
        if (StringUtils.isNotBlank(requestDto.getUser().getDeviceId())) {
            String url = String.format(URL_TEMPLATE, DOMAIN_NAME, requestDto.getUser().getDeviceId(), System.currentTimeMillis());
            requestDto.setUrl(url);
        } else {
            throw new BusinessException("请求体缺失关键参数{deviceId}.");
        }

        requestDto.setRequestMethod(RequestMethodEnum.IMG);
    }

    @Override
    protected void parser(BkRequestDto requestDto, BkResponseDto responseDto) {
        responseDto.setResultByte(requestDto.getResponseByte());
    }

    @Override
    protected void buildingHeader(BkRequestDto dto) {
        Map<String, String> baseHead = new HashMap<>(16);
        baseHead.put(ACCEPT, "gzip");
        baseHead.put(USER_AGENT, "Dalvik/2.1.0 (Linux; U; Android 8.1.0; Pixel Build/OPM1.171019.011)");
        baseHead.put(HOST, "app.api.ke.com");
        baseHead.put(CONNECTION, "Keep-Alive");

        baseHead.putAll(dto.getHead());
        dto.setHead(baseHead);
    }

    @Override
    protected boolean viewCheck(BkRequestDto requestDto) {
        return requestDto.getResponseByte() != null
                && requestDto.getResponseByte().length > 0;
    }
}
