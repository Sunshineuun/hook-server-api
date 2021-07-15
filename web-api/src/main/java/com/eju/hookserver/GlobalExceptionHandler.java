package com.eju.hookserver;

import com.eju.hookserver.common.utils.StringUtils;
import com.eju.houseparent.basemodel.BaseDTO;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.ServletRequestBindingException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import javax.servlet.http.HttpServletResponse;

/**
 * 贝壳切面
 *
 * @author qiushengming
 */
@Slf4j
@RestControllerAdvice
public class GlobalExceptionHandler {
    @ExceptionHandler(Exception.class)
    public BaseDTO<String> globalException(Exception ex) {
        BaseDTO<String> baseDTO = new BaseDTO<>();
        baseDTO.setCode("999");
        baseDTO.setMsg(ex.getMessage());
        baseDTO.setResultVo(StringUtils.stackTraceInfoToStr(ex));
        log.error("{}", baseDTO);
        return baseDTO;
    }

    @ExceptionHandler(ServletRequestBindingException.class)
    public BaseDTO<String> servletRequestBindingException(ServletRequestBindingException ex) {
        BaseDTO<String> baseDTO = new BaseDTO<>();
        baseDTO.setCode("999");
        baseDTO.setMsg(ex.getMessage());
        baseDTO.setResultVo(StringUtils.stackTraceInfoToStr(ex));
        if (StringUtils.equals(ex.getMessage(), "Missing session attribute 'phoneNo' of type String")) {
            baseDTO.setMsg("未登录，请重新登录");
        }
        log.error("{}", baseDTO);
        return baseDTO;
    }
}
