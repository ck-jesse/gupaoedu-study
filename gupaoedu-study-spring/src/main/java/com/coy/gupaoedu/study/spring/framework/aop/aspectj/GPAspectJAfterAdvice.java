package com.coy.gupaoedu.study.spring.framework.aop.aspectj;

import com.coy.gupaoedu.study.spring.framework.aop.aopalliance.intercept.GPMethodInterceptor;
import com.coy.gupaoedu.study.spring.framework.aop.aopalliance.intercept.GPMethodInvocation;
import com.coy.gupaoedu.study.spring.framework.aop.support.matcher.GPPointcut;
import com.coy.gupaoedu.study.spring.framework.beans.GPBeanFactory;

import java.io.Serializable;
import java.lang.reflect.Method;

/**
 * @author chenck
 * @date 2019/4/24 16:22
 */
public class GPAspectJAfterAdvice extends GPAbstractAspectJAdvice implements GPMethodInterceptor, GPAfterAdvice, Serializable {


    public GPAspectJAfterAdvice(Method aspectJBeforeAdviceMethod, GPPointcut pointcut, GPBeanFactory beanFactory) {
        super(aspectJBeforeAdviceMethod, pointcut, beanFactory);
    }

    @Override
    public Object invoke(GPMethodInvocation invocation) throws Throwable {
        Object retValue = invocation.proceed();
        invokeAdviceMethod(invocation, retValue, null);
        return retValue;
    }
}
