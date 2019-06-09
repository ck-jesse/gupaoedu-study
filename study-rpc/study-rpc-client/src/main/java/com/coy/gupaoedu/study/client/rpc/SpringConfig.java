package com.coy.gupaoedu.study.client.rpc;

import com.coy.gupaoedu.study.server.facade.HelloServiceFacade;
import com.coy.gupaoedu.study.server.facade.PaymentServiceFacade;
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
@ComponentScan(basePackages = "com.coy.gupaoedu.study.client")
public class SpringConfig {

    private String host = "localhost";
    private int port = 8080;

    /**
     * 定义客户端代理bean
     */
    @Bean(name = "rpcPRoxyClient")
    public RpcProxyClient proxyClient() {
        return new RpcProxyClient();
    }

    /**
     * 定义远程服务的bean
     */
    @Bean(name = "helloServiceFacade")
    public RefereceBean<HelloServiceFacade> helloServiceFacade() {
        return new RefereceBean<HelloServiceFacade>(HelloServiceFacade.class, host, port);
    }

    /**
     * 定义远程服务的bean
     */
    @Bean(name = "paymentServiceFacade")
    public RefereceBean<PaymentServiceFacade> paymentServiceFacade() {
        return new RefereceBean<PaymentServiceFacade>(PaymentServiceFacade.class, host, port, "v1.0");
    }
}
