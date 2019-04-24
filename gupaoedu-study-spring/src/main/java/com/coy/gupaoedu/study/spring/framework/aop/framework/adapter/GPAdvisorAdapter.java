package com.coy.gupaoedu.study.spring.framework.aop.framework.adapter;

import com.coy.gupaoedu.study.spring.framework.aop.GPAdvisor;
import com.coy.gupaoedu.study.spring.framework.aop.aopalliance.GPAdvice;
import com.coy.gupaoedu.study.spring.framework.aop.aopalliance.intercept.GPMethodInterceptor;

/**
 * @author chenck
 * @date 2019/4/17 15:53
 */
public interface GPAdvisorAdapter {
    /**
     * Does this adapter understand this advice object? Is it valid to
     * invoke the {@code getInterceptors} method with an Advisor that
     * contains this advice as an argument?
     */
    boolean supportsAdvice(GPAdvice advice);

    /**
     * Return an AOP Alliance MethodInterceptor exposing the behavior of
     * the given advice to an interception-based AOP framework.
     * <p>Don't worry about any Pointcut contained in thedvisor;
     * the AOP framework will take care of checking the pointcut.
     */
    GPMethodInterceptor getInterceptor(GPAdvisor advisor);
}
