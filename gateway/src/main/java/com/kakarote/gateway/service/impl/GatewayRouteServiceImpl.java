package com.kakarote.gateway.service.impl;


import com.alibaba.fastjson.JSON;
import com.kakarote.core.servlet.BaseServiceImpl;
import com.kakarote.gateway.config.GatewayConst;
import com.kakarote.gateway.entity.GatewayRoute;
import com.kakarote.gateway.mapper.GatewayRouterMapper;
import com.kakarote.gateway.service.GatewayRouteService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.cloud.gateway.filter.FilterDefinition;
import org.springframework.cloud.gateway.handler.predicate.PredicateDefinition;
import org.springframework.cloud.gateway.route.RouteDefinition;
import org.springframework.stereotype.Service;

import javax.annotation.PostConstruct;
import java.net.URI;
import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

/**
 * @author zhangzhiwei
 * 网关的路由处理
 */
@Service
@Slf4j
public class GatewayRouteServiceImpl extends BaseServiceImpl<GatewayRouterMapper, GatewayRoute> implements GatewayRouteService {

    private final Map<String, RouteDefinition> ROUTES = new ConcurrentHashMap<>();

    /**
     * 删除路由
     *
     * @param routerId 路由ID
     */
    @Override
    public void removeRouter(String routerId) {
        ROUTES.remove(routerId);
    }

    /**
     * 新增路由
     *
     * @param routeDefinition 路由对象
     */
    @Override
    public void saveRouter(RouteDefinition routeDefinition) {
        ROUTES.put(routeDefinition.getId(), routeDefinition);
    }

    /**
     * 获取router列表
     */
    @Override
    public Collection<RouteDefinition> getRouteDefinitions() {
        return ROUTES.values();
    }

    /**
     * 从数据库重载配置
     */
    @Override
    @PostConstruct
    public void loadConfig() {
        List<GatewayRoute> routeList = list();
        routeList.forEach(router -> {
            log.debug("加载路由，路由id:{},路由uri:{},路由filters:{},路由predicates:{}", router.getRouteId(), router.getUri(), router.getFilters(), router.getPredicates());
            RouteDefinition routeDefinition = new RouteDefinition();
            routeDefinition.setId(router.getRouteId());
            routeDefinition.setOrder(router.getOrders());
            routeDefinition.setUri(URI.create(router.getUri()));
            routeDefinition.setFilters(JSON.parseArray(router.getFilters(), FilterDefinition.class));
            routeDefinition.setPredicates(JSON.parseArray(router.getPredicates(), PredicateDefinition.class));
            Map<String,Object> metadata = new HashMap<>(6,0.5f);
            metadata.put(GatewayConst.ROUTER_INTERCEPT,router.getIntercept());
            metadata.put(GatewayConst.ROUTER_DESC,router.getDescription());
            routeDefinition.setMetadata(metadata);
            ROUTES.put(routeDefinition.getId(), routeDefinition);
        });
    }
}
