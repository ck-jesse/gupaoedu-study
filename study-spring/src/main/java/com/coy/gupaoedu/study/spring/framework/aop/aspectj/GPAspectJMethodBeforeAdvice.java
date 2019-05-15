package com.coy.gupaoedu.study.spring.framework.aop.aspectj;

import com.coy.gupaoedu.study.spring.framework.aop.advice.GPMethodBeforeAdvice;
import com.coy.gupaoedu.study.spring.framework.aop.aopalliance.intercept.GPMethodInvocation;
import com.coy.gupaoedu.study.spring.framework.aop.support.matcher.GPPointcut;
import com.coy.gupaoedu.study.spring.framework.beans.GPBeanFactory;

import java.io.Serializable;
import java.lang.reflect.Method;

/**
 * @author chenck
 * @date 2019/4/24 17:34
 */
public class GPAspectJMethodBeforeAdvice extends GPAbstractAspectJAdvice implements GPMethodBeforeAdvice, Serializable {

    public GPAspectJMethodBeforeAdvice(Method aspectJAdviceMethod, GPPointcut pointcut, GPBeanFactory beanFactory) {
        super(aspectJAdviceMethod, pointcut, beanFactory);
    }

    @Override
    public void before(Method method, Object[] args, Object target) throws Throwable {
        super.invokeAdviceMethod(super.currentJoinPoint(), null, null);
    }

    @Override
    public Object invoke(GPMethodInvocation invocation) throws Throwable {
        this.before(invocation.getMethod(), invocation.getArguments(), invocation.getThis());
        return invocation.proceed();
    }
}
