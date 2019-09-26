package com.coy.gupaoedu.study.server;

import com.coy.gupaoedu.study.server.registry.ZookeeperRegistryCenter;
import com.coy.gupaoedu.study.server.rpc.RpcInvoker;
import com.coy.gupaoedu.study.server.rpc.netty.RpcConfig;
import com.coy.gupaoedu.study.server.rpc.netty.RpcNettyServer;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;

/**
 * Netty 服务端测试类
 *
 * @author chenck
 * @date 2019/6/9 16:00
 */
@Configuration
@ComponentScan(basePackages = "com.coy.gupaoedu.study.server")
public class RpcNettyServerTest {

    public static final int port = 8090;
    public static final String connectString = "127.0.0.1:2181";

    @Bean(name = "rpcConfig")
    public RpcConfig rpcConfig() {
        return new RpcConfig(port, connectString);
    }

    /**
     * 实例化 RpcNettyServer
     */
    @Bean(name = "rpcNettyReistry")
    public RpcNettyServer rpcNettyReistry(RpcInvoker rpcInvoker) {
        return new RpcNettyServer(port, rpcInvoker);
    }

    /**
     * 构造zookeeper注册中心实例
     * 注：基于zk实现分布式服务注册
     */
    @Bean(name = "zookeeperRegistryCenter")
    public ZookeeperRegistryCenter zookeeperRegistryCenter(RpcConfig rpcConfig) {
        return new ZookeeperRegistryCenter(rpcConfig);
    }

    /**
     * 对应的客户端测试类 com.coy.gupaoedu.study.client.test.RpcNettyClientTest
     */
    public static void main(String[] args) {
        AnnotationConfigApplicationContext applicationContext = new AnnotationConfigApplicationContext(RpcNettyServerTest.class);
        applicationContext.start();

        // 启动Netty服务监听
        RpcNettyServer rpcNettyServer = applicationContext.getBean(RpcNettyServer.class);
        rpcNettyServer.start();
    }
}
