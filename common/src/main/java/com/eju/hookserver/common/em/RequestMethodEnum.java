package com.eju.hookserver.common.em;


/**
 * httpclient请求的类型定义，不同类型请求方式不同。
 *
 * @author qiusm
 */
public enum RequestMethodEnum {
    /**
     * GET;POST_FORM;POST_JSON
     * IMG图片下载
     */
    GET("get"),
    POST_FORM("post-form"),
    POST_JSON("post-json"),
    CHROME_DRIVE("chrome-drive"),
    IMG("img");

    private final String code;

    RequestMethodEnum(String code) {
        this.code = code;
    }

    public String getCode() {
        return code;
    }
}
