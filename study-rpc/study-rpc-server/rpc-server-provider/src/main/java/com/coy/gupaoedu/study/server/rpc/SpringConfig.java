package com.coy.gupaoedu.study.server.rpc;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;

/**
 * 基于spring来配置启动server端
 *
 * @author chenck
 * @date 2019/6/9 15:57
 */
@Configuration
@ComponentScan(basePackages = "com.coy.gupaoedu.study.server")
public class SpringConfig {

    @Bean(name = "rpcServicePublisher")
    public RpcServicePublisher rpcServicePublisher() {
        return new RpcServicePublisher(8080);
    }
}
