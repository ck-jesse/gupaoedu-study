package com.coy.gupaoedu.study.spring.framework.beans;

/**
 * bean工厂：管理容器中存放的bean的工厂
 *
 * @author chenck
 * @date 2019/4/10 21:26
 */
public interface GPBeanFactory {

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
     * Register the given bean definition with the given bean factory
     */
    void registerBeanDefinition(String beanName, GPBeanDefinition beanDefinition);

    /**
     * Ensure that all non-lazy-init singletons are instantiated, also considering
     */
    void preInstantiateSingletons();

}
