package com.kakarote.core.config;

import cn.hutool.core.util.StrUtil;
import com.alicp.jetcache.Cache;
import com.alicp.jetcache.anno.CreateCache;
import com.kakarote.core.common.Const;
import com.kakarote.core.entity.UserExtraInfo;
import com.kakarote.core.entity.UserInfo;
import com.kakarote.core.exception.NoLoginException;
import com.kakarote.core.feign.admin.service.AdminService;
import com.kakarote.core.redis.Redis;
import com.kakarote.core.servlet.LoginFromCookie;
import com.kakarote.core.utils.UserUtil;
import lombok.extern.slf4j.Slf4j;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.reflect.MethodSignature;
import org.springframework.aop.aspectj.MethodInvocationProceedingJoinPoint;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.Ordered;
import org.springframework.stereotype.Component;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import java.lang.reflect.Method;
import java.util.concurrent.TimeUnit;

/**
 * @author zhangzhiwei
 * user注入切面
 */
@Aspect
@Component
@Slf4j
public class ParamAspect implements Ordered {

    @Autowired
    private Redis redis;

    @CreateCache(name = Const.COMPANY_MANAGE_CACHE_NAME, expire = 3, timeUnit = TimeUnit.DAYS)
    private Cache<Long, Boolean> companyCache;

    @Autowired
    private AdminService adminService;

    @Around("(execution(* com.kakarote.*.controller..*(..))||execution(* com.kakarote.*.*.controller..*(..))) && execution(@(org.springframework.web.bind.annotation.*Mapping) * *(..))  && !execution(@(com.kakarote.core.common.ParamAspect) * *(..))")
    public Object around(ProceedingJoinPoint point) throws Throwable {
        ServletRequestAttributes attributes = ((ServletRequestAttributes) RequestContextHolder.getRequestAttributes());
        HttpServletRequest request = attributes.getRequest();
        try {
            String token = request.getHeader(Const.TOKEN_NAME);
            if (StrUtil.isEmpty(token)) {
                if (point instanceof MethodInvocationProceedingJoinPoint) {
                    Method targetMethod = ((MethodSignature) point.getSignature()).getMethod();
                    LoginFromCookie fromCookie = targetMethod.getAnnotation(LoginFromCookie.class);
                    if (fromCookie != null) {
                        for (Cookie cookie : request.getCookies()) {
                            if (Const.TOKEN_NAME.equals(cookie.getName())) {
                                token = cookie.getValue();
                                break;
                            }
                        }
                    }
                }
            }
            UserInfo info = null;
            if (StrUtil.isNotEmpty(token)) {
                Object data = redis.get(token);
                if (data instanceof UserExtraInfo) {
                    throw new NoLoginException((UserExtraInfo) data);
                } else if (data instanceof UserInfo) {
                    info = (UserInfo) data;
                }
                if (info != null) {
                    info.setRequest(request);
                    info.setResponse(attributes.getResponse());
                    UserUtil.setUser(info);
                    UserUtil.userExpire(token);
                }
            }
            if (info == null) {
                //TODO 未登录时有个空的user对象
                info = new UserInfo();
                info.setRequest(request);
                info.setResponse(attributes.getResponse());
                UserUtil.setUser(info);
            }
            return point.proceed();
        } finally {
            UserUtil.removeUser();
        }
    }

    @Override
    public int getOrder() {
        return 1;
    }
}
