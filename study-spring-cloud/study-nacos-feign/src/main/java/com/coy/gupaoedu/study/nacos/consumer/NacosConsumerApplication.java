package com.coy.gupaoedu.study.nacos.consumer;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;
import org.springframework.cloud.openfeign.EnableFeignClients;

/**
 * @author chenck
 * @date 2019/9/4 13:05
 */
@SpringBootApplication
@EnableDiscoveryClient// 开启服务注册发现功能
@EnableFeignClients // 开启Feign功能
public class NacosConsumerApplication {

    public static void main(String[] args) {
        SpringApplication.run(NacosConsumerApplication.class, args);
    }
}
