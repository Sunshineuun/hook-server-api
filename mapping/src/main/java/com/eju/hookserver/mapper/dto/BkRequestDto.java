package com.eju.hookserver.mapper.dto;

import com.eju.hookserver.common.em.RequestMethodEnum;
import lombok.Data;
import org.apache.commons.lang3.StringUtils;

import java.util.HashMap;
import java.util.Map;

import static com.eju.hookserver.common.constant.CharacterSet.UTF8;

/**
 * @author qiushengming
 */
@Data
public class BkRequestDto {
    /**
     * 请求地址
     */
    private String url;
    /**
     * 请求的字符串集
     */
    private String charset;
    /**
     * 请求方式
     */
    private RequestMethodEnum requestMethod = RequestMethodEnum.GET;
    /**
     * 判断是否需要登录
     */
    private Boolean isLoad;
    /**
     * 请求返回的结果
     */
    private String responseStr;
    /**
     * 用于传输图片、文件流
     */
    private byte[] responseByte;
    /**
     * 请求使用的参数
     */
    private Map<String, String> requestParam;
    /**
     * 请求头
     */
    private Map<String, String> head;
    /**
     * 用户信息
     */
    private BkUser user;

    private BkRequestDto(Builder builder) {
        this.url = builder.url;
        this.charset = builder.charset;
        this.requestParam = builder.requestParam;
        this.head = builder.head;
        this.user = builder.user;
        this.isLoad = builder.isLoad;
    }

    public static Builder builder() {
        return new Builder();
    }

    public static final class Builder {
        String url;
        String charset = UTF8;
        Map<String, String> requestParam = new HashMap<>();
        Map<String, String> head = new HashMap<>();
        BkUser user;
        Boolean isLoad = false;

        public Builder url(String url) {
            this.url = url;
            return this;
        }

        public Builder charset(String charset) {
            if (StringUtils.isNotBlank(charset)) {
                this.charset = charset;
            }
            return this;
        }

        public Builder requestParam(String key, String value) {
            this.requestParam.put(key, value);
            return this;
        }

        public Builder requestParam(Map<String, String> requestParam) {
            this.requestParam.putAll(requestParam);
            return this;
        }

        public Builder head(String key, String value) {
            this.head.put(key, value);
            return this;
        }

        public Builder head(Map<String, String> head) {
            this.head.putAll(head);
            return this;
        }

        public Builder user(BkUser user) {
            this.user = user;
            return this;
        }

        public Builder isLoad(Boolean isLoad) {
            this.isLoad = isLoad;
            return this;
        }

        public BkRequestDto build() {
            return new BkRequestDto(this);
        }


    }

}
