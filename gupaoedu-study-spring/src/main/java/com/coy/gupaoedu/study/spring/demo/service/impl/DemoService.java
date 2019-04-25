package com.coy.gupaoedu.study.spring.demo.service.impl;

import com.coy.gupaoedu.study.spring.demo.service.IDemoService;
import com.coy.gupaoedu.study.spring.framework.mvc.annotation.GPService;

/**
 * 核心业务逻辑
 * 用于测试基于接口的JDK代理
 */
@GPService
public class DemoService implements IDemoService {

    private String init;

    @Override
    public String get(String name) {
        return "My name is " + name;
    }

    @Override
    public void afterPropertiesSet() throws Exception {
        init = "通过GPInitializingBean来实现实例化后的初始化";
        System.out.println(init);
    }
}
