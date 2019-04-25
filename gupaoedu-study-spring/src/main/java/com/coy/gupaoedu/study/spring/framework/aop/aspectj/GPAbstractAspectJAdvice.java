package com.coy.gupaoedu.study.spring.framework.aop.aspectj;

import com.coy.gupaoedu.study.spring.framework.aop.AopInvocationException;
import com.coy.gupaoedu.study.spring.framework.aop.aopalliance.GPAdvice;
import com.coy.gupaoedu.study.spring.framework.aop.aopalliance.intercept.GPJoinpoint;
import com.coy.gupaoedu.study.spring.framework.aop.aopalliance.intercept.GPMethodInterceptor;
import com.coy.gupaoedu.study.spring.framework.aop.aopalliance.intercept.GPMethodInvocation;
import com.coy.gupaoedu.study.spring.framework.aop.support.matcher.GPPointcut;
import com.coy.gupaoedu.study.spring.framework.beans.GPBeanFactory;
import com.coy.gupaoedu.study.spring.framework.core.GPOrdered;
import com.coy.gupaoedu.study.spring.framework.core.util.Assert;
import com.coy.gupaoedu.study.spring.framework.core.util.ReflectionUtils;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;

/**
 * 抽象AspectJ切面通知
 *
 * @author chenck
 * @date 2019/4/23 16:58
 */
public abstract class GPAbstractAspectJAdvice implements GPMethodInterceptor, GPAdvice, GPOrdered {

    private final GPBeanFactory beanFactory;

    private final Class<?> declaringClass;

    private final String methodName;

    private final Class<?>[] parameterTypes;

    protected transient Method aspectJAdviceMethod;

    private final GPPointcut pointcut;

    /**
     * 在织入时传入的（也就是MethodInterceptor.invoke()中的invocation）
     */
    private GPMethodInvocation invocation;

    /**
     * The name of the aspect
     */
    private String aspectName = "";

    public GPAbstractAspectJAdvice(Method aspectJAdviceMethod, GPPointcut pointcut, GPBeanFactory beanFactory) {
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

    public GPMethodInvocation getInvocation() {
        return invocation;
    }

    public void setInvocation(GPMethodInvocation invocation) {
        this.invocation = invocation;
    }

    @Override
    public int getOrder() {
        Class<?> type = this.beanFactory.getType(this.aspectName);
        if (type != null) {
            if (GPOrdered.class.isAssignableFrom(type) && this.beanFactory.isSingleton(this.aspectName)) {
                return ((GPOrdered) this.beanFactory.getBean(this.aspectName)).getOrder();
            }
        }
        return GPOrdered.LOWEST_PRECEDENCE;
    }

    public final GPPointcut getPointcut() {
        return this.pointcut;
    }

    /**
     * Invoke the advice method.
     */
    protected Object invokeAdviceMethod(GPJoinpoint joinPoint, Object returnValue, Throwable ex) throws Throwable {
        Class[] paramTypes = this.aspectJAdviceMethod.getParameterTypes();
        Object[] args = new Object[paramTypes.length];

        /*for (int i = 0; i < paramTypes.length; i++) {
            if (paramTypes[i] instanceof GPJoinpoint) {
                args[i] = joinPoint;
            } else if (paramTypes[i] instanceof Throwable) {
                args[i] = ex;
            } else if (paramTypes[i] instanceof Object) {
                args[i] = returnValue;
            }
        }*/
        for (int i = 0; i < paramTypes.length; i++) {
            if (paramTypes[i] == GPJoinpoint.class) {
                args[i] = joinPoint;
            } else if (paramTypes[i] == GPMethodInvocation.class) {
                args[i] = joinPoint;
            } else if (paramTypes[i] == Throwable.class) {
                args[i] = ex;
            } else if (paramTypes[i] == Object.class) {
                args[i] = returnValue;
            }
        }
        return invokeAdviceMethodWithGivenArgs(args);
    }

    protected Object invokeAdviceMethodWithGivenArgs(Object[] args) throws Throwable {
        Object[] actualArgs = args;
        if (this.aspectJAdviceMethod.getParameterCount() == 0) {
            actualArgs = null;
        }
        try {
            ReflectionUtils.makeAccessible(this.aspectJAdviceMethod);
            Object target = this.beanFactory.getBean(declaringClass);
            if (null == target) {
                target = declaringClass.newInstance();
            }
            return this.aspectJAdviceMethod.invoke(target, actualArgs);
        } catch (IllegalArgumentException ex) {
            throw new AopInvocationException("Mismatch on arguments to advice method [" +
                    this.aspectJAdviceMethod + "]; pointcut expression [ xxx ]", ex);
        } catch (InvocationTargetException ex) {
            throw ex.getTargetException();
        }
    }

}
