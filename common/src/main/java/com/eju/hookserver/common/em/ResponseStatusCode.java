package com.eju.hookserver.common.em;

/**
 * http响应结果状态码
 * <a href="https://blog.csdn.net/t_332741160/article/details/81408597">HTTP状态码对照表</a>
 *
 * @author qiushengming
 */
public enum ResponseStatusCode {
    /**
     * HTTP状态码
     */
    R200(200, "请求成功"),
    R301(301, "资源（网页等）被永久转移到其它URL"),
    R400(400, "客户端请求的语法错误，服务器无法理解"),
    R401(401, "请求要求用户的身份认证"),
    R404(404, "请求的资源（网页等）不存在"),
    R500(500, "内部服务器错误");


    private int code;
    private String msg;

    ResponseStatusCode(Integer code, String msg) {
        this.code = code;
        this.msg = msg;
    }

    public int getCode() {
        return code;
    }

    public void setCode(int code) {
        this.code = code;
    }

    public String getMsg() {
        return msg;
    }

    public void setMsg(String msg) {
        this.msg = msg;
    }

    @Override
    public String toString() {
        return String.valueOf(code);
    }
}
