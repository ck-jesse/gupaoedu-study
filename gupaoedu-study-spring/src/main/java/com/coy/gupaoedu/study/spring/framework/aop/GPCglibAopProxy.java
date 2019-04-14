package com.coy.gupaoedu.study.spring.framework.aop;

import java.io.Serializable;
import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Method;

/**
 * @author chenck
 * @date 2019/4/14 21:55
 */
public class GPCglibAopProxy implements GPAopProxy, InvocationHandler, Serializable {
    @Override
    public Object getProxy() {
        return null;
    }

    @Override
    public Object getProxy(ClassLoader classLoader) {
        return null;
    }

    @Override
    public Object invoke(Object proxy, Method method, Object[] args) throws Throwable {
        return null;
    }
}
