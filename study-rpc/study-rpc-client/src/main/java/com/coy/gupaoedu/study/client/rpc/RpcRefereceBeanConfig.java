package com.coy.gupaoedu.study.client.rpc;

import com.coy.gupaoedu.study.server.facade.HelloServiceFacade;
import com.coy.gupaoedu.study.server.facade.PaymentServiceFacade;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * 基于spring来配置启动server端
 *
 * @author chenck
 * @date 2019/6/9 15:57
 */
@Configuration
public class RpcRefereceBeanConfig {

    /**
     * 定义远程服务的bean
     *
     * TODO 可以通过自定义注解来进行RPC 服务Bean定义
     */
    @Bean(name = "helloServiceFacade")
    public RefereceBean<HelloServiceFacade> helloServiceFacade() {
        return new RefereceBean<HelloServiceFacade>(HelloServiceFacade.class);
    }

    /**
     * 定义远程服务的bean
     */
    @Bean(name = "paymentServiceFacade")
    public RefereceBean<PaymentServiceFacade> paymentServiceFacade() {
        return new RefereceBean<PaymentServiceFacade>(PaymentServiceFacade.class, "v1.0");
    }
}
