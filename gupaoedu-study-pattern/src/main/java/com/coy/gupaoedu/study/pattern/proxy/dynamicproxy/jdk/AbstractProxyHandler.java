package com.coy.gupaoedu.study.pattern.proxy.dynamicproxy.jdk;

import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Method;

/**
 * 【JDK动态代理】抽象代理处理器
 * 
 * @author chenck
 * @date 2017年3月15日 下午2:34:29
 */
public abstract class AbstractProxyHandler implements InvocationHandler {

    /** 目标对象 */
    private Object targetObject;

    /*
     * <p>Description: </p>
     * 
     * @see java.lang.reflect.InvocationHandler#invoke(java.lang.Object,
     * java.lang.reflect.Method, java.lang.Object[])
     */
    @Override
    public Object invoke(Object proxy, Method method, Object[] args) throws Throwable {
        Object result = null;
        try {
            // 前置处理
            beforeInvoke(proxy, method, args);

            // 执行目标对象方法
            result = method.invoke(targetObject, args);

            // 后置处理
            afterInvoke(proxy, method, args);
        } catch (Exception e) {
            throw e;
        }
        return result;
    }

    /** 目标对象方法执行前处理 */
    public abstract Object beforeInvoke(Object proxy, Method method, Object[] args);

    /** 目标对象方法执行后处理 */
    public abstract Object afterInvoke(Object proxy, Method method, Object[] args);

    public Object getTargetObject() {
        return targetObject;
    }

    public void setTargetObject(Object targetObject) {
        this.targetObject = targetObject;
    }

}
