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
import com.kakarote.core.entity.UserInfo;
import com.kakarote.core.exception.CrmException;
import com.kakarote.core.redis.Redis;
import com.kakarote.core.utils.UserUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

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
    AuthenticationManager authenticationManager;

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
    public Result login(AuthorizationUser user) {
        String token = IdUtil.simpleUUID();
        UserInfo userInfo = user.toUserInfo();
        if(userInfo.getStatus() == 0){
            throw new CrmException(AuthorizationCodeEnum.AUTHORIZATION_USER_DISABLE_ERROR);
        }
        userInfo.setRoles(adminUserService.queryUserRoleIds(userInfo.getUserId()).getData());
        userInfo.setNoAuthMenuUrls(adminUserService.queryNoAuthMenu(userInfo.getUserId()).getData());
        UserUtil.userToken(token, userInfo);
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
    public Result doLogin(AuthorizationUser user) {
        try {
            Authentication authentication = authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(user.getUsername().trim(), user.getPassword().trim()));
            AuthorizationUserInfo userInfo = (AuthorizationUserInfo) authentication.getDetails();
            if (userInfo.getAuthorizationUserList().size() == 0) {
                return Result.error(AuthorizationCodeEnum.AUTHORIZATION_LOGIN_NO_USER);
            }
            return login(userInfo.getAuthorizationUserList().get(0));
        } catch (BadCredentialsException e){
            return Result.error(AuthorizationCodeEnum.AUTHORIZATION_LOGIN_NO_USER);
        } catch (AuthException e) {
            return Result.error(e.getResultCode());
        }

    }

    @Override
    public Result logout() {
        return Result.ok();
    }
}
