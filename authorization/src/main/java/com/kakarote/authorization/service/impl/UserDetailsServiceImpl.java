package com.kakarote.authorization.service.impl;

import cn.hutool.core.util.IdUtil;
import com.kakarote.authorization.common.AuthException;
import com.kakarote.authorization.common.AuthorizationCodeEnum;
import com.kakarote.authorization.entity.AdminUserStatusBO;
import com.kakarote.authorization.entity.AuthorizationUser;
import com.kakarote.authorization.entity.AuthorizationUserInfo;
import com.kakarote.authorization.entity.VO.LoginVO;
import com.kakarote.authorization.service.AdminUserService;
import com.kakarote.authorization.service.LoginService;
import com.kakarote.core.common.Result;
import com.kakarote.core.common.SystemCodeEnum;
import com.kakarote.core.entity.UserInfo;
import com.kakarote.core.exception.CrmException;
import com.kakarote.core.redis.Redis;
import com.kakarote.core.utils.UserUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AbstractAuthenticationToken;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import javax.servlet.http.HttpServletResponse;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

/**
 * @author Administrator
 */
@Service
public class UserDetailsServiceImpl implements UserDetailsService, LoginService {

    @Autowired
    private AdminUserService adminUserService;

    @Autowired
    private AuthenticationManager authenticationManager;

    @Autowired
    private Redis redis;


    @Override
    @SuppressWarnings("unchecked")
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        Result result = adminUserService.findByUsername(username);
        if (result.hasSuccess()) {
            return new AuthorizationUser().setUserInfoList((List<Object>) result.getData());
        }
        throw new UsernameNotFoundException(null);
    }

    /**
     * 登录方法的处理
     *
     * @param user 用户对象
     * @return Result
     */
    @Override
    public Result login(AuthorizationUser user,HttpServletResponse response) {
        String token = IdUtil.simpleUUID();
        UserInfo userInfo = user.toUserInfo();
        if(userInfo.getStatus() == 0){
            throw new CrmException(AuthorizationCodeEnum.AUTHORIZATION_USER_DISABLE_ERROR);
        }
        userInfo.setRoles(adminUserService.queryUserRoleIds(userInfo.getUserId()).getData());
        UserUtil.userToken(token, userInfo,user.getType());
        if (userInfo.getStatus() == 2){
            adminUserService.setUserStatus(AdminUserStatusBO.builder().status(1).ids(Collections.singletonList(userInfo.getUserId())).build());
        }
        return Result.ok(new LoginVO().setAdminToken(token));
    }

    /**
     * 登录方法的处理
     *
     * @param user 用户对象
     * @return Result
     */
    @Override
    public Result doLogin(AuthorizationUser user,HttpServletResponse response) {
        try {
            AbstractAuthenticationToken authenticationToken = new UsernamePasswordAuthenticationToken(user.getUsername().trim(), user.getPassword().trim());

            Authentication authentication = authenticationManager.authenticate(authenticationToken);
            AuthorizationUserInfo userInfo = (AuthorizationUserInfo) authentication.getDetails();
            if (userInfo.getAuthorizationUserList().size() == 0) {
                return Result.error(AuthorizationCodeEnum.AUTHORIZATION_LOGIN_NO_USER);
            }
            return login(userInfo.getAuthorizationUserList().get(0).setType(user.getType()),response);
        } catch (AuthException e) {
            return Result.error(e.getResultCode());
        } catch (BadCredentialsException e){
            return Result.error(AuthorizationCodeEnum.AUTHORIZATION_LOGIN_NO_USER);
        }

    }

    @Override
    public Result permission(String authentication, String url, String method) {
        UserInfo userInfo = redis.get(authentication);
        if (userInfo == null) {
            throw new CrmException(SystemCodeEnum.SYSTEM_NOT_LOGIN);
        }
        Long userId = userInfo.getUserId();
        String key = userId.toString();
        List<String> noAuthMenuUrls = redis.get(key);
        if (noAuthMenuUrls == null){
            noAuthMenuUrls = adminUserService.queryNoAuthMenu(userId).getData();
        }
        boolean permission =  isHasPermission(noAuthMenuUrls, url);
        return permission ? Result.ok() : Result.noAuth();
    }

    @Override
    public Result logout(String authentication) {
        Object data = redis.get(authentication);
        if(data instanceof UserInfo){
            UserInfo userInfo = (UserInfo) data;
            redis.del(authentication);
            redis.del(userInfo.getUserId());
        }
        return Result.ok();
    }



    /**
     * 判断有无权限访问
     *
     * @param noAuthMenuUrls
     * @param url
     * @return boolean
     * @date 2020/8/21 13:35
     **/
    private boolean isHasPermission(List<String> noAuthMenuUrls, String url) {
        //用户信息丢失 | 错误
        if (noAuthMenuUrls == null) {
            return false;
        }
        //管理员
        if (noAuthMenuUrls.size() == 0) {
            return true;
        }
        //没有任何权限
        if (noAuthMenuUrls.size() == 1 && "/*/**".equals(noAuthMenuUrls.get(0))) {
            return false;
        }
        boolean permission = true;
        for (String noAuthMenuUrl : noAuthMenuUrls) {
            if (noAuthMenuUrl.contains("*")) {
                if (noAuthMenuUrl.contains(",")) {
                    boolean isNoAuth = false;
                    for (String noAuthUrl : noAuthMenuUrl.split(",")) {
                        if (url.startsWith(noAuthUrl.replace("*", ""))) {
                            isNoAuth = true;
                            break;
                        }
                    }
                    if (isNoAuth) {
                        permission = false;
                        break;
                    }
                } else {
                    if (url.startsWith(noAuthMenuUrl.replace("*", ""))) {
                        permission = false;
                        break;
                    }
                }
            } else {
                if (noAuthMenuUrl.contains(",")) {
                    if (Arrays.asList(noAuthMenuUrl.split(",")).contains(url)) {
                        permission = false;
                        break;
                    }
                } else {
                    if (noAuthMenuUrl.equals(url)) {
                        permission = false;
                        break;
                    }
                }
            }
        }
        return permission;
    }
}
