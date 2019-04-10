package com.coy.gupaoedu.study.spring.framework.beans;

/**
 * bean工厂：作为容器存放
 *
 * @author chenck
 * @date 2019/4/10 21:26
 */
public interface GPBeanFactory {


    Object getBean(String beanName);

    <T> T getBean(Class<T> beanClazz);

    GPBeanDefinition getBeanDefinition(String beanName);
}
