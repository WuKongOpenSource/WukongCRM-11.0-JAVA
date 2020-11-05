package com.kakarote.authorization.controller;

import cn.hutool.core.util.StrUtil;
import com.kakarote.authorization.common.AuthorizationCodeEnum;
import com.kakarote.authorization.entity.AuthorizationUser;
import com.kakarote.authorization.service.LoginService;
import com.kakarote.core.common.Const;
import com.kakarote.core.common.ParamAspect;
import com.kakarote.core.common.Result;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.validation.Valid;

/**
 * @author zhangzhiwei
 * 添加权限的controller
 */
@RestController
@Api(tags = "用户登录相关接口")
public class AuthController {

    @Autowired
    private LoginService loginService;


    @RequestMapping(value = "/permission")
    @ParamAspect
    public Result permission(@RequestParam("url") String url, @RequestParam("method") String method, HttpServletRequest request) {
        String token = request.getHeader(Const.TOKEN_NAME);
        return loginService.permission(token, url, method);
    }

    /**
     * 登录方法，限流由sentinel处理
     */
    @PostMapping(value = "/login")
    @ApiOperation(tags = "用户登录", httpMethod = "POST", value = "/doLogin")
    public Result doLogin(@Valid @RequestBody AuthorizationUser user,HttpServletResponse response) {

        if (StrUtil.trimToNull(user.getUsername()) == null) {
            return Result.error(AuthorizationCodeEnum.AUTHORIZATION_USERNAME_REQUIRED);
        }
        if (StrUtil.trimToNull(user.getPassword()) == null && StrUtil.trimToNull(user.getSmscode()) == null) {
            return Result.error(AuthorizationCodeEnum.AUTHORIZATION_PASSWORD_REQUIRED);
        }

        return loginService.doLogin(user,response);
    }

    @RequestMapping(value = "/logout")
    @ApiOperation(tags = "用户注销", httpMethod = "GET", value = "/logout")
    @ParamAspect
    public Result logout(HttpServletRequest request, HttpServletResponse response) {
        String token = request.getHeader(Const.TOKEN_NAME);
        if (StrUtil.isNotEmpty(token)) {
            loginService.logout(token);
        }
        return Result.ok();
    }
}
