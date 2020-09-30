package com.kakarote.gateway.config;

import com.netflix.loadbalancer.ILoadBalancer;
import com.netflix.loadbalancer.RoundRobinRule;
import com.netflix.loadbalancer.Server;
import org.springframework.context.annotation.Primary;

import java.util.List;
import java.util.Objects;

@Primary
public class LBConfigRule extends RoundRobinRule {

    @Override
    public Server choose(ILoadBalancer lb, Object key) {
        if (lb == null) {
            return null;
        }

        String ip = GatewayFilter.ip.get();
        List<Server> upList = lb.getReachableServers();
        if (upList.size() > 1) {
            for (Server server : upList) {
                if (Objects.equals(ip, server.getHost())) {
                    return server;
                }
            }
        }
        GatewayFilter.ip.remove();
        return super.choose(lb, key);
    }
}
