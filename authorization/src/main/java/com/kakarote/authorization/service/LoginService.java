package com.kakarote.authorization.service;

import com.kakarote.authorization.entity.AuthorizationUser;
import com.kakarote.core.common.Result;

/**
 * @author z
 */
public interface LoginService {
    /**
     * 登录方法的处理
     * @param user 用户对象
     * @return Result
     */
    public Result login(AuthorizationUser user);

    /**
     * 预登录
     * @param user 用户对象
     * @return Result
     */
    public Result doLogin(AuthorizationUser user);

    /**
     * 退出登陆
     * @return Result
     */
    Result logout();
}
