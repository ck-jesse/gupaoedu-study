package com.coy.gupaoedu.study.client.service;

import com.coy.gupaoedu.study.server.facade.HelloServiceFacade;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;

/**
 * @author chenck
 * @date 2019/6/6 16:56
 */
@Component
public class HelloService {

    @Resource
    HelloServiceFacade helloServiceFacade;

    public String testHelloServiceFacade(String content){
        return helloServiceFacade.sayHello(content);
    }
}
