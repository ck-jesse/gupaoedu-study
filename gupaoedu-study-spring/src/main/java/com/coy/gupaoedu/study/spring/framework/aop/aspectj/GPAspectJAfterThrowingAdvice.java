package com.coy.gupaoedu.study.spring.framework.aop.aspectj;

import com.coy.gupaoedu.study.spring.framework.aop.advice.GPAfterAdvice;
import com.coy.gupaoedu.study.spring.framework.aop.aopalliance.intercept.GPMethodInterceptor;
import com.coy.gupaoedu.study.spring.framework.aop.aopalliance.intercept.GPMethodInvocation;
import com.coy.gupaoedu.study.spring.framework.aop.support.matcher.GPPointcut;
import com.coy.gupaoedu.study.spring.framework.beans.GPBeanFactory;

import java.io.Serializable;
import java.lang.reflect.Method;

/**
 * @author chenck
 * @date 2019/4/24 17:36
 */
public class GPAspectJAfterThrowingAdvice extends GPAbstractAspectJAdvice implements GPMethodInterceptor, GPAfterAdvice, Serializable {

    private String throwingName;

    public GPAspectJAfterThrowingAdvice(Method aspectJAdviceMethod, GPPointcut pointcut, GPBeanFactory beanFactory) {
        super(aspectJAdviceMethod, pointcut, beanFactory);
    }

    @Override
    public Object invoke(GPMethodInvocation invocation) throws Throwable {
        return null;
    }

    public String getThrowingName() {
        return throwingName;
    }

    public void setThrowingName(String throwingName) {
        this.throwingName = throwingName;
    }
}
