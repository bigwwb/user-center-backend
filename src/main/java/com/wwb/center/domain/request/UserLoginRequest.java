package com.wwb.center.domain.request;

import lombok.Data;

import java.io.Serializable;
/**
 * 用户登录请求体
 *
 * @author wwb
 */
@Data
public class UserLoginRequest implements Serializable {
    private static final long serialVersionUID = 7117746568918180207L;
    /**
     * 用户账号
     */
    private String userAccount;
    /**
     * 用户密码
     */
    private String userPassword;
}
