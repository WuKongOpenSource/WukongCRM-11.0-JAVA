package com.kakarote.core.common.log;

import com.kakarote.core.common.SubModelType;

import java.lang.annotation.*;

/**
 * 系统日志注解
 * @author hmb
 */
@Retention(RetentionPolicy.RUNTIME)
@Target({ElementType.TYPE})
@Documented
public @interface SysLog {
    /**
     * 子模块名称
     */
    SubModelType subModel() default SubModelType.NULL;


    /**
     * 日志代理类
     */
    Class logClass() default void.class;
}
