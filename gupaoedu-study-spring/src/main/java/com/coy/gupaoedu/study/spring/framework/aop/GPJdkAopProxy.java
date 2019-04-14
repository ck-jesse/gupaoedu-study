package com.coy.gupaoedu.study.spring.framework.aop;

import java.io.Serializable;
import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Method;
import java.lang.reflect.Proxy;

/**
 * @author chenck
 * @date 2019/4/14 21:54
 */
public class GPJdkAopProxy implements GPAopProxy, InvocationHandler, Serializable {

    /**
     * Config used to configure this proxy
     */
    private final GPAdvisedSupport advised;

    public GPJdkAopProxy(GPAdvisedSupport advisedSupport) {
        this.advised = advisedSupport;
    }

    @Override
    public Object getProxy() {
        return getProxy(this.advised.getTargetClass().getClassLoader());
    }

    @Override
    public Object getProxy(ClassLoader classLoader) {
        Class<?>[] proxiedInterfaces = this.advised.getTargetClass().getInterfaces();
        return Proxy.newProxyInstance(classLoader, proxiedInterfaces, this);
    }

    @Override
    public Object invoke(Object proxy, Method method, Object[] args) throws Throwable {
        return null;
    }
}
