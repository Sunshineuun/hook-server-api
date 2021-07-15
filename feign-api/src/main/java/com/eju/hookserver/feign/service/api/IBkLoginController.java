package com.eju.hookserver.feign.service.api;

import com.eju.houseparent.basemodel.BaseDTO;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.SessionAttribute;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

/**
 * 贝壳登录
 *
 * @author qiushengming
 */
public interface IBkLoginController {

    String PREFIX = "/bk/login/";

    /**
     * 通过手机号码来获取图片验证码 <br>
     * 返回图片流
     *
     * @param response HttpServletResponse
     * @param request  HttpServletRequest
     * @param phoneNo  手机号码
     * @param cityId   城市6位编码
     */
    @GetMapping(PREFIX + "get/picVerifyCodeByPhone")
    void getPicVerifyCodeByPhone(
            HttpServletResponse response,
            HttpServletRequest request,
            String phoneNo,
            @RequestParam(value = "cityId", required = false, defaultValue = "310000") String cityId);


    /**
     * 通过手机号码+图片验证码获取短信验证码农
     *
     * @param session       session
     * @param phoneNo       手机号码
     * @param picVerifyCode 图片验证码
     * @param cityId        城市6位编码
     * @return 返回成功状态
     */
    @GetMapping(PREFIX + "get/phoneVerifyCodeByPhone")
    BaseDTO<String> getPhoneVerifyCodeByPhone(
            HttpSession session, String phoneNo, String picVerifyCode,
            @RequestParam(value = "cityId", required = false, defaultValue = "310000") String cityId);

    /**
     * 通过短信验证码+图片验证码+手机号码获取token
     *
     *
     * @param session
     * @param phoneNo       手机号码
     * @param verifyCode    短信验证码
     * @param picVerifyCode 图片验证码
     * @param cityId        城市6位编码
     * @return token
     */
    @GetMapping(PREFIX + "loginByVerifyCode")
    BaseDTO<String> loginByVerifyCode(
            HttpSession session, String phoneNo, String verifyCode, String picVerifyCode,
            @RequestParam(value = "cityId", required = false, defaultValue = "310000") String cityId);

    /**
     * 通过手机号码+密码+图片验证码进行登录
     *
     *
     * @param session
     * @param phoneNo       手机号码
     * @param password      密码
     * @param picVerifyCode 图片验证码
     * @param cityId        城市6位编码
     * @return return token
     */
    @GetMapping(PREFIX + "loginByPasswordV2")
    BaseDTO<String> loginByPasswordV2(
            HttpSession session, String phoneNo, String password, String picVerifyCode,
            @RequestParam(value = "cityId", required = false, defaultValue = "310000") String cityId);

    /**
     * 获取用户信息
     *
     * @param phoneNo phoneNo
     * @return BkUser
     */
    @GetMapping(PREFIX + "get/user/")
    BaseDTO<Object> getUserByPhoneNo(
            @SessionAttribute("phoneNo") String phoneNo);
}
