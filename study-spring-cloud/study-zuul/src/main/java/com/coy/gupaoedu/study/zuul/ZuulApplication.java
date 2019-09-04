package com.coy.gupaoedu.study.zuul;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;
import org.springframework.cloud.netflix.zuul.EnableZuulProxy;

/**
 * zuul 网关路由的demo
 * <p>
 * zuul 支持服务路由和服务过滤
 *
 * @author chenck
 * @date 2019/9/4 13:05
 */
@SpringBootApplication
@EnableZuulProxy// 开启zuul功能
//@EnableEurekaClient // 开启eureka服务注册功能
@EnableDiscoveryClient// 开启服务注册发现功能
public class ZuulApplication {

    /**
     * 从配置文件中读取路由规则进行路由
     * <p>
     * 路由到 nacos-consumer-service
     * http://localhost:8765/api-a/echo?name=lee
     * <p>
     * 路由到 nacos-provider-service
     * http://localhost:8765/api-b/echo?name=lee
     */
    public static void main(String[] args) {
        SpringApplication.run(ZuulApplication.class, args);
    }
}
