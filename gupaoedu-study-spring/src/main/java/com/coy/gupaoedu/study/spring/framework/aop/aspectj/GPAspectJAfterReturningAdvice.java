package com.coy.gupaoedu.study.spring.framework.aop.aspectj;

import com.coy.gupaoedu.study.spring.framework.aop.advice.GPAfterReturningAdvice;
import com.coy.gupaoedu.study.spring.framework.aop.aopalliance.intercept.GPMethodInvocation;
import com.coy.gupaoedu.study.spring.framework.aop.support.matcher.GPPointcut;
import com.coy.gupaoedu.study.spring.framework.beans.GPBeanFactory;

import java.io.Serializable;
import java.lang.reflect.Method;

/**
 * @author chenck
 * @date 2019/4/24 17:33
 */
public class GPAspectJAfterReturningAdvice extends GPAbstractAspectJAdvice implements GPAfterReturningAdvice, Serializable {

    public GPAspectJAfterReturningAdvice(Method aspectJAdviceMethod, GPPointcut pointcut, GPBeanFactory beanFactory) {
        super(aspectJAdviceMethod, pointcut, beanFactory);
    }

    @Override
    public void afterReturning(Object returnValue, Method method, Object[] args, Object target) throws Throwable {
        super.invokeAdviceMethod(super.getInvocation(), returnValue, null);
    }

    @Override
    public Object invoke(GPMethodInvocation invocation) throws Throwable {
        super.setInvocation(invocation);
        Object retVal = invocation.proceed();
        this.afterReturning(retVal, invocation.getMethod(), invocation.getArguments(), invocation.getThis());
        return retVal;
    }
}
