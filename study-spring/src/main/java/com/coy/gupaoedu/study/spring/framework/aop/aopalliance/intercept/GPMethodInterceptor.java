package com.coy.gupaoedu.study.spring.framework.aop.aopalliance.intercept;

/**
 * @author chenck
 * @date 2019/4/16 20:39
 */
public interface GPMethodInterceptor extends GPInterceptor {

    /**
     * Implement this method to perform extra treatments before and
     * after the invocation. Polite implementations would certainly
     * like to invoke {@link Joinpoint#proceed()}.
     *
     * @param invocation the method invocation joinpoint
     * @return the result of the call to {@link Joinpoint#proceed()};
     * might be intercepted by the interceptor
     * @throws Throwable if the interceptors or the target object
     *                   throws an exception
     */
    Object invoke(GPMethodInvocation invocation) throws Throwable;
}
