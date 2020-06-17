package com.coy.gupaoedu.study.client.test;

import com.coy.gupaoedu.study.client.rpc.RpcProxyClient;
import com.coy.gupaoedu.study.client.rpc.discovery.ZookeeperServiceDiscovery;
import com.coy.gupaoedu.study.client.rpc.transport.RpcNetTransport;
import com.coy.gupaoedu.study.client.rpc.transport.bio.RpcBioNetTransport;
import com.coy.gupaoedu.study.client.rpc.transport.netty.RpcNettyNetTransport;
import com.coy.gupaoedu.study.client.rpc.transport.nio.RpcNioNetTransport;
import com.coy.gupaoedu.study.client.service.HelloService;
import com.coy.gupaoedu.study.server.facade.HelloServiceFacade;
import com.coy.gupaoedu.study.server.facade.PaymentServiceFacade;
import com.coy.gupaoedu.study.server.rpc.RpcConfig;
import com.coy.gupaoedu.study.server.rpc.RpcConsts;
import org.apache.log4j.PropertyConfigurator;
import org.junit.Before;
import org.junit.Test;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;

import java.net.URL;

/**
 * RPC client 测试类
 *
 * @author chenck
 * @date 2019/6/9 16:11
 */
@Configuration
@ComponentScan(basePackages = {"com.coy.gupaoedu.study.client.rpc", "com.coy.gupaoedu.study.client.service"})
public class RpcClientTest {

    public static final String connectString = "127.0.0.1:2181";

    @Bean(name = "rpcConfig")
    public RpcConfig rpcConfig() {
//        return new RpcConfig(0, connectString, RpcConsts.BIO);
        return new RpcConfig(0, connectString, RpcConsts.NIO);
        //return new RpcConfig(0, connectString, RpcConsts.NETTY);
    }

    /**
     * 实例化 RpcNetTransport
     * 注意：此处定义很重要，是用来区分是基于netty还是基于bio
     */
    @Bean(name = "rpcBioNetTransport")
    public RpcNetTransport rpcBioNetTransport(RpcConfig rpcConfig) {
        if (RpcConsts.BIO.equalsIgnoreCase(rpcConfig.getRpcServerType())) {
            System.out.println("基于 BIO 的RPC远程服务调用");
            return new RpcBioNetTransport();
        } else if (RpcConsts.NIO.equalsIgnoreCase(rpcConfig.getRpcServerType())) {
            System.out.println("基于 NIO 的RPC远程服务调用");
            return new RpcNioNetTransport();
        } else if (RpcConsts.NETTY.equalsIgnoreCase(rpcConfig.getRpcServerType())) {
            System.out.println("基于 Netty 的RPC远程服务调用");
            return new RpcNettyNetTransport();
        }
        throw new IllegalArgumentException("暂不支持该RPC通信类型" + rpcConfig.getRpcServerType());
    }

    /**
     * 服务发现
     * <p>
     * 注：基于zk实现分布式服务发现1
     */
    @Bean(name = "zookeeperServiceDiscovery")
    public ZookeeperServiceDiscovery zookeeperServiceDiscovery(RpcConfig rpcConfig) {
        return new ZookeeperServiceDiscovery(rpcConfig.getZkConnectString());
    }

    private AnnotationConfigApplicationContext context;

    @Before
    public void before() {
        // log4j 加载
        URL url = RpcClientTest.class.getClassLoader().getResource("log4j.properties");
        PropertyConfigurator.configure(url.getPath());

        context = new AnnotationConfigApplicationContext(RpcClientTest.class);
    }

    @Test
    public void test() throws Exception {
        RpcProxyClient rpcProxyClient = context.getBean(RpcProxyClient.class);

        Thread.sleep(1000);// 休眠1s，等待从注册中心发现服务，再进行服务调用
        // 通过RpcProxyClient创建rpc服务代理对象
        HelloServiceFacade helloServiceFacade = rpcProxyClient.clientProxy(HelloServiceFacade.class);
        System.out.println(helloServiceFacade.sayHello("coy"));

        // 通过RefereceBean定义来创建rpc服务代理对象
        //PaymentServiceFacade paymentServiceFacade = context.getBean(PaymentServiceFacade.class);
        //System.out.println(paymentServiceFacade.pay(" payment wechat "));
    }

    /**
     * 对远程facade接口生成代理对象，并注入到普通bean中的测试
     */
    @Test
    public void facadeProxyTest() throws InterruptedException {
        HelloService helloService = context.getBean(HelloService.class);
        Thread.sleep(1000);
        String result = helloService.testHelloServiceFacade(" coy testHelloServiceFacade");
        System.out.println(result);
    }

    /**
     * facade接口版本号测试
     */
    @Test
    public void versionTest() throws InterruptedException {
        PaymentServiceFacade paymentServiceFacade = context.getBean(PaymentServiceFacade.class);
        Thread.sleep(1000);
        String result = paymentServiceFacade.pay(" payment wechat ");
        System.out.println(result);
    }
}
