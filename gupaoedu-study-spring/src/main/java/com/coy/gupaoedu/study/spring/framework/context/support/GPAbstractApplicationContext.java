package com.coy.gupaoedu.study.spring.framework.context.support;

import com.coy.gupaoedu.study.spring.framework.beans.GPBeanDefinition;
import com.coy.gupaoedu.study.spring.framework.beans.GPBeanDefinitionReader;
import com.coy.gupaoedu.study.spring.framework.beans.GPBeanFactory;
import com.coy.gupaoedu.study.spring.framework.beans.support.GPDefaultListableBeanFactory;
import com.coy.gupaoedu.study.spring.framework.context.GPApplicationContext;

/**
 * 包装器模式：针对GPBeanFactory进行包装
 * 也可以理解为是一种静态代理模式，很类似
 *
 * @author chenck
 * @date 2019/4/10 21:35
 */
public class GPAbstractApplicationContext implements GPApplicationContext {

    private final GPDefaultListableBeanFactory beanFactory;

    public GPAbstractApplicationContext() {
        this.beanFactory = new GPDefaultListableBeanFactory();
    }

    //=====================================
    // Implements of GPBeanFactory
    //=====================================
    @Override
    public Object getBean(String beanName) {
        return this.getBeanFactory().getBean(beanName);
    }

    @Override
    public <T> T getBean(Class<T> beanClazz) {
        return this.getBeanFactory().getBean(beanClazz);
    }

    @Override
    public GPBeanDefinition getBeanDefinition(String beanName) {
        return this.getBeanFactory().getBeanDefinition(beanName);
    }

    @Override
    public void registerBeanDefinition(String beanName, GPBeanDefinition beanDefinition) {

    }

    //=====================================
    // Implements of GPApplicationContext
    //=====================================
    @Override
    public GPBeanFactory getBeanFactory() {
        return this.beanFactory;
    }

    @Override
    public void refresh() {
        // 1、定位，定位配置文件
        GPBeanDefinitionReader gpBeanDefinitionReader = new GPBeanDefinitionReader();

        // 2、加载配置文件，扫描相关的类，把他们封装为BeanDefinition

        // 3、注册，将加载的类注册到IOC容器

        // 4、依赖注入，把不是延迟加载的类，提前进行初始化（DI注入）

    }
}
