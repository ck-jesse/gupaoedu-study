package com.coy.gupaoedu.study.spring.framework.beans.factory.config;

/**
 * @author chenck
 * @date 2019/4/14 21:27
 */
public class GPBeanPostProcessor {
    public Object postProcessBeforeInitialization(Object bean, String beanName) {
        return bean;
    }

    public Object postProcessAfterInitialization(Object bean, String beanName) {
        return bean;
    }
}
