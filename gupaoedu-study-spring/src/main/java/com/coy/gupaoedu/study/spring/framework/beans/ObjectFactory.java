package com.coy.gupaoedu.study.spring.framework.beans;

import com.coy.gupaoedu.study.spring.framework.beans.exception.GPBeansException;

/**
 * Defines a factory which can return an Object instance
 *
 * @author chenck
 * @date 2019/4/16 10:54
 */
public interface ObjectFactory<T> {

    /**
     * Return an instance (possibly shared or independent) of the object managed by this factory.
     */
    T getObject() throws GPBeansException;
}
