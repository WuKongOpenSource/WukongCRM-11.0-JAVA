package com.kakarote.core.common.log;

import com.kakarote.core.common.SubModelType;

import java.lang.annotation.*;

/**
 * @author hmb
 */
@Retention(RetentionPolicy.RUNTIME)
@Target({ElementType.METHOD})
@Documented
public @interface SysLogHandler {

    /**
     * 模块名称
     */
    String applicationName() default "";

    /**
     * 子模块名称
     */
    SubModelType subModel() default SubModelType.NULL;


    /**
     * 操作
     */
    BehaviorEnum behavior() default BehaviorEnum.NULL;

    /**
     * 操作对象
     */
    String object() default "";
    /**
     * 操作详情
     */
    String detail() default "";

    /**
     * 默认为false
     * false 注解在Controller,需要自己重写操作日志逻辑,返回Content
     * true 注解在操作记录实现类,针对已经写过操作记录的方法,直接修改操作记录返回值为Content类型,切面直接获取返回值。
     */
    boolean isReturn() default false;

}
