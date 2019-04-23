package com.coy.gupaoedu.study.spring.framework.aop.aspectj;

import com.coy.gupaoedu.study.spring.framework.aop.AopInvocationException;
import com.coy.gupaoedu.study.spring.framework.aop.aopalliance.aop.GPAdvice;
import com.coy.gupaoedu.study.spring.framework.beans.GPBeanFactory;
import com.coy.gupaoedu.study.spring.framework.core.GPOrdered;
import com.coy.gupaoedu.study.spring.framework.core.util.Assert;
import com.coy.gupaoedu.study.spring.framework.core.util.ReflectionUtils;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;

/**
 * @author chenck
 * @date 2019/4/23 16:58
 */
public abstract class GPAbstractAspectJAdvice implements GPAdvice, GPOrdered {

    private final GPBeanFactory beanFactory;

    private final Class<?> declaringClass;

    private final String methodName;

    private final Class<?>[] parameterTypes;

    protected transient Method aspectJAdviceMethod;

    private final GPAspectJExpressionPointcut pointcut;

    /**
     * The name of the aspect
     */
    private String aspectName = "";

    public GPAbstractAspectJAdvice(Method aspectJAdviceMethod, GPAspectJExpressionPointcut pointcut, GPBeanFactory beanFactory) {
        Assert.notNull(aspectJAdviceMethod, "Advice method must not be null");
        this.declaringClass = aspectJAdviceMethod.getDeclaringClass();
        this.methodName = aspectJAdviceMethod.getName();
        this.parameterTypes = aspectJAdviceMethod.getParameterTypes();
        this.aspectJAdviceMethod = aspectJAdviceMethod;
        this.pointcut = pointcut;
        this.beanFactory = beanFactory;
    }

    public void setAspectName(String name) {
        this.aspectName = name;
    }

    public String getAspectName() {
        return this.aspectName;
    }

    /**
     * Return the AspectJ expression pointcut.
     */
    public final GPAspectJExpressionPointcut getPointcut() {
        return this.pointcut;
    }

    /*public final GPPointcut buildSafePointcut() {
        GPPointcut pc = getPointcut();
        GPMethodMatcher safeMethodMatcher = MethodMatchers.intersection(
                new AdviceExcludingMethodMatcher(this.aspectJAdviceMethod), pc.getMethodMatcher());
        return new ComposablePointcut(pc.getClassFilter(), safeMethodMatcher);
    }


    *//**
     * Invoke the advice method.
     *
     * @param jpMatch     the JoinPointMatch that matched this execution join point
     * @param returnValue the return value from the method execution (may be null)
     * @param ex          the exception thrown by the method execution (may be null)
     * @return the invocation result
     * @throws Throwable in case of invocation failure
     *//*
    protected Object invokeAdviceMethod(GPJoinPointMatch jpMatch, Object returnValue, Throwable ex) throws Throwable {
        return invokeAdviceMethodWithGivenArgs(argBinding(getJoinPoint(), jpMatch, returnValue, ex));
    }

    *//**
     * As above, but in this case we are given the join point.
     *//*
    protected Object invokeAdviceMethod(GPJoinPoint jp, GPJoinPointMatch jpMatch, Object returnValue, Throwable t) throws Throwable {
        return invokeAdviceMethodWithGivenArgs(argBinding(jp, jpMatch, returnValue, t));
    }*/

    protected Object invokeAdviceMethodWithGivenArgs(Object[] args) throws Throwable {
        Object[] actualArgs = args;
        if (this.aspectJAdviceMethod.getParameterCount() == 0) {
            actualArgs = null;
        }
        try {
            ReflectionUtils.makeAccessible(this.aspectJAdviceMethod);
            return this.aspectJAdviceMethod.invoke(this.beanFactory.getBean(declaringClass), actualArgs);
        } catch (IllegalArgumentException ex) {
            throw new AopInvocationException("Mismatch on arguments to advice method [" +
                    this.aspectJAdviceMethod + "]; pointcut expression [" +
                    this.pointcut.getExpression() + "]", ex);
        } catch (InvocationTargetException ex) {
            throw ex.getTargetException();
        }
    }

}
