package com.coy.gupaoedu.study.nacos.consumer;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;
import org.springframework.cloud.client.loadbalancer.LoadBalanced;
import org.springframework.cloud.netflix.hystrix.EnableHystrix;
import org.springframework.cloud.openfeign.EnableFeignClients;
import org.springframework.context.annotation.Bean;
import org.springframework.web.client.RestTemplate;

/**
 * Feign默认集成了ribbon
 * Spring cloud有两种服务调用方式，一种是ribbon+restTemplate，另一种是feign
 *
 * @author chenck
 * @date 2019/9/4 13:05
 */
@SpringBootApplication
@EnableDiscoveryClient// 开启服务注册发现功能
@EnableFeignClients // 开启Feign功能
@EnableHystrix// 开启Hystrix熔断器功能
public class NacosFeignApplication {

    public static void main(String[] args) {
        SpringApplication.run(NacosFeignApplication.class, args);
    }

    /**
     * 一种是ribbon+restTemplate实现服务调用
     * 注意：必须设置 @LoadBalanced 才能使用到ribbon的功能，会从注册中心获取服务，做负载均衡
     */
    @LoadBalanced
    @Bean
    public RestTemplate restTemplate() {
        return new RestTemplate();
    }
}
