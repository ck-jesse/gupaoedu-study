package com.coy.gupaoedu.study.eureka.provider;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.netflix.eureka.EnableEurekaClient;

/**
 * 启动一个eureka provider，往eureka注册中心注册服务
 * <p>
 * 疑问：定义了 @EnableEurekaClient 就会将Controller的方法暴露为服务吗？
 *
 * @author chenck
 * @date 2019/9/4 10:56
 */
@SpringBootApplication
@EnableEurekaClient// 开启服务注册功能
public class EurekaProviderApplication {

    public static void main(String[] args) {
        SpringApplication.run(EurekaProviderApplication.class, args);
    }
}
