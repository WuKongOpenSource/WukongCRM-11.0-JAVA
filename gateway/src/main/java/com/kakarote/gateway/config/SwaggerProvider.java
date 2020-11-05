package com.kakarote.gateway.config;

import com.kakarote.gateway.service.GatewayRouteService;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Primary;
import org.springframework.stereotype.Component;
import org.springframework.web.reactive.config.WebFluxConfigurer;
import springfox.documentation.swagger.web.SwaggerResource;
import springfox.documentation.swagger.web.SwaggerResourcesProvider;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

/**
 * @author zhangzhiwei
 * swagger聚合
 */
@Component
@Primary
@AllArgsConstructor
@Slf4j
public class SwaggerProvider implements SwaggerResourcesProvider, WebFluxConfigurer {
    /**
     * swagger2默认的url后缀
     */
    public static final String API_URI = "/v2/api-docs";

    @Autowired
    private final GatewayRouteService gatewayRouteService;

    @Override
    public List<SwaggerResource> get() {
        List<SwaggerResource> resources = new ArrayList<>();
        gatewayRouteService.getRouteDefinitions()
                .forEach(routeDefinition -> {
                    if (!Objects.equals(GatewayConst.ROUTER_INTERCEPT_OK, routeDefinition.getMetadata().get(GatewayConst.ROUTER_INTERCEPT))) {
                        resources.add(swaggerResource(routeDefinition.getId(),"/"+routeDefinition.getId()+API_URI));
                    }
                });
        log.debug("resources:{}", resources);
        return resources;
    }
    private SwaggerResource swaggerResource(String name, String location) {
        SwaggerResource swaggerResource = new SwaggerResource();
        swaggerResource.setName(name);
        swaggerResource.setLocation(location);
        swaggerResource.setSwaggerVersion("2.0");
        return swaggerResource;
    }
}
