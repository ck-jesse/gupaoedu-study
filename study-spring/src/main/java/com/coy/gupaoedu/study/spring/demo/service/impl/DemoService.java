package com.coy.gupaoedu.study.spring.demo.service.impl;

import com.coy.gupaoedu.study.spring.demo.service.IDemoService;
import com.coy.gupaoedu.study.spring.framework.beans.GPBeanFactory;
import com.coy.gupaoedu.study.spring.framework.beans.GPBeanFactoryAware;
import com.coy.gupaoedu.study.spring.framework.beans.exception.GPBeansException;
import com.coy.gupaoedu.study.spring.framework.beans.annotation.GPAutowired;
import com.coy.gupaoedu.study.spring.framework.context.annotation.GPService;

/**
 * 核心业务逻辑
 * 用于测试基于接口的JDK代理
 */
@GPService
public class DemoService implements IDemoService, GPBeanFactoryAware {

    private GPBeanFactory beanFactory;

    /**
     * 循环依赖的问题测试
     */
    @GPAutowired
    private UserService userService;

    /**
     *
     */
    private String init;

    @Override
    public String get(String name) {
        String result = "My name is " + name;
        System.out.println(result + " " + init);
        return result;
    }

    @Override
    public void afterPropertiesSet() throws Exception {
        init = "通过GPInitializingBean来实现实例化后的初始化";
        System.out.println(init);
    }

    @Override
    public void setBeanFactory(GPBeanFactory beanFactory) throws GPBeansException {
        this.beanFactory = beanFactory;
        System.out.println("DemoService 通过GPBeanFactory来实现实例化后的BeanFactory的注入");
    }
}
