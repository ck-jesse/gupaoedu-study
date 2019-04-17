package com.coy.gupaoedu.study.spring.framework.aop;

import com.sun.istack.internal.Nullable;

import java.lang.reflect.Method;

/**
 * Checks whether the target method is eligible for advice
 * 方法切入点匹配的筛选器
 *
 * @author chenck
 * @date 2019/4/17 10:30
 */
public interface GPMethodMatcher {

    /**
     * 切入点是否应应用于给定给定方法
     * Perform static checking whether the given method matches. If this
     * returns {@code false} or if the {@link #isRuntime()} method
     * returns {@code false}, no runtime check (i.e. no.
     * {@link #matches(java.lang.reflect.Method, Class, Object[])} call) will be made.
     *
     * @param method      the candidate method
     * @param targetClass the target class (may be {@code null}, in which case
     *                    the candidate class must be taken to be the method's declaring class)
     * @return whether or not this method matches statically
     */
    boolean matches(Method method, @Nullable Class<?> targetClass);

    /**
     * Is this MethodMatcher dynamic, that is, must a final call be made on the
     * {@link #matches(java.lang.reflect.Method, Class, Object[])} method at
     * runtime even if the 2-arg matches method returns {@code true}?
     * <p>Can be invoked when an AOP proxy is created, and need not be invoked
     * again before each method invocation,
     *
     * @return whether or not a runtime match via the 3-arg
     * {@link #matches(java.lang.reflect.Method, Class, Object[])} method
     * is required if static matching passed
     */
    boolean isRuntime();

    /**
     * 切入点是否应应用于给定给定方法
     * Check whether there a runtime (dynamic) match for this method,
     * which must have matched statically.
     */
    boolean matches(Method method, @Nullable Class<?> targetClass, Object... args);


    /**
     * Canonical instance that matches all methods.
     */
    GPMethodMatcher TRUE = GPTrueMethodMatcher.INSTANCE;
}
