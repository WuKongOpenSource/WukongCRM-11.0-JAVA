package com.kakarote.authorization.common;

import com.kakarote.core.common.ResultCode;

/**
 * @author zhangzhiwei
 * 权限后台响应错误代码枚举类
 */

public enum AuthorizationCodeEnum implements ResultCode {
    //系统响应成功
    AUTHORIZATION_LOGIN(1001, "请先登录"),
    AUTHORIZATION_USERNAME_REQUIRED(1002, "请输入用户名"),
    AUTHORIZATION_PASSWORD_REQUIRED(1003, "请输入密码或短信验证码"),
    AUTHORIZATION_LOGIN_NO_USER(1004, "账号或密码错误"),
    AUTHORIZATION_LOGIN_ERR(1005, "登录认证失败"),
    AUTHORIZATION_COMPANY_NOT_EXIST(1006, "未找到该企业!"),
    AUTHORIZATION_USER_DISABLE_ERROR(1007, "账户被禁用!"),
    AUTHORIZATION_USER_SMS_CODE_ERROR(1008, "验证码错误!"),
    AUTHORIZATION_USER_DOES_NOT_EXIST(1009, "用户不存在,请先注册！"),
    AUTHORIZATION_LOGIN_PASSWORD_TO_MANY_ERROR(1010, "密码错误次数过多，请在s%后重试！"),
    CODE_CANNOT_BE_EMPTY(1011, "code不能为空!");



    AuthorizationCodeEnum(int code, String msg) {
        this.code = code;
        this.msg = msg;
    }

    private int code;
    private String msg;

    @Override
    public int getCode() {
        return code;
    }

    @Override
    public String getMsg() {
        return msg;
    }
}
