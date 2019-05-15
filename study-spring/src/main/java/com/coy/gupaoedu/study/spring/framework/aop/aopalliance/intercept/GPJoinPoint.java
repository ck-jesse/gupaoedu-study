package com.coy.gupaoedu.study.spring.framework.aop.aopalliance.intercept;

import java.lang.reflect.AccessibleObject;

/**
 * @author chenck
 * @date 2019/4/16 20:40
 */
public interface GPJoinPoint {

    /**
     * 执行拦截器链式调用
     * Proceed to the next interceptor in the chain.
     * <p>The implementation and the semantics of this method depends
     * on the actual joinpoint type (see the children interfaces).
     *
     * @return see the children interfaces' proceed definition
     * @throws Throwable if the joinpoint throws an exception
     */
    Object proceed() throws Throwable;

    /**
     * 获取目标对象
     * Return the object that holds the current joinpoint's static part.
     * <p>For instance, the target object for an invocation.
     *
     * @return the object (can be null if the accessible object is static)
     */
    Object getThis();

    /**
     * Return the static part of this joinpoint.
     * <p>The static part is an accessible object on which a chain of
     * interceptors are installed.
     */
    AccessibleObject getStaticPart();
}
