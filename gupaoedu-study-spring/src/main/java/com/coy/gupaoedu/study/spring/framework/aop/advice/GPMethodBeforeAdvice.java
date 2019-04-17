package com.coy.gupaoedu.study.spring.framework.aop.advice;

import java.lang.reflect.Method;

/**
 * Advice invoked before a method is invoked
 *
 * @author chenck
 * @date 2019/4/17 10:28
 */
public interface GPMethodBeforeAdvice extends GPBeforeAdvice {

    /**
     * Callback before a given method is invoked.
     *
     * @param method method being invoked
     * @param args   arguments to the method
     * @param target target of the method invocation. May be {@code null}.
     * @throws Throwable if this object wishes to abort the call.
     *                   Any exception thrown will be returned to the caller if it's
     *                   allowed by the method signature. Otherwise the exception
     *                   will be wrapped as a runtime exception.
     */
    void before(Method method, Object[] args, Object target) throws Throwable;
}
