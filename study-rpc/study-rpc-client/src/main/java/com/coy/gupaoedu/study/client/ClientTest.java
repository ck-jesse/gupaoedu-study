package com.coy.gupaoedu.study.client;

import com.coy.gupaoedu.study.client.rpc.RpcProxyClient;
import com.coy.gupaoedu.study.client.rpc.SpringConfig;
import com.coy.gupaoedu.study.client.service.HelloService;
import com.coy.gupaoedu.study.server.facade.HelloServiceFacade;
import com.coy.gupaoedu.study.server.facade.PaymentServiceFacade;
import org.junit.Test;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;

/**
 * @author chenck
 * @date 2019/6/9 16:11
 */
public class ClientTest {

    public static void main(String[] args) {
        ApplicationContext context = new AnnotationConfigApplicationContext(SpringConfig.class);
        RpcProxyClient rpcProxyClient = context.getBean(RpcProxyClient.class);

        HelloServiceFacade helloServiceFacade = rpcProxyClient.clientProxy(HelloServiceFacade.class, "localhost", 8080);
        String result = helloServiceFacade.sayHello("coy");
        System.out.println(result);
    }

    /**
     * 对远程facade接口生成代理对象，并注入到普通bean中的测试
     */
    @Test
    public void facadeProxyTest(){
        ApplicationContext context = new AnnotationConfigApplicationContext(SpringConfig.class);
        HelloService helloService = context.getBean(HelloService.class);
        String result = helloService.testHelloServiceFacade(" coy testHelloServiceFacade");
        System.out.println(result);
    }

    /**
     * facade接口版本号测试
     */
    @Test
    public void versionTest(){
        ApplicationContext context = new AnnotationConfigApplicationContext(SpringConfig.class);
        PaymentServiceFacade paymentServiceFacade = context.getBean(PaymentServiceFacade.class);
        String result = paymentServiceFacade.pay(" payment wechat ");
        System.out.println(result);
    }
}
