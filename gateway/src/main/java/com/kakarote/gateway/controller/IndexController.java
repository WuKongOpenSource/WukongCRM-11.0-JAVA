package com.kakarote.gateway.controller;

import com.kakarote.core.common.Const;
import com.kakarote.core.common.Result;
import org.springframework.http.HttpStatus;
import org.springframework.http.server.reactive.ServerHttpResponse;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import reactor.core.publisher.Mono;

import java.net.URI;


/**
 * @author Administrator
 */
@RestController
public class IndexController {


    @RequestMapping("/")
    public Mono<Void> index(ServerHttpResponse response) {
        return Mono.fromRunnable(() -> {
            response.setStatusCode(HttpStatus.FOUND);
            response.getHeaders().setLocation(URI.create("./index.html"));
        });
    }

    @RequestMapping("/ping")
    public Result ping() {
        return Result.ok();
    }

    @RequestMapping("/version")
    public Result<String> version() {
        return Result.ok(Const.PROJECT_VERSION);
    }
}
