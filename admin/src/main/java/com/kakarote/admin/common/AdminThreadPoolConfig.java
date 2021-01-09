package com.kakarote.admin.common;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.scheduling.concurrent.ThreadPoolTaskExecutor;

import java.util.concurrent.ThreadPoolExecutor;

@Configuration
public class AdminThreadPoolConfig {

    @Bean(name = "adminThreadPoolExecutor")
    public ThreadPoolTaskExecutor adminThreadPoolExecutor(){
        ThreadPoolTaskExecutor threadPoolTaskExecutor = new ThreadPoolTaskExecutor();
        threadPoolTaskExecutor.setCorePoolSize(20);
        threadPoolTaskExecutor.setMaxPoolSize(80);
        threadPoolTaskExecutor.setKeepAliveSeconds(30);
        threadPoolTaskExecutor.setQueueCapacity(200);
        threadPoolTaskExecutor.setThreadNamePrefix("ADMIN_");
        threadPoolTaskExecutor.setRejectedExecutionHandler(new ThreadPoolExecutor.CallerRunsPolicy());
        return threadPoolTaskExecutor;
    }
}
