package com.eju.hookserver.controller.inner;

import com.eju.hookserver.feign.service.api.IBkLoginController;
import com.eju.hookserver.mapper.dto.BkRequestDto;
import com.eju.hookserver.mapper.dto.BkResponseDto;
import com.eju.hookserver.mapper.dto.BkUser;
import com.eju.hookserver.service.business.bk.IBkRedisService;
import com.eju.hookserver.service.business.bk.app.login.ILoginByPasswordV2Service;
import com.eju.hookserver.service.business.bk.app.login.ILoginByVerifyCodeService;
import com.eju.hookserver.service.business.bk.app.login.IPhoneVerifyCodeService;
import com.eju.hookserver.service.business.bk.app.login.IPicVerifyCodeService;
import com.eju.houseparent.basemodel.BaseDTO;
import com.eju.houseparent.basemodel.BaseDTOCode;
import com.eju.houseparent.config.starter.BaseInnerController;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiParam;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.io.IOUtils;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import java.io.IOException;

import static com.eju.hookserver.common.constant.head.BkHttpHeadConstant.LIANJIA_CITY_ID;

/**
 * 贝壳登录控制
 *
 * @author qiushengming
 */
@Api(value = "贝壳登录", tags = "贝壳登录")
@Slf4j
@RestController
public class BkLoginController implements IBkLoginController, BaseInnerController {

    @Resource
    private IPicVerifyCodeService picVerifyCodeService;

    @Resource
    private IPhoneVerifyCodeService phoneVerifyCodeService;

    @Resource
    private ILoginByVerifyCodeService loginByVerifyCodeService;

    @Resource
    private ILoginByPasswordV2Service loginByPasswordV2Service;

    @Resource
    private IBkRedisService redisService;

    @Override
    public void getPicVerifyCodeByPhone(@ApiParam(hidden = true) HttpServletResponse response,
                                        HttpServletRequest request,
                                        String phoneNo, String cityId) {
        request.getSession().setAttribute("phoneNo", phoneNo);

        BkUser user = redisService.getUserByPhoneNo(phoneNo);
        BkRequestDto requestDto = BkRequestDto.builder()
                .user(user)
                .head(LIANJIA_CITY_ID, cityId)
                .build();
        BkResponseDto responseDto = picVerifyCodeService.execute(requestDto);
        try {
            response.setContentType(MediaType.IMAGE_PNG_VALUE);
            IOUtils.write(responseDto.getResultByte(), response.getOutputStream());
        } catch (IOException exception) {
            log.error("{}", exception.getMessage());
        }
    }

    @Override
    public BaseDTO<String> getPhoneVerifyCodeByPhone(
            HttpSession session, String phoneNo, String picVerifyCode, String cityId) {
        session.setAttribute("phoneNo", phoneNo);

        BkUser user = redisService.getUserByPhoneNo(phoneNo);

        BkRequestDto requestDto = BkRequestDto.builder()
                .user(user)
                .requestParam("pic_verify_code", picVerifyCode)
                .head(LIANJIA_CITY_ID, cityId)
                .build();
        BkResponseDto responseDto = phoneVerifyCodeService.execute(requestDto);
        return toBaseDto(responseDto);
    }

    @Override
    public BaseDTO<String> loginByVerifyCode(
            HttpSession session, String phoneNo, String verifyCode, String picVerifyCode, String cityId) {
        BkUser user = redisService.getUserByPhoneNo(phoneNo);
        BkRequestDto requestDto = BkRequestDto.builder()
                .user(user)
                .requestParam("verify_code", verifyCode)
                .requestParam("pic_verify_code", picVerifyCode)
                .head(LIANJIA_CITY_ID, cityId)
                .build();
        BkResponseDto responseDto = loginByVerifyCodeService.execute(requestDto);

        BaseDTO<String> baseDTO = new BaseDTO<>();
        baseDTO.setMsg(responseDto.getBkErrorMsg());
        if (responseDto.getSuccess()) {
            redisService.updateToken(user);
            baseDTO.setCode(BaseDTOCode.SUCCESS.getCode());
        } else {
            baseDTO.setCode(BaseDTOCode.FAILED.getCode());
        }
        return baseDTO;
    }

    @Override
    public BaseDTO<String> loginByPasswordV2(
            HttpSession session, String phoneNo, String password, String picVerifyCode, String cityId) {
        BkUser user = redisService.getUserByPhoneNo(phoneNo, password);
        BkRequestDto requestDto = BkRequestDto.builder()
                .user(user)
                .requestParam("pic_verify_code", picVerifyCode)
                .head(LIANJIA_CITY_ID, cityId)
                .build();
        BkResponseDto responseDto = loginByPasswordV2Service.execute(requestDto);

        BaseDTO<String> baseDTO = new BaseDTO<>();
        baseDTO.setMsg(responseDto.getBkErrorMsg());
        if (responseDto.getSuccess()) {
            redisService.updateToken(user);
            baseDTO.setCode(BaseDTOCode.SUCCESS.getCode());
        } else {
            baseDTO.setCode(BaseDTOCode.FAILED.getCode());
        }
        return baseDTO;
    }

    @Override
    public BaseDTO<Object> getUserByPhoneNo(String phoneNo) {
        BkUser user = redisService.getUserByPhoneNo(phoneNo);
        BaseDTO<Object> baseDTO = new BaseDTO<>();
        baseDTO.setResultVo(user);
        return baseDTO;
    }

    private BaseDTO<String> toBaseDto(BkResponseDto responseDto) {
        BaseDTO<String> baseDTO = new BaseDTO<>();
        baseDTO.setMsg(responseDto.getBkErrorMsg());
        if (responseDto.getSuccess()) {
            baseDTO.setCode(BaseDTOCode.SUCCESS.getCode());
        } else {
            baseDTO.setCode(responseDto.getBkErrorCode());
        }
        return baseDTO;
    }

}
