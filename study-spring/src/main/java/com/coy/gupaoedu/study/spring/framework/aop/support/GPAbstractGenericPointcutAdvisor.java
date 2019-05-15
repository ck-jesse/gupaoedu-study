package com.coy.gupaoedu.study.spring.framework.aop.support;

import com.coy.gupaoedu.study.spring.framework.aop.GPPointcutAdvisor;
import com.coy.gupaoedu.study.spring.framework.aop.aopalliance.GPAdvice;

import java.io.Serializable;

/**
 * @author chenck
 * @date 2019/4/17 12:53
 */
public abstract class GPAbstractGenericPointcutAdvisor implements GPPointcutAdvisor, Serializable {

    private GPAdvice advice = EMPTY_ADVICE;

    /**
     * Specify the advice that this advisor should apply.
     */
    public void setAdvice(GPAdvice advice) {
        this.advice = advice;
    }

    @Override
    public GPAdvice getAdvice() {
        return this.advice;
    }


    @Override
    public String toString() {
        return getClass().getName() + ": advice [" + getAdvice() + "]";
    }
}
