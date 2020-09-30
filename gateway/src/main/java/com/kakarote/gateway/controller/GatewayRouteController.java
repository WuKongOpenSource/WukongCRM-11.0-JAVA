package com.kakarote.gateway.controller;

import com.kakarote.core.common.Result;
import com.kakarote.gateway.service.GatewayRouteService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * @author Administrator
 */
@RestController
@RequestMapping("/gateway")
public class GatewayRouteController {

    @Autowired
    private GatewayRouteService gatewayRouteService;

    @RequestMapping("/")
    public Result index(){
        return Result.ok();
    }

    @RequestMapping("/reloadConfig")
    public Result reloadConfig(){
        gatewayRouteService.loadConfig();
        return Result.ok();
    }

}
