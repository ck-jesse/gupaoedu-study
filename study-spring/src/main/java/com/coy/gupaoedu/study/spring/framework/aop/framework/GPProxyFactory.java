package com.coy.gupaoedu.study.spring.framework.aop.framework;

import com.coy.gupaoedu.study.spring.framework.aop.aopalliance.intercept.GPInterceptor;
import com.coy.gupaoedu.study.spring.framework.core.util.Assert;
import com.coy.gupaoedu.study.spring.framework.core.util.ClassUtils;

/**
 * AOP代理工厂
 *
 * @author chenck
 * @date 2019/4/18 11:43
 */
public class GPProxyFactory extends GPAdvisedSupport {

    /**
     * AOP代理工厂
     */
    private GPAopProxyFactory aopProxyFactory;

    public GPProxyFactory() {
        this.aopProxyFactory = new GPAopProxyFactory();
    }

    public GPProxyFactory(GPAopProxyFactory aopProxyFactory) {
        Assert.notNull(aopProxyFactory, "AopProxyFactory must not be null");
        this.aopProxyFactory = aopProxyFactory;
    }

    /**
     * 创建一个ProxyFactory
     * 设置目标对象，同时获取目标对象实现的所有接口
     */
    public GPProxyFactory(Object target) {
        this();
        setTarget(target);
        setInterfaces(ClassUtils.getAllInterfaces(target));
    }

    /**
     * 创建一个ProxyFactory.
     * 无目标对象，仅仅接口集，必须添加拦截器
     */
    public GPProxyFactory(Class<?>... proxyInterfaces) {
        setInterfaces(proxyInterfaces);
    }

    /**
     * Create a new ProxyFactory for the given interface and interceptor
     * 创建一个ProxyFactory
     * 并设置指定的接口和拦截器
     */
    public GPProxyFactory(Class<?> proxyInterface, GPInterceptor interceptor) {
        addInterface(proxyInterface);
        addAdvice(interceptor);
    }

    /**
     * Customize the AopProxyFactory, allowing different strategies
     * to be dropped in without changing the core framework.
     */
    public void setAopProxyFactory(GPAopProxyFactory aopProxyFactory) {
        Assert.notNull(aopProxyFactory, "AopProxyFactory must not be null");
        this.aopProxyFactory = aopProxyFactory;
    }

    /**
     * Return the AopProxyFactory that this ProxyConfig uses.
     */
    public GPAopProxyFactory getAopProxyFactory() {
        return this.aopProxyFactory;
    }

    /**
     * 获取AopProxy，可用于创建具体的代理对象
     */
    protected final synchronized GPAopProxy createAopProxy() {
        return getAopProxyFactory().createAopProxy(this);
    }

    /**
     * 以设置的代理工厂创建一个新的代理对象
     *
     * @return the proxy object
     */
    public Object getProxy() {
        return createAopProxy().getProxy();
    }

    /**
     * 以设置的代理工厂创建一个新的代理对象
     */
    public Object getProxy(ClassLoader classLoader) {
        return createAopProxy().getProxy(classLoader);
    }

    /**
     * Create a new proxy for the given interface and interceptor.
     * 根据指定的接口和连接器创建代理对象
     *
     * @param proxyInterface the interface that the proxy should implement
     * @param interceptor    the interceptor that the proxy should invoke
     * @return the proxy object
     */
    @SuppressWarnings("unchecked")
    public static <T> T getProxy(Class<T> proxyInterface, GPInterceptor interceptor) {
        return (T) new GPProxyFactory(proxyInterface, interceptor).getProxy();
    }

}
