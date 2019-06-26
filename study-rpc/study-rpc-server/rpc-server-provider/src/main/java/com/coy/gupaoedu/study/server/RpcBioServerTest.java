package com.coy.gupaoedu.study.server;

import com.coy.gupaoedu.study.server.rpc.RpcInvoker;
import com.coy.gupaoedu.study.server.rpc.bio.RpcBioServer;
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
public class RpcBioServerTest {

    /**
     * 实例化 RpcBioServer
     */
    @Bean(name = "rpcBioServer")
    public RpcBioServer rpcBioServer(RpcInvoker rpcInvoker) {
        return new RpcBioServer(8081, rpcInvoker);
    }

    public static void main(String[] args) {
        AnnotationConfigApplicationContext applicationContext = new AnnotationConfigApplicationContext(RpcBioServerTest.class);
        applicationContext.start();

        // 启动socket服务监听
        RpcBioServer rpcBioServer = applicationContext.getBean(RpcBioServer.class);
        rpcBioServer.start();
    }
}
