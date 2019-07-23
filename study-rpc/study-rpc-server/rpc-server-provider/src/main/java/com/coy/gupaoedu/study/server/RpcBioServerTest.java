package com.coy.gupaoedu.study.server;

import com.coy.gupaoedu.study.server.registry.ZookeeperRegistryCenter;
import com.coy.gupaoedu.study.server.rpc.RpcInvoker;
import com.coy.gupaoedu.study.server.rpc.bio.RpcBioServer;
import com.coy.gupaoedu.study.server.rpc.netty.RpcConfig;
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

    public static final int port = 8081;
    public static final String connectString = "127.0.0.1:2181";

    @Bean(name = "rpcConfig")
    public RpcConfig rpcConfig() {
        return new RpcConfig(port, connectString);
    }

    /**
     * 实例化 RpcBioServer
     */
    @Bean(name = "rpcBioServer")
    public RpcBioServer rpcBioServer(RpcInvoker rpcInvoker, RpcConfig rpcConfig) {
        return new RpcBioServer(rpcConfig.getServerPort(), rpcInvoker);
    }

    /**
     * 构造zookeeper注册中心
     */
    @Bean(name = "zookeeperRegistryCenter")
    public ZookeeperRegistryCenter zookeeperRegistryCenter(RpcConfig rpcConfig) {
        return new ZookeeperRegistryCenter(rpcConfig);
    }

    public static void main(String[] args) {
        AnnotationConfigApplicationContext applicationContext = new AnnotationConfigApplicationContext(RpcBioServerTest.class);
        applicationContext.start();

        // 启动socket服务监听
        RpcBioServer rpcBioServer = applicationContext.getBean(RpcBioServer.class);
        rpcBioServer.start();
    }
}
