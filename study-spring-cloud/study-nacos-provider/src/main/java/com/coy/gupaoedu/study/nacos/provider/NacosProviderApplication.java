package com.coy.gupaoedu.study.nacos.provider;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;

/**
 * @author chenck
 * @date 2019/9/4 13:05
 */
@SpringBootApplication
@EnableDiscoveryClient// 开启服务注册发现功能
public class NacosProviderApplication {

    public static void main(String[] args) {
        SpringApplication.run(NacosProviderApplication.class, args);
    }
}
