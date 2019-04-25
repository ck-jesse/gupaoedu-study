package com.coy.gupaoedu.study.spring.framework.aop.framework;

import com.coy.gupaoedu.study.spring.framework.core.util.AopProxyUtils;
import com.sun.istack.internal.Nullable;
import net.sf.cglib.proxy.Callback;
import net.sf.cglib.proxy.Enhancer;
import net.sf.cglib.proxy.MethodInterceptor;
import net.sf.cglib.proxy.MethodProxy;

import java.io.Serializable;
import java.lang.reflect.Method;
import java.lang.reflect.Modifier;
import java.util.List;

/**
 * CGLIB代理
 *
 * @author chenck
 * @date 2019/4/14 21:55
 */
public class GPCglibAopProxy implements GPAopProxy, Serializable {

    /**
     * Config used to configure this proxy
     */
    private final GPAdvisedSupport advised;

    public GPCglibAopProxy(GPAdvisedSupport advisedSupport) {
        this.advised = advisedSupport;
    }

    @Override
    public Object getProxy() {
        return getProxy(null);
    }

    @Override
    public Object getProxy(ClassLoader classLoader) {

        try {
            // Configure CGLIB Enhancer...
            Enhancer enhancer = new Enhancer();
            if (classLoader != null) {
                enhancer.setClassLoader(classLoader);
            }
            // 设置需要创建子类的目标对象class
            enhancer.setSuperclass(this.advised.getTargetClass());
            //enhancer.setInterfaces(AopProxyUtils.completeProxiedInterfaces(this.advised));

            // 获取拦截器
            Callback aopInterceptor = new DynamicAdvisedInterceptor(this.advised);
            // 回调方法
            enhancer.setCallback(aopInterceptor);

            // Generate the proxy class and create a proxy instance.
            //enhancer.setInterceptDuringConstruction(false);
            return enhancer.create();
        } catch (Exception ex) {
            ex.printStackTrace();
            // TargetSource.getTarget() failed
            throw new AopConfigException("Unexpected AOP exception", ex);
        }
    }


    /**
     * General purpose AOP callback. Used when the target is dynamic or when the
     * proxy is not frozen.
     */
    private static class DynamicAdvisedInterceptor implements MethodInterceptor, Serializable {

        private final GPAdvisedSupport advised;

        public DynamicAdvisedInterceptor(GPAdvisedSupport advised) {
            this.advised = advised;
        }

        @Override
        @Nullable
        public Object intercept(Object proxy, Method method, Object[] args, MethodProxy methodProxy) throws Throwable {
            Object target = this.advised.getTarget();
            // Get as late as possible to minimize the time we "own" the target, in case it comes from a pool...
            Class<?> targetClass = (target != null ? target.getClass() : null);
            List<Object> chain = this.advised.getInterceptorsAndDynamicInterceptionAdvice(method, targetClass);
            Object retVal;
            // Check whether we only have one InvokerInterceptor: that is,
            // no real advice, but just reflective invocation of the target.
            if (chain.isEmpty() && Modifier.isPublic(method.getModifiers())) {
                // We can skip creating a MethodInvocation: just invoke the target directly.
                // Note that the final invoker must be an InvokerInterceptor, so we know
                // it does nothing but a reflective operation on the target, and no hot
                // swapping or fancy proxying.
                Object[] argsToUse = AopProxyUtils.adaptArgumentsIfNecessary(method, args);
                retVal = methodProxy.invoke(target, argsToUse);
            } else {
                // We need to create a method invocation...
                CglibMethodInvocation invocation = new CglibMethodInvocation(proxy, target, method, args, targetClass, chain, methodProxy);
                retVal = invocation.proceed();
            }
            retVal = processReturnType(proxy, target, method, retVal);
            return retVal;
        }

        @Override
        public boolean equals(Object other) {
            return (this == other ||
                    (other instanceof DynamicAdvisedInterceptor &&
                            this.advised.equals(((DynamicAdvisedInterceptor) other).advised)));
        }

        /**
         * CGLIB uses this to drive proxy creation.
         */
        @Override
        public int hashCode() {
            return this.advised.hashCode();
        }
    }


    /**
     * Implementation of AOP Alliance MethodInvocation used by this AOP proxy.
     */
    private static class CglibMethodInvocation extends GPReflectiveMethodInvocation {

        private final MethodProxy methodProxy;

        private final boolean publicMethod;

        public CglibMethodInvocation(Object proxy, @Nullable Object target, Method method, Object[] arguments, @Nullable Class<?> targetClass,
                                     List<Object> interceptorsAndDynamicMethodMatchers, MethodProxy methodProxy) {
            super(proxy, target, method, arguments, targetClass, interceptorsAndDynamicMethodMatchers);
            this.methodProxy = methodProxy;
            this.publicMethod = Modifier.isPublic(method.getModifiers());
        }

        /**
         * Gives a marginal performance improvement versus using reflection to
         * invoke the target when invoking public methods.
         */
        @Override
        protected Object invokeJoinpoint() throws Throwable {
            // 对父类中方法的重写，如果方法是public的，那么直接执行，如果不是public的则设置method.setAccessible(true)，以便可以执行该方法
            if (this.publicMethod) {
                return this.methodProxy.invoke(this.target, this.arguments);
            } else {
                return super.invokeJoinpoint();
            }
        }
    }

    /**
     * Process a return value. Wraps a return of {@code this} if necessary to be the
     * {@code proxy} and also verifies that {@code null} is not returned as a primitive.
     */
    @Nullable
    private static Object processReturnType(
            Object proxy, @Nullable Object target, Method method, @Nullable Object returnValue) {
        // Massage return value if necessary
        if (returnValue != null && returnValue == target) {
            returnValue = proxy;
        }
        Class<?> returnType = method.getReturnType();
        if (returnValue == null && returnType != Void.TYPE && returnType.isPrimitive()) {
            throw new AopConfigException("Null return value from advice does not match primitive return type for: " + method);
        }
        return returnValue;
    }

}
