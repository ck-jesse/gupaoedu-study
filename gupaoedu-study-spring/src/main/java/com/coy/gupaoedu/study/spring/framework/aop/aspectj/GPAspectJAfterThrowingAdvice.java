package com.coy.gupaoedu.study.spring.framework.aop.aspectj;

import com.coy.gupaoedu.study.spring.framework.aop.advice.GPAfterAdvice;
import com.coy.gupaoedu.study.spring.framework.aop.aopalliance.intercept.GPMethodInvocation;
import com.coy.gupaoedu.study.spring.framework.aop.support.matcher.GPPointcut;
import com.coy.gupaoedu.study.spring.framework.beans.GPBeanFactory;
import lombok.extern.slf4j.Slf4j;

import java.io.Serializable;
import java.lang.reflect.Method;

/**
 * @author chenck
 * @date 2019/4/24 17:36
 */
@Slf4j
public class GPAspectJAfterThrowingAdvice extends GPAbstractAspectJAdvice implements GPAfterAdvice, Serializable {

    /**
     * 抛出的异常名称
     */
    private String afterThrowingName;

    public GPAspectJAfterThrowingAdvice(Method aspectJAdviceMethod, GPPointcut pointcut, GPBeanFactory beanFactory, String afterThrowingName) {
        super(aspectJAdviceMethod, pointcut, beanFactory);
        this.afterThrowingName = afterThrowingName;
        try {
            Class<?> throwsAdvice = Class.forName(afterThrowingName);
        } catch (ClassNotFoundException e) {
            throw new IllegalArgumentException("class not found in class [" + afterThrowingName + "]");
        }
    }

    @Override
    public Object invoke(GPMethodInvocation invocation) throws Throwable {
        super.setInvocation(invocation);
        try {
            return invocation.proceed();
        } catch (Throwable e) {
            Class<?> exceptionClass = e.getClass();
            if (exceptionClass.getName().equals(afterThrowingName)) {
                super.invokeAdviceMethod(invocation, null, e);
            }
            throw e;
        }
    }

    public String getAfterThrowingName() {
        return afterThrowingName;
    }

    public void setAfterThrowingName(String afterThrowingName) {
        this.afterThrowingName = afterThrowingName;
    }

}
