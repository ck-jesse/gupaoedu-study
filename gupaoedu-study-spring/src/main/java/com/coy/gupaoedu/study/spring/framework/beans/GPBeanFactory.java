package com.coy.gupaoedu.study.spring.framework.beans;

import com.coy.gupaoedu.study.spring.framework.beans.factory.config.GPBeanPostProcessor;

/**
 * bean工厂：管理容器中存放的bean的工厂
 *
 * @author chenck
 * @date 2019/4/10 21:26
 */
public interface GPBeanFactory {

    //对FactoryBean的转义定义，因为如果使用bean的名字检索FactoryBean得到的对象是工厂生成的对象，
    //如果需要得到工厂本身，需要转义
    String FACTORY_BEAN_PREFIX = "&";

    /**
     * Return the bean definition for the given bean name
     */
    GPBeanDefinition getBeanDefinition(String beanName);

    /**
     * Return the names of all beans defined in this factory
     */
    String[] getBeanDefinitionNames();

    /**
     * Return the number of beans defined in the factory
     */
    int getBeanDefinitionCount();

    /**
     * Check if this bean factory contains a bean definition with the given name
     */
    boolean containsBeanDefinition(String beanName);

    /**
     * Register the given bean definition with the given bean factory
     */
    void registerBeanDefinition(String beanName, GPBeanDefinition beanDefinition);

    /**
     * Ensure that all non-lazy-init singletons are instantiated, also considering
     */
    void preInstantiateSingletons();

    /**
     * Add a new BeanPostProcessor that will get applied to beans created
     * by this factory. To be invoked during factory configuration.
     *
     * @param beanPostProcessor the post-processor to register
     */
    void addBeanPostProcessor(GPBeanPostProcessor beanPostProcessor);

    /**
     * Return the current number of registered BeanPostProcessors, if any.
     */
    int getBeanPostProcessorCount();

    /**
     * 获取bean对象
     * Return an instance
     */
    Object getBean(String beanName);

    /**
     * 获取bean对象
     * Return an instance
     */
    <T> T getBean(Class<T> beanClazz);

    /**
     * Central method of this class: creates a bean instance
     * 创建一个指定Bean实例对象
     */
    Object createBean(String beanName, GPBeanDefinition mbd, Object[] args);

}
