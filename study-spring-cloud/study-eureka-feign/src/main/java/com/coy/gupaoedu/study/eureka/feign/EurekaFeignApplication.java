package com.coy.gupaoedu.study.eureka.feign;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;
import org.springframework.cloud.netflix.eureka.EnableEurekaClient;
import org.springframework.cloud.openfeign.EnableFeignClients;

/**
 * 服务消费方
 * 基于eureka注册中心的服务发现，并基于fegin来进行服务调用
 *
 * @author chenck
 * @date 2019/9/4 10:56
 */
@SpringBootApplication
@EnableEurekaClient// 开启服务注册功能
@EnableDiscoveryClient // 开启服务发现功能
@EnableFeignClients // 开启Feign功能
public class EurekaFeignApplication {

    public static void main(String[] args) {
        SpringApplication.run(EurekaFeignApplication.class, args);
    }
}
