package com.coy.gupaoedu.study.spring.framework.context.support;

import com.coy.gupaoedu.study.spring.framework.beans.GPBeanDefinition;
import com.coy.gupaoedu.study.spring.framework.beans.GPBeanDefinitionReader;
import com.coy.gupaoedu.study.spring.framework.beans.GPBeanFactory;
import com.coy.gupaoedu.study.spring.framework.beans.factory.config.GPBeanPostProcessor;
import com.coy.gupaoedu.study.spring.framework.beans.support.GPDefaultListableBeanFactory;
import com.coy.gupaoedu.study.spring.framework.context.GPApplicationContext;

import java.util.List;

/**
 * 包装器模式：针对GPBeanFactory进行包装
 * 也可以理解为是一种静态代理模式，很类似
 * <p>
 * 作用：按之前源码分析的套路，IOC、DI、MVC、AOP
 *
 * @author chenck
 * @date 2019/4/10 21:35
 */
public class GPAbstractApplicationContext implements GPApplicationContext {

    private String[] configLoactions;
    private GPBeanDefinitionReader beanDefinitionReader;
    private final GPDefaultListableBeanFactory beanFactory;


    public GPAbstractApplicationContext(String... configLoactions) {
        this.configLoactions = configLoactions;
        this.beanFactory = new GPDefaultListableBeanFactory();
        try {
            refresh();
        } catch (Exception e) {
            e.printStackTrace();
        }
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
    public boolean containsBean(String name) {
        return this.getBeanFactory().containsBean(name);
    }

    @Override
    public GPBeanDefinition getBeanDefinition(String beanName) {
        return this.getBeanFactory().getBeanDefinition(beanName);
    }

    @Override
    public String[] getBeanDefinitionNames() {
        return this.getBeanFactory().getBeanDefinitionNames();
    }

    @Override
    public int getBeanDefinitionCount() {
        return this.getBeanFactory().getBeanDefinitionCount();
    }

    @Override
    public boolean containsBeanDefinition(String beanName) {
        return this.getBeanFactory().containsBeanDefinition(beanName);
    }

    @Override
    public void registerBeanDefinition(String beanName, GPBeanDefinition beanDefinition) {
        this.getBeanFactory().registerBeanDefinition(beanName, beanDefinition);
    }

    /**
     * 往容器中注册BeanDefinition
     */
    protected void registerBeanDefinition(List<GPBeanDefinition> beanDefinitions) {
        for (GPBeanDefinition bd : beanDefinitions) {
            registerBeanDefinition(bd.getFactoryBeanName(), bd);
        }
    }

    @Override
    public void preInstantiateSingletons() {
        this.getBeanFactory().preInstantiateSingletons();
    }

    @Override
    public void addBeanPostProcessor(GPBeanPostProcessor beanPostProcessor) {
        this.getBeanFactory().addBeanPostProcessor(beanPostProcessor);
    }

    @Override
    public int getBeanPostProcessorCount() {
        return this.getBeanFactory().getBeanPostProcessorCount();
    }

    @Override
    public Object createBean(String beanName, GPBeanDefinition mbd, Object[] args) {
        return this.getBeanFactory().createBean(beanName, mbd, args);
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
        beanDefinitionReader = new GPBeanDefinitionReader(this.configLoactions);

        // 2、加载配置文件，扫描相关的类，把他们封装为BeanDefinition
        List<GPBeanDefinition> beanDefinitions = beanDefinitionReader.loadBeanDefinitions();

        // 3、注册，将加载的类配置信息注册到IOC容器
        registerBeanDefinition(beanDefinitions);
        // 到这里为止，容器初始化完毕

        // TODO
        // 初始化所有的BeanPostProcessor
        // 为BeanFactory注册BeanPostProcessor后置处理器，用于监听容器触发的事件
        // 注：后置处理器中包含：aop的AdvisorAdapter注册器
        // registerBeanPostProcessors(beanFactory);

        // 5、初始化单利bean，并完成对应依赖注入（DI注入）
        preInstantiateSingletons();
    }
}