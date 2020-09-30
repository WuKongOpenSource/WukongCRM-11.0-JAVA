package com.kakarote.authorization.common;

import com.kakarote.core.common.Const;
import com.kakarote.core.common.Result;
import org.springframework.core.Ordered;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.AuthenticationEntryPoint;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.PrintWriter;

/**
 * @author z
 * 未登录拦截
 */
public class AuthorizedFilter implements AuthenticationEntryPoint, Ordered {


    @Override
    public void commence(HttpServletRequest request, HttpServletResponse response, AuthenticationException authException) throws IOException, ServletException {
        Result result = Result.error(AuthorizationCodeEnum.AUTHORIZATION_LOGIN);
        response.setContentType(Const.DEFAULT_CONTENT_TYPE);
        PrintWriter out = response.getWriter();
        out.write(result.toJSONString());
        out.flush();
        out.close();
    }

    @Override
    public int getOrder() {
        return 9999;
    }
}
