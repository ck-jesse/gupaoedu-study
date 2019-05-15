package com.coy.gupaoedu.study.spring.framework.beans;

import com.coy.gupaoedu.study.spring.framework.beans.exception.GPBeansException;

/**
 * @author chenck
 * @date 2019/4/16 20:05
 */
public interface GPBeanFactoryAware extends GPAware {

    /**
     * Callback that supplies the owning factory to a bean instance
     * 向bean实例提供所属工厂的回调
     */
    void setBeanFactory(GPBeanFactory beanFactory) throws GPBeansException;
}
