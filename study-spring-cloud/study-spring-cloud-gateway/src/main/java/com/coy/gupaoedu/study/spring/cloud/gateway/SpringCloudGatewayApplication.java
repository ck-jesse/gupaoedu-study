package com.coy.gupaoedu.study.spring.cloud.gateway;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;
import org.springframework.cloud.gateway.route.RouteLocator;
import org.springframework.cloud.gateway.route.builder.RouteLocatorBuilder;
import org.springframework.cloud.netflix.eureka.EnableEurekaClient;
import org.springframework.context.ConfigurableApplicationContext;
import org.springframework.context.annotation.Bean;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import reactor.core.publisher.Mono;

/**
 * 结合如下工程，来对Spring Cloud Gateway如何配合服务注册中心进行路由转发进行验证测试
 * study-eureka-server 注册中心
 * study-eureka-provider 服务提供者
 * study-eureka-feign 服务提供者和服务消费者
 *
 * TODO 其中 Spring Cloud Gateway 怎么与dubbo结合起来？？ 可基于 Spring Cloud Alibaba 来进行结合。
 */
@SpringBootApplication
//@EnableEurekaClient// 开启服务注册功能 - 配合服务注册中心进行路由转发
@EnableDiscoveryClient // 开启服务发现功能
public class SpringCloudGatewayApplication {

    public static void main(String[] args) {
        // 通过切换 spring.profiles.active 来验证不同的场景
        // discovery -> 对应 application-discovery.yml，表示基于服务注册中心进行路由转发
        // after_route -> 对应
        ConfigurableApplicationContext applicationContext = SpringApplication.run(SpringCloudGatewayApplication.class, args);
        // TODO 此方式设置的激活的profiles不起作用，后续再研究
        applicationContext.getEnvironment().setActiveProfiles("after_route");
    }
}
