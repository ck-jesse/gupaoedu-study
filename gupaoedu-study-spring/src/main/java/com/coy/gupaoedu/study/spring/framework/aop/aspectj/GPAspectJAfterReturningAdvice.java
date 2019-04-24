package com.coy.gupaoedu.study.spring.framework.aop.aspectj;

import com.coy.gupaoedu.study.spring.framework.aop.support.matcher.GPPointcut;
import com.coy.gupaoedu.study.spring.framework.beans.GPBeanFactory;

import java.io.Serializable;
import java.lang.reflect.Method;

/**
 * @author chenck
 * @date 2019/4/24 17:33
 */
public class GPAspectJAfterReturningAdvice extends GPAbstractAspectJAdvice implements GPAfterReturningAdvice, GPAfterAdvice, Serializable {

    public GPAspectJAfterReturningAdvice(Method aspectJAdviceMethod, GPPointcut pointcut, GPBeanFactory beanFactory) {
        super(aspectJAdviceMethod, pointcut, beanFactory);
    }
}
