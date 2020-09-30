package com.kakarote.core.common;

import java.lang.annotation.*;

/**
 * swagger不扫描的内部方法，方便后续扩展
 */
@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.METHOD)
@Inherited
@Documented
public @interface ApiExplain {
    String value();
}
