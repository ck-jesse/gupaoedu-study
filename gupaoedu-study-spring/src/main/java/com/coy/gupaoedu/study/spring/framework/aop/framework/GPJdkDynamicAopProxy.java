package com.coy.gupaoedu.study.spring.framework.aop.framework;

import com.coy.gupaoedu.study.spring.framework.aop.aopalliance.intercept.GPMethodInvocation;
import com.coy.gupaoedu.study.spring.framework.core.util.AopProxyUtils;
import com.coy.gupaoedu.study.spring.framework.core.util.ReflectionUtils;

import java.io.Serializable;
import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Method;
import java.lang.reflect.Proxy;
import java.util.List;

/**
 * JDK动态代理
 *
 * @author chenck
 * @date 2019/4/14 21:54
 */
public class GPJdkDynamicAopProxy implements GPAopProxy, InvocationHandler, Serializable {

    /**
     * Config used to configure this proxy
     */
    private final GPAdvisedSupport advised;

    /**
     * Is the {@link #equals} method defined on the proxied interfaces?
     * 记录代理类实现的接口集中是否有定义equals方法，true表示有，false表示无（默认）
     */
    private boolean equalsDefined;

    /**
     * Is the {@link #hashCode} method defined on the proxied interfaces?
     * 记录代理类实现的接口集中是否有定义hashCode方法，true表示有，false表示无（默认）
     */
    private boolean hashCodeDefined;

    /**
     * Construct a new JdkDynamicAopProxy for the given AOP configuration
     * 用advise配置构建一个JDK动态代理对象
     */
    public GPJdkDynamicAopProxy(GPAdvisedSupport advisedSupport) {
        this.advised = advisedSupport;
    }

    @Override
    public Object getProxy() {
        return getProxy(this.advised.getTargetClass().getClassLoader());
    }

    @Override
    public Object getProxy(ClassLoader classLoader) {
        Class<?>[] proxiedInterfaces = this.advised.getTargetClass().getInterfaces();
        findDefinedEqualsAndHashCodeMethods(proxiedInterfaces);
        return Proxy.newProxyInstance(classLoader, proxiedInterfaces, this);
    }

    /**
     * 从提供的接口集中查找equals和hashCode方法
     *
     * @param proxiedInterfaces the interfaces to introspect
     */
    private void findDefinedEqualsAndHashCodeMethods(Class<?>[] proxiedInterfaces) {
        for (Class<?> proxiedInterface : proxiedInterfaces) {
            Method[] methods = proxiedInterface.getDeclaredMethods();
            for (Method method : methods) {
                if (ReflectionUtils.isEqualsMethod(method)) {
                    this.equalsDefined = true;
                }
                if (ReflectionUtils.isHashCodeMethod(method)) {
                    this.hashCodeDefined = true;
                }
                if (this.equalsDefined && this.hashCodeDefined) {
                    return;
                }
            }
        }
    }

    @Override
    public Object invoke(Object proxy, Method method, Object[] args) throws Throwable {

        // equals和hashcode方法的处理
        if (!this.equalsDefined && ReflectionUtils.isEqualsMethod(method)) {
            // The target does not implement the equals(Object) method itself.
            return equals(args[0]);
        }
        if (!this.hashCodeDefined && ReflectionUtils.isHashCodeMethod(method)) {
            // The target does not implement the hashCode() method itself.
            return hashCode();
        }

        Class<?> targetClass = this.advised.getTargetClass();
        // 获取method的拦截器链
        List<Object> chain = this.advised.getInterceptorsAndDynamicInterceptionAdvice(method, targetClass);

        Object retVal;
        Object target = this.advised.getTarget();
        if (chain.isEmpty()) {
            // 没有匹配的拦截器时，通过反射来执行
            method.setAccessible(true);
            retVal = method.invoke(target, args);
        } else {
            // 创建一个method执行器
            GPMethodInvocation invocation = new GPReflectiveMethodInvocation(proxy, target, method, args, targetClass, chain);
            // 执行method的拦截器链式调用
            retVal = invocation.proceed();
        }
        return retVal;
    }

    /**
     * Equality means interfaces, advisors and TargetSource are equal.
     * <p>The compared object may be a JdkDynamicAopProxy instance itself
     * or a dynamic proxy wrapping a JdkDynamicAopProxy instance.
     */
    @Override
    public boolean equals(Object other) {
        if (other == this) {
            return true;
        }
        if (other == null) {
            return false;
        }

        GPJdkDynamicAopProxy otherProxy;
        if (other instanceof GPJdkDynamicAopProxy) {
            otherProxy = (GPJdkDynamicAopProxy) other;
        } else if (Proxy.isProxyClass(other.getClass())) {
            InvocationHandler ih = Proxy.getInvocationHandler(other);
            if (!(ih instanceof GPJdkDynamicAopProxy)) {
                return false;
            }
            otherProxy = (GPJdkDynamicAopProxy) ih;
        } else {
            // Not a valid comparison...
            return false;
        }

        // If we get here, otherProxy is the other AopProxy.
        return AopProxyUtils.equalsInProxy(this.advised, otherProxy.advised);
    }

    /**
     * Proxy uses the hash code of the TargetSource.
     */
    @Override
    public int hashCode() {
        return GPJdkDynamicAopProxy.class.hashCode() * 13 + this.advised.getTarget().hashCode();
    }

}
