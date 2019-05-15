package com.coy.gupaoedu.study.spring.framework.beans.factory.config;

/**
 * 初始化bean后置处理器
 *
 * @author chenck
 * @date 2019/4/14 21:27
 */
public interface GPBeanPostProcessor {

    /**
     * Apply this BeanPostProcessor to the given new bean instance <i>before</i> any bean
     * initialization callbacks
     * 为在Bean的初始化前提供回调入口
     */
    public Object postProcessBeforeInitialization(Object bean, String beanName);

    /**
     * Apply this BeanPostProcessor to the given new bean instance <i>after</i> any bean
     * initialization callbacks
     * 为在Bean的初始化之后提供回调入口
     */
    public Object postProcessAfterInitialization(Object bean, String beanName);
}
