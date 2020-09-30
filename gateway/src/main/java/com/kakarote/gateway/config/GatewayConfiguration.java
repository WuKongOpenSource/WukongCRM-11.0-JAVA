package com.kakarote.gateway.config;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.cloud.context.config.annotation.RefreshScope;
import org.springframework.stereotype.Component;

import java.util.List;

/**
 * @author zhangzhiwei
 * 默认的一些配置
 */
@Data
@Component
@RefreshScope
@ConfigurationProperties(prefix = "crm.gateway")
public class GatewayConfiguration {
    /**
     * 不验证权限的url
     */
    public List<String> ignoreUrl;


}
