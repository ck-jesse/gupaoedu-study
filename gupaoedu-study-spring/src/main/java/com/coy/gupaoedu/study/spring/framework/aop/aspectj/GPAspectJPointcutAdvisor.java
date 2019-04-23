package com.coy.gupaoedu.study.spring.framework.aop.aspectj;

import com.coy.gupaoedu.study.spring.framework.aop.GPPointcut;
import com.coy.gupaoedu.study.spring.framework.aop.GPPointcutAdvisor;
import com.coy.gupaoedu.study.spring.framework.aop.aopalliance.aop.GPAdvice;
import com.coy.gupaoedu.study.spring.framework.core.GPOrdered;
import com.coy.gupaoedu.study.spring.framework.core.util.Assert;

/**
 * @author chenck
 * @date 2019/4/23 16:55
 */
public class GPAspectJPointcutAdvisor implements GPPointcutAdvisor, GPOrdered {

    /**
     *
     */
    private final GPAbstractAspectJAdvice advice;

    /**
     * 切入点
     */
    private final GPPointcut pointcut;

    private Integer order;

    public GPAspectJPointcutAdvisor(GPAbstractAspectJAdvice advice) {
        Assert.notNull(advice, "Advice must not be null");
        this.advice = advice;
        this.pointcut = advice.buildSafePointcut();
    }

    @Override
    public GPPointcut getPointcut() {
        return this.pointcut;
    }

    @Override
    public GPAdvice getAdvice() {
        return this.advice;
    }

    public void setOrder(int order) {
        this.order = order;
    }

    @Override
    public int getOrder() {
        if (this.order != null) {
            return this.order;
        } else {
            return this.advice.getOrder();
        }
    }
}
