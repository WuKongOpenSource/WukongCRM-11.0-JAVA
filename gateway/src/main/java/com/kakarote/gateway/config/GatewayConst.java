package com.kakarote.gateway.config;

import com.kakarote.core.common.Const;

/**
 * @author gateway网关的一些常量配置
 */
public class GatewayConst extends Const {

    /**
     * 路由是否要进行权限认证标识
     */
    public static final String ROUTER_INTERCEPT = "intercept";

    /**
     * 不需要进行权限认证标识
     */
    public static final Integer ROUTER_INTERCEPT_OK = 0;

    /**
     * 路由说明标识
     */
    public static final String ROUTER_DESC = "description";
}
