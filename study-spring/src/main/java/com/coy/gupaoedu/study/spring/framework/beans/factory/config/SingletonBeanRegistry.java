package com.coy.gupaoedu.study.spring.framework.beans.factory.config;

/**
 * @author chenck
 * @date 2019/4/14 18:48
 */
public interface SingletonBeanRegistry {

    /**
     * Register the given existing object as singleton in the bean registry,under the given bean name
     * @param beanName 其值为beanClassName
     */
    void registerSingleton(String beanName, Object singletonObject);

    /**
     * Return the (raw) singleton object registered under the given name
     * @param beanName 其值为factoryBeanName或者beanClassName
     */
    Object getSingleton(String beanName);

    /**
     * Check if this registry contains a singleton instance with the given name
     * @param beanName 其值为factoryBeanName或者beanClassName
     */
    boolean containsSingleton(String beanName);

    /**
     * Return the names of singleton beans registered in this registry
     */
    String[] getSingletonNames();

    /**
     * Return the number of singleton beans registered in this registry
     */
    int getSingletonCount();

    /**
     * Return the singleton mutex used by this registry (for external collaborators).
     */
    Object getSingletonMutex();
}
