package com.coy.gupaoedu.study.spring.framework.beans.factory.config;

import com.coy.gupaoedu.study.spring.framework.beans.exception.GPBeansException;

/**
 * 实例化的后置处理器
 *
 * @author chenck
 * @date 2019/4/16 18:53
 */
public interface GPInstantiationAwareBeanPostProcessor extends GPBeanPostProcessor {

    /**
     * 实例化前的处理
     */
    default Object postProcessBeforeInstantiation(Class<?> beanClass, String beanName) throws GPBeansException {
        return null;
    }

    /**
     * 实例化后的处理
     */
    default boolean postProcessAfterInstantiation(Object bean, String beanName) throws GPBeansException {
        return true;
    }

    /**
     * 获取对指定bean的早期访问的引用
     */
    default Object getEarlyBeanReference(Object bean, String beanName) throws GPBeansException {
        return bean;
    }
}
