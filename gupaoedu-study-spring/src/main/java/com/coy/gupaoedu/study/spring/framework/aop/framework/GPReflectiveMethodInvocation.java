package com.coy.gupaoedu.study.spring.framework.aop.framework;

import com.coy.gupaoedu.study.spring.framework.aop.GPProxyMethodInvocation;
import com.coy.gupaoedu.study.spring.framework.aop.aopalliance.intercept.GPMethodInterceptor;

import java.lang.reflect.AccessibleObject;
import java.lang.reflect.Method;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * 先调用拦截器链，并使用反射调用目标对象方法
 *
 * @author chenck
 * @date 2019/4/17 19:26
 */
public class GPReflectiveMethodInvocation implements GPProxyMethodInvocation {

    /**
     * 代理对象
     */
    protected final Object proxy;

    /**
     * 目标对象
     */
    protected final Object target;

    /**
     * 目标方法
     */
    protected final Method method;

    /**
     * 目标方法参数
     */
    protected Object[] arguments = new Object[0];

    /**
     * 目标对象class
     */
    private final Class<?> targetClass;

    /**
     * Lazily initialized map of user-specific attributes for this invocation.
     * 用户属性
     */
    private Map<String, Object> userAttributes;

    /**
     * List of MethodInterceptor and InterceptorAndDynamicMethodMatcher
     * that need dynamic checks.
     */
    protected final List<?> interceptorsAndDynamicMethodMatchers;

    /**
     * Index from 0 of the current interceptor we're invoking.
     * -1 until we invoke: then the current interceptor.
     */
    private int currentInterceptorIndex = -1;

    /**
     * Construct a new ReflectiveMethodInvocation with the given arguments
     */
    protected GPReflectiveMethodInvocation(
            Object proxy, Object target, Method method, Object[] arguments,
            Class<?> targetClass, List<Object> interceptorsAndDynamicMethodMatchers) {
        this.proxy = proxy;
        this.target = target;
        this.targetClass = targetClass;
        this.method = method;
        this.arguments = arguments;
        this.interceptorsAndDynamicMethodMatchers = interceptorsAndDynamicMethodMatchers;
    }

    @Override
    public Object getProxy() {
        return this.proxy;
    }

    @Override
    public void setArguments(Object... arguments) {
        this.arguments = arguments;
    }

    @Override
    public void setUserAttribute(String key, Object value) {
        if (value != null) {
            if (this.userAttributes == null) {
                this.userAttributes = new HashMap<>();
            }
            this.userAttributes.put(key, value);
        } else {
            if (this.userAttributes != null) {
                this.userAttributes.remove(key);
            }
        }
    }

    @Override
    public Object getUserAttribute(String key) {
        return (this.userAttributes != null ? this.userAttributes.get(key) : null);
    }

    @Override
    public Method getMethod() {
        return this.method;
    }

    @Override
    public Object[] getArguments() {
        return this.arguments;
    }

    /**
     * 执行拦截器链式调用
     */
    @Override
    public Object proceed() throws Throwable {
        //	从索引-1开始，并提前递增
        if (this.currentInterceptorIndex == this.interceptorsAndDynamicMethodMatchers.size() - 1) {
            // 使用反射调用目标方法，也就是joinpoint
            return invokeJoinpoint();
        }

        Object interceptorOrInterceptionAdvice = this.interceptorsAndDynamicMethodMatchers.get(++this.currentInterceptorIndex);
        if (interceptorOrInterceptionAdvice instanceof GPInterceptorAndDynamicMethodMatcher) {
            // 动态方法匹配（静态部分已被评估并找到匹配项）
            GPInterceptorAndDynamicMethodMatcher dm = (GPInterceptorAndDynamicMethodMatcher) interceptorOrInterceptionAdvice;
            if (dm.methodMatcher.matches(this.method, this.targetClass, this.arguments)) {
                return dm.interceptor.invoke(this);
            } else {
                // 动态方法匹配失败，则跳过此拦截器并调用链中的下一个拦截器
                return proceed();
            }
        } else {
            // It's an interceptor, so we just invoke it: The pointcut will have
            // been evaluated statically before this object was constructed.
            return ((GPMethodInterceptor) interceptorOrInterceptionAdvice).invoke(this);
        }
    }

    /**
     * 使用反射调用目标方法
     *
     * @return the return value of the joinpoint
     * @throws Throwable if invoking the joinpoint resulted in an exception
     */
    protected Object invokeJoinpoint() throws Throwable {
        method.setAccessible(true);
        return method.invoke(this.target, this.method, this.arguments);
    }

    @Override
    public Object getThis() {
        return this.target;
    }

    @Override
    public AccessibleObject getStaticPart() {
        return this.method;
    }
}
