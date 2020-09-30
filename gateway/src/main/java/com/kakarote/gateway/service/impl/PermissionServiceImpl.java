package com.kakarote.gateway.service.impl;

import com.kakarote.core.common.Result;
import com.kakarote.core.redis.Redis;
import com.kakarote.gateway.config.GatewayConfiguration;
import com.kakarote.gateway.service.AuthService;
import com.kakarote.gateway.service.PermissionService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
@Slf4j
public class PermissionServiceImpl implements PermissionService {
    @Autowired
    private Redis redis;
    @Autowired
    private AuthService authService;
    @Autowired
    private GatewayConfiguration configuration;

    /**
     * 调用签权服务，判断用户是否有权限
     *
     * @param authentication 权限标识
     * @param url            请求url
     * @param method         请求方法
     * @return true/false
     */
    @Override
    public boolean hasPermission(String authentication, String url, String method) {
        Result result = authService.hasPermission(authentication, url, method);
        return result.hasSuccess();
    }

    /**
     * 判断token是否有效
     *
     * @param authentication 权限标识
     * @return true为令牌已失效
     */
    @Override
    public boolean invalidAccessToken(String authentication) {
        return true;
    }

    /**
     * 判断url是否不需要授权
     *
     * @param url 请求url
     * @return true为不需要授权
     */
    @Override
    public boolean ignoreAuthentication(String url) {
        return configuration.getIgnoreUrl().stream().anyMatch(ignoreUrl -> ignoreUrl.startsWith(url));
    }
}
