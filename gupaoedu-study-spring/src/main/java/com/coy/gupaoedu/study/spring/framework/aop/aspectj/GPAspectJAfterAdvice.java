package com.coy.gupaoedu.study.spring.framework.aop.aspectj;

import com.coy.gupaoedu.study.spring.framework.aop.advice.GPAfterAdvice;
import com.coy.gupaoedu.study.spring.framework.aop.aopalliance.intercept.GPMethodInvocation;
import com.coy.gupaoedu.study.spring.framework.aop.support.matcher.GPPointcut;
import com.coy.gupaoedu.study.spring.framework.beans.GPBeanFactory;

import java.io.Serializable;
import java.lang.reflect.Method;

/**
 * @author chenck
 * @date 2019/4/24 16:22
 */
public class GPAspectJAfterAdvice extends GPAbstractAspectJAdvice implements GPAfterAdvice, Serializable {


    public GPAspectJAfterAdvice(Method aspectJBeforeAdviceMethod, GPPointcut pointcut, GPBeanFactory beanFactory) {
        super(aspectJBeforeAdviceMethod, pointcut, beanFactory);
    }

    @Override
    public Object invoke(GPMethodInvocation invocation) throws Throwable {
        Object retValue = invocation.proceed();
        super.invokeAdviceMethod(invocation, retValue, null);
        return retValue;
    }
}
