package com.kakarote.gateway.service;

/**
 * 统一鉴权service
 * @author zhangzhiwei
 */
public interface PermissionService {

    /**
     * 调用签权服务，判断用户是否有权限
     *
     * @param authentication 权限标识
     * @param url 请求url
     * @param method 请求方法
     * @return true/false
     */
    boolean hasPermission(String authentication, String url, String method);

    /**
     * 判断token是否有效
     *
     * @param authentication 权限标识
     * @return true为令牌已失效
     */
    boolean invalidAccessToken(String authentication);

    /**
     * 判断url是否不需要授权
     * @param url 请求url
     * @return true为不需要授权
     */
    boolean ignoreAuthentication(String url);
}
