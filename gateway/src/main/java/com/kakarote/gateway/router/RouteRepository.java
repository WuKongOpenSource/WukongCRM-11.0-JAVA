package com.kakarote.gateway.router;

import com.kakarote.gateway.service.GatewayRouteService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cloud.gateway.route.RouteDefinition;
import org.springframework.cloud.gateway.route.RouteDefinitionRepository;
import org.springframework.stereotype.Component;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

/**
 * @author zhangzhiwei
 * 路由配置工厂
 */
@Component
public class RouteRepository implements RouteDefinitionRepository {

    @Autowired
    private GatewayRouteService gatewayRouteService;



    @Override
    public Flux<RouteDefinition> getRouteDefinitions() {
        return Flux.fromIterable(gatewayRouteService.getRouteDefinitions());
    }

    @Override
    public Mono<Void> save(Mono<RouteDefinition> route) {
        return route.flatMap(routeDefinition -> {
            gatewayRouteService.saveRouter(routeDefinition);
            return Mono.empty();
        });
    }

    @Override
    public Mono<Void> delete(Mono<String> routeId) {
        return routeId.flatMap(id -> {
            gatewayRouteService.removeRouter(id);
            return Mono.empty();
        });
    }
}
