package com.kakarote.core.servlet;

import java.lang.annotation.*;

/**
 * @author zhangzhiwei
 * 尝试从cookie获取登录态
 */
@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.METHOD)
@Inherited
@Documented
public @interface LoginFromCookie {
}
