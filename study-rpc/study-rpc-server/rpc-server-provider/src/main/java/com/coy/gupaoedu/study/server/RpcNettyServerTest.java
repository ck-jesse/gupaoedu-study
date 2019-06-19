package com.coy.gupaoedu.study.server;

import com.coy.gupaoedu.study.server.rpc.RpcInvoker;
import com.coy.gupaoedu.study.server.rpc.netty.RpcNettyServer;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;

/**
 * @author chenck
 * @date 2019/6/9 16:00
 */
@Configuration
@ComponentScan(basePackages = "com.coy.gupaoedu.study.server")
public class RpcNettyServerTest {

    /**
     * 实例化 RpcNettyServer
     */
    @Bean(name = "rpcNettyReistry")
    public RpcNettyServer rpcNettyReistry(RpcInvoker rpcInvoker) {
        return new RpcNettyServer(8080, rpcInvoker);
    }

    public static void main(String[] args) {
        AnnotationConfigApplicationContext applicationContext = new AnnotationConfigApplicationContext(RpcNettyServerTest.class);
        applicationContext.start();

        // 启动Netty服务监听
        RpcNettyServer rpcNettyServer = applicationContext.getBean(RpcNettyServer.class);
        rpcNettyServer.start();
    }
}
