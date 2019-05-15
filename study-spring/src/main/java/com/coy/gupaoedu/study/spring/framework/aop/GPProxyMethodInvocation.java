package com.coy.gupaoedu.study.spring.framework.aop;

import com.coy.gupaoedu.study.spring.framework.aop.aopalliance.intercept.GPMethodInvocation;

/**
 * 基于代理的方法执行器
 * <p>
 * Extension of the AOP Alliance MethodInvocation interface
 *
 * @author chenck
 * @date 2019/4/17 19:22
 */
public interface GPProxyMethodInvocation extends GPMethodInvocation {

    /**
     * Return the proxy that this method invocation was made through.
     *
     * @return the original proxy object
     */
    Object getProxy();

    /**
     * Set the arguments to be used on subsequent invocations in the any advice
     * in this chain.
     *
     * @param arguments the argument array
     */
    void setArguments(Object... arguments);

}
