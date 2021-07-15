package com.eju.hookserver.service.business.bk.app.login.impl;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.eju.hookserver.common.utils.StringUtils;
import com.eju.hookserver.mapper.dto.BkRequestDto;
import com.eju.hookserver.mapper.dto.BkResponseDto;
import com.eju.hookserver.service.business.bk.app.BkBaseExecute;
import com.eju.hookserver.service.business.bk.app.login.IPhoneVerifyCodeService;
import com.eju.houseparent.common.model.BusinessException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.Map;

/**
 * 手机验证码发送； <br>
 * 面向URL: /user/account/sendverifycodeforbindmobilev2?mobile_phone_no={手机号} <br>
 *
 * @author qiushengming
 */
@Slf4j
@Service
public class PhoneVerifyCodeServiceImpl
        extends BkBaseExecute
        implements IPhoneVerifyCodeService {
    private static final String URL_TEMPLATE = "%s/user/account/sendverifycodeforbindmobilev2?mobile_phone_no=%s";
    private static final String MOBILE_PHONE_NO = "mobile_phone_no";
    private static final String PIC_VERIFY_CODE = "pic_verify_code";
    private static final int PHONE_NO_LENGTH = 11;

    @Override
    protected void buildingUrl(BkRequestDto requestDto) {
        Map<String, String> requestParam = requestDto.getRequestParam();

        // 手机号码不为空 && 手机号码必须等于12位 && 且必须为纯数字
        String mpn = requestDto.getUser().getPhoneNo();
        if (StringUtils.isNotBlank(mpn)
                && StringUtils.length(mpn) == PHONE_NO_LENGTH
                && mpn.matches("[0-9]+")) {
            String url = String.format(URL_TEMPLATE, DOMAIN_NAME, mpn);
            if (requestParam.containsKey(PIC_VERIFY_CODE)) {
                url += "&pic_verify_code=" + requestParam.get(PIC_VERIFY_CODE);
            }
            requestDto.setUrl(url);
        } else {
            throw new BusinessException("手机号码验证不通过.手机号码(不可为空 && 长度必须等于11位 && 必须为纯数字)", mpn);
        }

    }

    /**
     * {"request_id":"64083d97-e980-4648-96cf-fe8eecfe79ce","uniqid":"010ACA160D1DDD9B9E7A0194597357AB","errno":0,"error":"操作成功","data":{},"cost":192}
     * {'request_id': '7b13d82f-af96-4426-980e-f85fb0291c66', 'uniqid': '010ACA160B19AB77567A01D7EB6BBF0B', 'errno': 20021, 'error': '请输入正确的图片验证码(20021-BSK)', 'data': {}, 'cost': 32}
     *
     * @param requestDto  request
     * @param responseDto response
     */
    @Override
    protected void parser(BkRequestDto requestDto, BkResponseDto responseDto) {
        JSONObject result = JSON.parseObject(requestDto.getResponseStr());
        responseDto.setResult(result);
        String mobilePhoneNo = requestDto.getRequestParam().get("mobile_phone_no");
        // 1. 判断是否请求成功
        Integer errno = result.getInteger("errno");
        String error = result.getString("error");
        switch (errno) {
            case 0:
                // 操作成功
                log.debug("发送验证码：{},{}", mobilePhoneNo, error);
                responseDto.setBkErrorMsg(error);
                return;
            case 20026:
            case 20021:
                // 请输入正确的图片验证码(20021-BSK)
                log.warn("发送验证码：{},{}", mobilePhoneNo, error);
                // 当需要图片验证码的时候，请求获取图片验证码服务
                break;
            case 10001:
            case 30020:
                // 发送短信验证码失败(30020-7WA)
                log.error("发送验证码：{},{}", mobilePhoneNo, error);
                break;
            default:
                log.warn("未知异常：{}", errno);
        }
    }

}
