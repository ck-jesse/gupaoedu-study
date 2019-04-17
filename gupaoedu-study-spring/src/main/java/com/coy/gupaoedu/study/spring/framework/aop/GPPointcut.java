package com.coy.gupaoedu.study.spring.framework.aop;

/**
 * Core Spring pointcut abstraction
 * 切入点
 *
 * @author chenck
 * @date 2019/4/17 10:26
 */
public interface GPPointcut {

    /**
     * Return the ClassFilter for this pointcut.
     *
     * @return the ClassFilter (never {@code null})
     */
    GPClassFilter getClassFilter();

    /**
     * Return the MethodMatcher for this pointcut.
     */
    GPMethodMatcher getMethodMatcher();

    /**
     * Canonical Pointcut instance that always matches.
     */
    GPPointcut TRUE = GPTruePointcut.INSTANCE;
}
