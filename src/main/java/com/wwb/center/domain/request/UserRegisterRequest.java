package com.wwb.center.domain.request;
import lombok.Data;
import java.io.Serializable;

/***
 * 用户注册请求体
 *
 * @author wwb
 */

@Data
public class UserRegisterRequest implements Serializable {
    //    Serializable是序列化尽量写
    private static final long serialVersionUID = 1229977869270755517L;
    /**
     * 用户账号
     */
    private String userAccount;
    /**
     * 用户密码
     */
    private String userPassword;
    /**
     * 校验码
     */
    private String checkPassword;
    /**
     * 星球编号
     */
    private String planetCode;
}
