package com.coy.gupaoedu.study.spring.framework.aop;

import com.coy.gupaoedu.study.spring.framework.aop.aopalliance.aop.GPAdvice;

/**
 * AOP 通知Advice的顾问
 *
 * @author chenck
 * @date 2019/4/16 20:28
 */
public interface GPAdvisor {

    /**
     * 空的公用占位符对象
     */
    GPAdvice EMPTY_ADVICE = new GPAdvice() {
    };

    /**
     * 返回通知Advice(拦截器)
     */
    GPAdvice getAdvice();
}
