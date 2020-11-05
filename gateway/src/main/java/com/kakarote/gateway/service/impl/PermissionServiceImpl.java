package com.kakarote.gateway.service.impl;

import cn.hutool.core.collection.ListUtil;
import cn.hutool.core.util.StrUtil;
import com.google.common.collect.Lists;
import com.kakarote.core.common.Const;
import com.kakarote.core.common.Result;
import com.kakarote.core.common.SystemCodeEnum;
import com.kakarote.core.entity.UserExtraInfo;
import com.kakarote.core.exception.CrmException;
import com.kakarote.core.exception.NoLoginException;
import com.kakarote.core.redis.Redis;
import com.kakarote.gateway.config.GatewayConfiguration;
import com.kakarote.gateway.config.SwaggerProvider;
import com.kakarote.gateway.service.AuthService;
import com.kakarote.gateway.service.PermissionService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpCookie;
import org.springframework.stereotype.Service;
import org.springframework.util.MultiValueMap;

import java.util.List;
import java.util.Optional;

@Service
@Slf4j
public class PermissionServiceImpl implements PermissionService {
    @Autowired
    private AuthService authService;
    @Autowired
    private GatewayConfiguration configuration;
    @Autowired
    private Redis redis;


    /**
     * 需要cookies
     */
    private static final String[] AUTH_SPECIAL_URLS = {"/adminFile/down/*", "/crmPrint/down", "/crmPrint/preview.pdf"};

    /**
     * 不需要cookies
     */
    private static final List<String> NOT_AUTH_URLS = Lists.newArrayList("/crmMarketing/queryMarketingId", "/crmMarketing/queryAddField", "/crmMarketing/saveMarketingInfo", "/adminUser/queryLoginUser","/adminUser/querySystemStatus","/adminUser/initUser");


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
     * @param url
     * @param cookies
     * @return true为令牌已失效
     */
    @Override
    public String invalidAccessToken(String authentication, String url, MultiValueMap<String, HttpCookie> cookies) {
        if (StrUtil.isEmpty(authentication)) {
            boolean isSpecialUrl = false;
            for (String specialUrl : AUTH_SPECIAL_URLS) {
                if (specialUrl.contains("*")) {
                    if (url.startsWith(specialUrl.replace("*", ""))) {
                        isSpecialUrl = true;
                        break;
                    }
                } else {
                    if (url.equals(specialUrl)) {
                        isSpecialUrl = true;
                        break;
                    }
                }
            }
            if (isSpecialUrl) {
                HttpCookie first = cookies.getFirst(Const.TOKEN_NAME);
                authentication = first != null ? first.getValue() : "";
            }
        }
        if (StrUtil.isEmpty(authentication)) {
            throw new CrmException(SystemCodeEnum.SYSTEM_NOT_LOGIN);
        }
        Object data = redis.get(authentication);
        if (data == null) {
            throw new CrmException(SystemCodeEnum.SYSTEM_NOT_LOGIN);
        }
        if (data instanceof UserExtraInfo) {
            throw new NoLoginException((UserExtraInfo) data);
        }
        return authentication;
    }

    /**
     * 判断url是否不需要授权
     *
     * @param url 请求url
     * @return true为不需要授权
     */
    @Override
    public boolean ignoreAuthentication(String url) {
        boolean isAuth = Optional.ofNullable(configuration.getIgnoreUrl()).orElse(ListUtil.empty()).stream().anyMatch(ignoreUrl -> ignoreUrl.startsWith(url));
        if (!isAuth) {
            if (url.endsWith(SwaggerProvider.API_URI) || NOT_AUTH_URLS.contains(url)) {
                isAuth = true;
            }
        }
        return isAuth;
    }
}
