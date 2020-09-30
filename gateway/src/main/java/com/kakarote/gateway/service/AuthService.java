package com.kakarote.gateway.service;

import com.kakarote.core.common.Const;
import com.kakarote.core.common.Result;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.stereotype.Component;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;


/**
 * 鉴权接口
 *
 * @author zhangzhiwei
 */
@FeignClient(name = "authorization", fallback = AuthService.AuthServiceImpl.class)
public interface AuthService {
    /**
     * 判断是否拥有权限访问
     *
     * @param authentication 用户权限标识
     * @param url            url
     * @param method         方法
     * @return Result
     */
    @RequestMapping(value = "/permission")
    Result hasPermission(@RequestHeader(Const.TOKEN_NAME) String authentication, @RequestParam("url") String url, @RequestParam("method") String method);

    @Component
    class AuthServiceImpl implements AuthService {

        /**
         * 判断是否拥有权限访问
         *
         * @param authentication 用户权限标识
         * @param url            url
         * @param method         方法
         * @return Result
         */
        @Override
        public Result hasPermission(String authentication, String url, String method) {
            return Result.noAuth();
        }
    }
}
