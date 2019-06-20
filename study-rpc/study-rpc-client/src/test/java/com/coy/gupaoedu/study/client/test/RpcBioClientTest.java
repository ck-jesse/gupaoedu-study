package com.coy.gupaoedu.study.client.test;

import com.coy.gupaoedu.study.client.rpc.RpcNetTransport;
import com.coy.gupaoedu.study.client.rpc.RpcProxyClient;
import com.coy.gupaoedu.study.client.rpc.bio.RpcBioNetTransport;
import com.coy.gupaoedu.study.client.service.HelloService;
import com.coy.gupaoedu.study.server.facade.HelloServiceFacade;
import com.coy.gupaoedu.study.server.facade.PaymentServiceFacade;
import org.junit.Before;
import org.junit.Test;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;

/**
 * @author chenck
 * @date 2019/6/9 16:11
 */
@Configuration
@ComponentScan(basePackages = {"com.coy.gupaoedu.study.client.rpc", "com.coy.gupaoedu.study.client.service"})
public class RpcBioClientTest {

    private String host = "localhost";
    private int port = 8081;

    /**
     * 实例化 RpcNetTransport
     * 注意：此处定义很重要，是用来区分是基于netty还是基于bio
     */
    @Bean(name = "rpcBioNetTransport")
    public RpcNetTransport rpcBioNetTransport() {
        return new RpcBioNetTransport(host, port);
    }


    private ApplicationContext context;

    @Before
    public void before() {
        System.out.println("基于 BIO 的RPC远程服务调用");
        context = new AnnotationConfigApplicationContext(RpcBioClientTest.class);
    }

    @Test
    public void test() {
        RpcProxyClient rpcProxyClient = context.getBean(RpcProxyClient.class);

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
    public void facadeProxyTest() {
        HelloService helloService = context.getBean(HelloService.class);
        String result = helloService.testHelloServiceFacade(" coy testHelloServiceFacade");
        System.out.println(result);
    }

    /**
     * facade接口版本号测试
     */
    @Test
    public void versionTest() {
        PaymentServiceFacade paymentServiceFacade = context.getBean(PaymentServiceFacade.class);
        String result = paymentServiceFacade.pay(" payment wechat ");
        System.out.println(result);
    }
}
