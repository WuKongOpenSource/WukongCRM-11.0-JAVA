package com.kakarote.core.common;

import java.lang.annotation.*;

/**
 * 拥有此注解的方法不经过切面扫描
 */
@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.METHOD)
@Inherited
@Documented
public @interface ParamAspect {
}
