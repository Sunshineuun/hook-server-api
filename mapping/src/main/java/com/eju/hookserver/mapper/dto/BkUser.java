package com.eju.hookserver.mapper.dto;

import lombok.Data;

/**
 * @author qiushengming
 */
@Data
public class BkUser {
    /**
     * 手机号码
     */
    private String phoneNo;
    /**
     * 设备号
     */
    private String deviceId;
    /**
     * token
     */
    private String token;

    /**
     * 密码
     */
    private String password;

    public BkUser() {

    }

    private BkUser(Builder builder) {
        this.phoneNo = builder.phoneNo;
        this.password = builder.password;
        this.deviceId = builder.deviceId;
    }

    public static Builder builder() {
        return new Builder();
    }

    public static final class Builder {
        String phoneNo;
        String deviceId;
        String password;

        public Builder phoneNo(String phoneNo) {
            this.phoneNo = phoneNo;
            return this;
        }

        public Builder deviceId(String deviceId) {
            this.deviceId = deviceId;
            return this;
        }

        public Builder password(String password) {
            this.password = password;
            return this;
        }

        public BkUser build() {
            return new BkUser(this);
        }
    }
}
