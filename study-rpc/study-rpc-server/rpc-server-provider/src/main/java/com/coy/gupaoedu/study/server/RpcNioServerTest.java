package com.coy.gupaoedu.study.server;

import com.coy.gupaoedu.study.server.registry.ZookeeperRegistryCenter;
import com.coy.gupaoedu.study.server.rpc.RpcInvoker;
import com.coy.gupaoedu.study.server.rpc.netty.RpcConfig;
import com.coy.gupaoedu.study.server.rpc.nio.RpcNioServer;
import org.apache.log4j.PropertyConfigurator;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;

import java.net.URL;

/**
 * @author chenck
 * @date 2019/6/9 16:00
 */
@Configuration
@ComponentScan(basePackages = "com.coy.gupaoedu.study.server")
public class RpcNioServerTest {

    public static final int port = 8081;
    public static final String connectString = "127.0.0.1:2181";

    @Bean(name = "rpcConfig")
    public RpcConfig rpcConfig() {
        return new RpcConfig(port, connectString);
    }

    /**
     * 实例化 RpcNioServer
     */
    @Bean(name = "rpcNioServer")
    public RpcNioServer rpcNioServer(RpcInvoker rpcInvoker, RpcConfig rpcConfig) {
        return new RpcNioServer(rpcConfig.getServerPort(), rpcInvoker);
    }

    /**
     * 构造zookeeper注册中心
     */
    @Bean(name = "zookeeperRegistryCenter")
    public ZookeeperRegistryCenter zookeeperRegistryCenter(RpcConfig rpcConfig) {
        return new ZookeeperRegistryCenter(rpcConfig);
    }

    public static void main(String[] args) {
        URL url = RpcNioServerTest.class.getClassLoader().getResource("log4j.properties");
        PropertyConfigurator.configure(url.getPath());

        AnnotationConfigApplicationContext applicationContext = new AnnotationConfigApplicationContext(RpcNioServerTest.class);
        applicationContext.start();

        // 启动socket服务监听
        RpcNioServer rpcNioServer = applicationContext.getBean(RpcNioServer.class);
        rpcNioServer.start();
    }
}
