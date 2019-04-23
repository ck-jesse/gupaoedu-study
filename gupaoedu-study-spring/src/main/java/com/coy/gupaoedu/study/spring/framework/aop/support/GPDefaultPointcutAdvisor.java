package com.coy.gupaoedu.study.spring.framework.aop.support;

import com.coy.gupaoedu.study.spring.framework.aop.support.matcher.GPPointcut;
import com.coy.gupaoedu.study.spring.framework.aop.aopalliance.aop.GPAdvice;

import java.io.Serializable;

/**
 * 默认的切入点Advisor实现
 *
 * @author chenck
 * @date 2019/4/17 12:50
 */
public class GPDefaultPointcutAdvisor extends GPAbstractGenericPointcutAdvisor implements Serializable {

    /**
     * 切入点
     */
    private GPPointcut pointcut;

    public GPDefaultPointcutAdvisor() {
    }

    /**
     * Create a DefaultPointcutAdvisor that matches all methods.
     * <p>{@code Pointcut.TRUE} will be used as Pointcut.
     *
     * @param advice the Advice to use
     */
    public GPDefaultPointcutAdvisor(GPAdvice advice) {
        this(GPPointcut.TRUE, advice);
    }

    /**
     * Create a DefaultPointcutAdvisor, specifying Pointcut and Advice.
     *
     * @param pointcut the Pointcut targeting the Advice
     * @param advice   the Advice to run when Pointcut matches
     */
    public GPDefaultPointcutAdvisor(GPPointcut pointcut, GPAdvice advice) {
        this.pointcut = pointcut;
        setAdvice(advice);
    }


    /**
     * Specify the pointcut targeting the advice.
     * <p>Default is {@code Pointcut.TRUE}.
     *
     * @see #setAdvice
     */
    public void setPointcut(GPPointcut pointcut) {
        this.pointcut = (pointcut != null ? pointcut : GPPointcut.TRUE);
    }

    @Override
    public GPPointcut getPointcut() {
        return this.pointcut;
    }


    @Override
    public String toString() {
        return getClass().getName() + ": pointcut [" + getPointcut() + "]; advice [" + getAdvice() + "]";
    }
}
