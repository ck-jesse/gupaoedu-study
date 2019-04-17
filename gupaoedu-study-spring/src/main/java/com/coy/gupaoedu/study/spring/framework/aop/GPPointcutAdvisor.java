package com.coy.gupaoedu.study.spring.framework.aop;

/**
 * 相当于切入点Pointcut和通知Advice的顾问
 *
 * @author chenck
 * @date 2019/4/17 11:55
 */
public interface GPPointcutAdvisor extends GPAdvisor {

    /**
     * Get the Pointcut that drives this advisor.
     */
    GPPointcut getPointcut();
}
