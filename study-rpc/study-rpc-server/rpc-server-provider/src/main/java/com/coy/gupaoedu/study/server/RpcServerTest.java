package com.coy.gupaoedu.study.server;

import com.coy.gupaoedu.study.server.registry.ZookeeperRegistryCenter;
import com.coy.gupaoedu.study.server.rpc.RpcConfig;
import com.coy.gupaoedu.study.server.rpc.RpcConsts;
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
public class RpcServerTest {

    public static final int port = 8081;
    public static final String connectString = "127.0.0.1:2181";

    @Bean(name = "rpcConfig")
    public RpcConfig rpcConfig() {
//        return new RpcConfig(port, connectString, RpcConsts.BIO);
        return new RpcConfig(port, connectString, RpcConsts.NIO);
        //return new RpcConfig(port, connectString, RpcConsts.NETTY);
    }

    /**
     * 构造zookeeper注册中心
     */
    @Bean(name = "zookeeperRegistryCenter")
    public ZookeeperRegistryCenter zookeeperRegistryCenter(RpcConfig rpcConfig) {
        return new ZookeeperRegistryCenter(rpcConfig);
    }

    public static void main(String[] args) {
        URL url = RpcServerTest.class.getClassLoader().getResource("log4j.properties");
        PropertyConfigurator.configure(url.getPath());

        AnnotationConfigApplicationContext applicationContext = new AnnotationConfigApplicationContext(RpcServerTest.class);
        applicationContext.start();
    }
}
