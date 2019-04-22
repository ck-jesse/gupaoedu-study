package com.coy.gupaoedu.study.spring.framework.context.support;

import com.coy.gupaoedu.study.spring.framework.beans.GPBeanDefinition;
import com.coy.gupaoedu.study.spring.framework.beans.GPBeanDefinitionReader;
import com.coy.gupaoedu.study.spring.framework.beans.GPBeanFactory;
import com.coy.gupaoedu.study.spring.framework.beans.factory.config.GPBeanPostProcessor;
import com.coy.gupaoedu.study.spring.framework.beans.support.GPDefaultListableBeanFactory;
import com.coy.gupaoedu.study.spring.framework.context.GPApplicationContext;
import com.coy.gupaoedu.study.spring.framework.context.PropertiesUtils;
import com.coy.gupaoedu.study.spring.framework.core.GPOrderComparator;
import com.coy.gupaoedu.study.spring.framework.core.GPOrdered;
import com.coy.gupaoedu.study.spring.framework.core.GPPriorityOrdered;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import java.util.ArrayList;
import java.util.Collections;
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

    private static final Log logger = LogFactory.getLog(GPAbstractApplicationContext.class);

    /**
     * 配置
     */
    private String[] configLoactions;
    /**
     * bean定义读取
     */
    private GPBeanDefinitionReader beanDefinitionReader;
    /**
     * bean工厂
     */
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
    public <T> T getBean(String beanName, Class<T> requiredType) {
        return this.getBeanFactory().getBean(beanName, requiredType);
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

    @Override
    public String[] getBeanNamesForType(Class<?> type) {
        return this.getBeanFactory().getBeanNamesForType(type);
    }

    @Override
    public String[] getBeanNamesForType(Class<?> type, boolean includeNonSingletons) {
        return this.getBeanFactory().getBeanNamesForType(type, includeNonSingletons);
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

    @Override
    public boolean isTypeMatch(String beanName, Class<?> typeToMatch) {
        return this.getBeanFactory().isTypeMatch(beanName, typeToMatch);
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

        // 加载属性文件
        PropertiesUtils.load(configLoactions);

        // 1、定位，定位配置文件
        beanDefinitionReader = new GPBeanDefinitionReader();

        // 2、加载配置文件，扫描相关的类，把他们封装为BeanDefinition
        List<GPBeanDefinition> beanDefinitions = beanDefinitionReader.loadBeanDefinitions();

        // 3、注册，将加载的类配置信息注册到IOC容器
        registerBeanDefinition(beanDefinitions);
        // 到这里为止，容器初始化完毕

        // 为BeanFactory注册所有实现了BeanPostProcessor的后置处理器
        // 注：后置处理器中包含：初始化aop的AdvisorAdapter注册器
        registerBeanPostProcessors();

        // 初始化容器事件传播器.
        // initApplicationEventMulticaster();

        // 5、初始化单利bean，并完成对应依赖注入（DI注入）
        preInstantiateSingletons();
    }

    /**
     * 实例化并注册所有的 BeanPostProcessor beans
     */
    protected void registerBeanPostProcessors() {
        // 获取实现了BeanPostProcessor的所有类
        String[] postProcessorNames = this.getBeanNamesForType(GPBeanPostProcessor.class, true);
        if (null == postProcessorNames || postProcessorNames.length == 0) {
            return;
        }

        // 区分不同的优先级
        List<GPBeanPostProcessor> priorityOrderedPostProcessors = new ArrayList<>();
        List<GPBeanPostProcessor> orderedPostProcessors = new ArrayList<>();
        List<GPBeanPostProcessor> nonOrderedPostProcessors = new ArrayList<>();

        for (String ppName : postProcessorNames) {
            // 此处会去创建bean
            GPBeanPostProcessor pp = beanFactory.getBean(ppName, GPBeanPostProcessor.class);
            if (beanFactory.isTypeMatch(ppName, GPPriorityOrdered.class)) {
                priorityOrderedPostProcessors.add(pp);
            } else if (beanFactory.isTypeMatch(ppName, GPOrdered.class)) {
                orderedPostProcessors.add(pp);
            } else {
                nonOrderedPostProcessors.add(pp);
            }
        }

        // 先注册实现了PriorityOrdered的BeanPostProcessor
        Collections.sort(priorityOrderedPostProcessors, GPOrderComparator.INSTANCE);
        registerBeanPostProcessors(priorityOrderedPostProcessors);

        // 再注册实现了Ordered的BeanPostProcessor
        Collections.sort(orderedPostProcessors, GPOrderComparator.INSTANCE);
        registerBeanPostProcessors(orderedPostProcessors);

        // 再注册实现了Ordered的BeanPostProcessor
        registerBeanPostProcessors(nonOrderedPostProcessors);
    }

    /**
     * Register the given BeanPostProcessor beans.
     */
    private void registerBeanPostProcessors(List<GPBeanPostProcessor> postProcessors) {
        for (GPBeanPostProcessor postProcessor : postProcessors) {
            beanFactory.addBeanPostProcessor(postProcessor);
        }
    }


}