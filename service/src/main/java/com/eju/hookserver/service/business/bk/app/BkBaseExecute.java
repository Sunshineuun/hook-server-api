package com.eju.hookserver.service.business.bk.app;

import cn.hutool.core.util.RandomUtil;
import com.alibaba.fastjson.JSONObject;
import com.eju.hookserver.common.utils.BeikeUtils;
import com.eju.hookserver.common.utils.ImageReaderUtils;
import com.eju.hookserver.common.utils.StringUtils;
import com.eju.hookserver.mapper.dto.BkRequestDto;
import com.eju.hookserver.mapper.dto.BkResponseDto;
import com.eju.hookserver.service.business.HttpExecute;
import com.eju.houseparent.common.model.BusinessException;
import com.eju.houseparent.common.utils.OkHttpUtils;
import lombok.extern.slf4j.Slf4j;

import java.net.SocketTimeoutException;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

import static com.eju.hookserver.common.constant.NumberConstant.NUM6;
import static com.eju.hookserver.common.constant.ProxyConstant.PROXY_URL0;
import static com.eju.hookserver.common.constant.head.BkHttpHeadConstant.*;

/**
 * @author qiushengming
 */
@Slf4j
public abstract class BkBaseExecute implements HttpExecute {

    private static final String CITY_ID = "city_id";

    protected static final String DOMAIN_NAME = "https://app.api.ke.com";

    protected OkHttpUtils httpClient = OkHttpUtils.Builder()
            .proxyUrl(PROXY_URL0)
            .addProxyRetryTag("ejuResponseCode")
            .builderHttp();

    /**
     * url构建的逻辑，由子类实现
     *
     * @param requestDto 请求dto
     */
    protected abstract void buildingUrl(BkRequestDto requestDto);

    /**
     * 解析响应结果
     *
     * @param requestDto  request
     * @param responseDto response
     */
    protected abstract void parser(BkRequestDto requestDto, BkResponseDto responseDto);

    /**
     * 整体流程的管控
     *
     * @param requestDto 关键字
     * @return BkResponseDto response
     */
    @Override
    public BkResponseDto execute(BkRequestDto requestDto) {
        BkResponseDto responseDto = new BkResponseDto();
        try {
            //0. 检验主要参数
            checkRequestParam(requestDto);
            //1. 构建URL
            buildingUrl(requestDto);
            //2. 构建请求头
            buildingHeader(requestDto);

            //3. 检查是否需要登录
            buildingCookie(requestDto);

            //4. 发送请求
            httpGet(requestDto);
            log.info("{}", requestDto);
            //5. 判断结果是否符合期望
            if (viewCheck(requestDto)) {
                //6. 解析结果httpGet
                parser(requestDto, responseDto);
            } else {
                throw new BusinessException("响应体不合法");
            }

            parserBkErrorMsg(requestDto, responseDto);
            //7. 判断结果状态
            if (StringUtils.equals(responseDto.getBkErrorCode(), "0")) {
                responseDto.setSuccess(true);
            } else {
                responseDto.setSuccess(false);
                responseDto.setSysErrorMsg(requestDto.getResponseStr());
                responseDto.setResult(new JSONObject());
            }

        } catch (Exception e) {
            // 1. SocketTimeoutException, 图片读取超时
            log.error("{}\n{}\n{}\n{}", e.getMessage(), requestDto, responseDto, StringUtils.stackTraceInfoToStr(e));
            requestDto.setResponseStr(e.getMessage());
        }

        return responseDto;
    }

    protected void checkRequestParam(BkRequestDto requestDto) {
        Map<String, String> requestParam = requestDto.getRequestParam();
        if (requestParam.containsKey(CITY_ID)) {
            String cityId = requestParam.get(CITY_ID).trim();
            if (!(StringUtils.isNotBlank(cityId) && StringUtils.length(cityId) == NUM6)) {
                throw new BusinessException("city_id不能为空. {}");
            }
        }
    }

    protected void parserBkErrorMsg(BkRequestDto requestDto, BkResponseDto responseDto) {
        if (StringUtils.isNotBlank(requestDto.getResponseStr())) {
            JSONObject var = JSONObject.parseObject(requestDto.getResponseStr());
            responseDto.setBkErrorMsg(var.getString("error"));
            responseDto.setBkErrorCode(Integer.toString(var.getInteger("errno")));
        }
    }

    /**
     * 通过httpclinet获取请求
     */
    protected void httpGet(BkRequestDto requestDto) {
        String htmlStr = null;
        byte[] imgByte = null;
        switch (requestDto.getRequestMethod()) {
            case GET:
                htmlStr = httpClient.get(requestDto.getUrl(), requestDto.getCharset(), requestDto.getHead());
                break;
            case IMG:
                imgByte = ImageReaderUtils.imageToByteV2(requestDto.getUrl(), requestDto.getHead());
                break;
            case POST_FORM:
                htmlStr = httpClient.postFrom(requestDto.getUrl(), requestDto.getCharset(), requestDto.getHead(), requestDto.getRequestParam());
                break;
            default:
                log.error("未知请求类型{}", requestDto.getRequestMethod());
        }
        if (imgByte != null) {
            requestDto.setResponseByte(imgByte);
        }

        if (htmlStr != null) {
            requestDto.setResponseStr(htmlStr);
        }


    }

    protected boolean viewCheck(BkRequestDto requestDto) {
        String responseStr = requestDto.getResponseStr();
        return !(StringUtils.isBlank(responseStr)
                || responseStr.startsWith("ejuResponseCode")
                || responseStr.startsWith("ResponseError")
                || responseStr.startsWith("ResponseCode"));
    }

    protected void buildingHeader(BkRequestDto dto) {
        Map<String, String> baseHead = new HashMap<>(16);
        baseHead.put(AUTHORIZATION, BeikeUtils.authorization(dto.getUrl()));
        baseHead.put(ACCEPT, "application/json");
        baseHead.put(ACCEPT_ENCODING, "utf-8");
        baseHead.put(REFERER, "ershoulistsearch");
        baseHead.put(USER_AGENT, "Beike2.20.1;google Pixel; Android 8.1.0");
        baseHead.put(HOST, "app.api.ke.com");
        baseHead.put(CONNECTION, "Keep-Alive");
        baseHead.put(LIANJIA_CHANNEL, "Android_ke_wandoujia");
        baseHead.put(LIANJIA_VERSION, "2.20.1");
        baseHead.put(LIANJIA_IM_VERSION, "2.34.0");
        baseHead.put(LIANJIA_DEVICE_ID, dto.getUser().getDeviceId());

        baseHead.putAll(dto.getHead());

        if (!baseHead.containsKey(LIANJIA_CITY_ID)) {
            throw new BusinessException("请求头缺少【LIANJIA_CITY_ID】");
        }

        dto.setHead(baseHead);

    }

    protected void buildingCookie(BkRequestDto dto) {
        String cookie = String.format("lianjia_udid=330000000%s;lianjia_ssid=%s;lianjia_uuid=%s;",
                RandomUtil.randomNumbers(6), UUID.randomUUID(), UUID.randomUUID());
        if (dto.getIsLoad()) {
            if (StringUtils.isNotBlank(dto.getUser().getToken())) {
                cookie += "lianjia_token=" + dto.getUser().getToken();
            } else {
                throw new BusinessException("无效的cookie");
            }
        }
        dto.getHead().put(COOKIE, cookie);
    }
}
