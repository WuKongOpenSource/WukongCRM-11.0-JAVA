package com.kakarote.authorization.common;

import cn.hutool.core.util.StrUtil;
import com.kakarote.authorization.entity.AuthorizationUser;
import com.kakarote.core.common.Const;
import com.kakarote.core.entity.UserInfo;
import com.kakarote.core.redis.Redis;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.Ordered;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.filter.OncePerRequestFilter;

import javax.annotation.Nonnull;
import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.ArrayList;

/**
 * @author zhangzhiwei
 * 默认的token处理类
 */
public class AuthenticationTokenFilter extends OncePerRequestFilter implements Ordered {


    @Autowired
    private Redis redis;


    @Override
    protected void doFilterInternal(@Nonnull HttpServletRequest request, @Nonnull HttpServletResponse response, @Nonnull FilterChain chain) throws ServletException, IOException {
        //获取header中的验证信息
        String token = request.getHeader(Const.TOKEN_NAME);
        logger.info(request.getRequestURI());
        response.setContentType(Const.DEFAULT_CONTENT_TYPE);
        if (StrUtil.isNotEmpty(token)) {
            Object user = redis.get(token);
            if(user instanceof UserInfo){
                AuthorizationUser authorizationUser = AuthorizationUser.toAuthorizationUser((UserInfo) user);
                UsernamePasswordAuthenticationToken authentication = new UsernamePasswordAuthenticationToken(authorizationUser, null, new ArrayList<>());
                SecurityContextHolder.getContext().setAuthentication(authentication);
            }
        }
        chain.doFilter(request, response);
    }

    @Override
    public int getOrder() {
        return -1;
    }
}
