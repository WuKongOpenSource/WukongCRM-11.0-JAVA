package com.kakarote.examine;

import com.alicp.jetcache.anno.config.EnableCreateCacheAnnotation;
import com.alicp.jetcache.anno.config.EnableMethodCache;
import com.kakarote.core.CoreApplication;
import io.seata.spring.annotation.datasource.EnableAutoDataSourceProxy;
import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;
import org.springframework.cloud.openfeign.EnableFeignClients;
import org.springframework.context.annotation.ComponentScan;

/**
 * @author zhangzhiwei
 */
@SpringBootApplication
@EnableAutoDataSourceProxy
@EnableDiscoveryClient
@EnableFeignClients(basePackageClasses = {CoreApplication.class, ExamineApplication.class})
@ComponentScan(basePackageClasses = {CoreApplication.class, ExamineApplication.class})
@MapperScan(basePackages = "com.kakarote.examine.mapper")
@EnableMethodCache(basePackages = "com.kakarote.examine",order = -9999)
@EnableCreateCacheAnnotation
public class ExamineApplication {

	public static void main(String[] args) {
		System.setProperty("javax.xml.accessExternalSchema","all");
		System.setProperty("javax.xml.accessExternalDTD","all");
		SpringApplication.run(ExamineApplication.class, args);
	}
}
