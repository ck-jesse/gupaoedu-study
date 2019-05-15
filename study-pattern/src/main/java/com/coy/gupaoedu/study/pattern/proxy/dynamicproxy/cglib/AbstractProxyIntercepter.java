package com.coy.gupaoedu.study.pattern.proxy.dynamicproxy.cglib;

import net.sf.cglib.proxy.MethodInterceptor;
import net.sf.cglib.proxy.MethodProxy;

import java.lang.reflect.Method;

/**
 * 【CGLIB动态代理】抽象代理拦截器
 * 
 * @author chenck
 * @date 2017年3月15日 下午3:13:48
 */
public abstract class AbstractProxyIntercepter implements MethodInterceptor {

    /** 目标对象 */
    private Object targetObject;

    /*
     * <p>Description: </p>
     * 
     * @see net.sf.cglib.proxy.MethodInterceptor#intercept(java.lang.Object,
     * java.lang.reflect.Method, java.lang.Object[],
     * net.sf.cglib.proxy.MethodProxy)
     */
    @Override
    public Object intercept(Object proxyObj, Method method, Object[] args, MethodProxy proxy)
            throws Throwable {
        Object result = null;
        try {
            // 前置处理
            beforeInvoke(proxyObj, method, args, proxy);

            // 通过代理类调用父类中的方法
            result = proxy.invokeSuper(proxyObj, args);
            // 通过反射执行方法
            // result = method.invoke(targetObject, args);

            // 后置处理
            afterInvoke(proxyObj, method, args, proxy);
        } catch (Exception e) {
            throw e;
        }
        return result;
    }

    /** 目标对象方法执行前处理 */
    public abstract Object beforeInvoke(Object targetObject1, Method method, Object[] args,
            MethodProxy proxy);

    /** 目标对象方法执行后处理 */
    public abstract Object afterInvoke(Object targetObject1, Method method, Object[] args,
            MethodProxy proxy);

    public Object getTargetObject() {
        return targetObject;
    }

    public void setTargetObject(Object targetObject) {
        this.targetObject = targetObject;
    }

}
