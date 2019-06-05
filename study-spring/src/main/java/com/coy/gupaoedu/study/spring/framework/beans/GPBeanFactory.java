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
     * 获取与指定Class类型匹配的bean的名称
     */
    String[] getBeanNamesForType(Class<?> type);

    /**
     * 获取与指定Class类型匹配的bean的名称
     *
     * @param includeNonSingletons 是否包含非单例
     */
    String[] getBeanNamesForType(Class<?> type, boolean includeNonSingletons);

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
     * 根据bean的名字和Class类型来得到bean实例，增加了类型安全验证机制。
     */
    <T> T getBean(String beanName, Class<T> requiredType);

    /**
     * 提供对bean的检索，看看是否在IOC容器有这个名字的bean
     */
    boolean containsBean(String name);

    /**
     * Central method of this class: creates a bean instance
     * 创建一个指定Bean实例对象
     */
    Object createBean(String beanName, GPBeanDefinition mbd, Object[] args);

    /**
     * 根据bean名字得到bean实例，并同时判断这个bean是不是单例
     */
    boolean isSingleton(String beanName);

    /**
     * 根据bean名字得到bean实例，并同时判断这个bean是不是单例
     */
    boolean isPrototype(String beanName);

    /**
     * 检查具有给定名称的bean是否与指定类型匹配
     * 注：包含了检查typeToMatch是否为自定义FactoryBean中的type类型，这点很重要
     */
    boolean isTypeMatch(String beanName, Class<?> typeToMatch);

    /**
     * 得到bean实例的Class类型
     */
    Class<?> getType(String beanName);
}
