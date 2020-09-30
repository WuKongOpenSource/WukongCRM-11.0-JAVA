package com.kakarote.gateway.mapper;

import com.kakarote.core.servlet.BaseMapper;
import com.kakarote.gateway.entity.GatewayRoute;
import org.apache.ibatis.annotations.Mapper;

@Mapper
public interface GatewayRouterMapper extends BaseMapper<GatewayRoute> {
}
