package com.coy.gupaoedu.study.spring.framework.aop.aspectj;

import com.coy.gupaoedu.study.spring.framework.aop.AopInvocationException;
import com.coy.gupaoedu.study.spring.framework.aop.aopalliance.GPAdvice;
import com.coy.gupaoedu.study.spring.framework.aop.aopalliance.intercept.GPJoinPoint;
import com.coy.gupaoedu.study.spring.framework.aop.aopalliance.intercept.GPMethodInterceptor;
import com.coy.gupaoedu.study.spring.framework.aop.aopalliance.intercept.GPMethodInvocation;
import com.coy.gupaoedu.study.spring.framework.aop.framework.GPReflectiveMethodInvocation;
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
     * 执行advice method
     */
    protected Object invokeAdviceMethod(GPJoinPoint joinPoint, Object returnValue, Throwable ex) throws Throwable {
        Class[] paramTypes = this.aspectJAdviceMethod.getParameterTypes();
        Object[] args = new Object[paramTypes.length];

        for (int i = 0; i < paramTypes.length; i++) {
            if (paramTypes[i] == GPJoinPoint.class) {
                args[i] = joinPoint;
            }
            // 此处暂时先直接判断类型
            else if (paramTypes[i] == GPMethodInvocation.class) {
                args[i] = joinPoint;
            } else if (paramTypes[i] == Throwable.class) {
                args[i] = ex;
            } else if (paramTypes[i] == Object.class) {
                args[i] = returnValue;
            }
        }
        return invokeAdviceMethodWithGivenArgs(args);
    }

    /**
     * 执行advice method，并设置指定的参数
     */
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

    /**
     * Key used in ReflectiveMethodInvocation userAtributes map for the current joinpoint.
     */
    protected static final String JOIN_POINT_KEY = GPJoinPoint.class.getName();

    /**
     * 获取当前的JoinPoint连接点，也就是MethodInvocation
     * <p>
     * Lazily instantiate joinpoint for the current invocation.
     * Requires MethodInvocation to be bound with ExposeInvocationInterceptor.
     * <p>Do not use if access is available to the current ReflectiveMethodInvocation
     * (in an around advice).
     *
     * @return current AspectJ joinpoint, or through an exception if we're not in a
     * Spring AOP invocation.
     */
    public GPMethodInvocation currentJoinPoint() {
        GPMethodInvocation mi = GPExposeInvocationInterceptor.currentInvocation();
        if (!(mi instanceof GPReflectiveMethodInvocation)) {
            throw new IllegalStateException("MethodInvocation is not a Spring ProxyMethodInvocation: " + mi);
        }
        GPReflectiveMethodInvocation pmi = (GPReflectiveMethodInvocation) mi;
        return pmi;
    }

}
