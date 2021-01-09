package com.kakarote.authorization.service;

import com.kakarote.authorization.entity.AuthorizationUser;
import com.kakarote.core.common.Result;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * @author z
 */
public interface LoginService {
    /**
     * 登录方法的处理
     * @param user 用户对象
     * @param request
     * @return Result
     */
    public Result login(AuthorizationUser user, HttpServletResponse response, HttpServletRequest request);

    /**
     * 预登录
     * @param user 用户对象
     * @return Result
     */
    public Result doLogin(AuthorizationUser user,HttpServletResponse response,HttpServletRequest request);


    /**
     * 权限验证
     * @param authentication
     * @param url
     * @param method
     * @return Result
     */
    Result permission(String authentication, String url, String method);

    /**
     * 退出登陆
     * @param authentication
     * @return Result
     */
    Result logout(String authentication);
}
