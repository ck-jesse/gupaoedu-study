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

    /**
     * Add the specified user attribute with the given value to this invocation.
     * <p>Such attributes are not used within the AOP framework itself. They are
     * just kept as part of the invocation object, for use in special interceptors.
     * 添加用户属性到此调用
     *
     * @param key   the name of the attribute
     * @param value the value of the attribute, or {@code null} to reset it
     */
    void setUserAttribute(String key, Object value);

    /**
     * Return the value of the specified user attribute.
     *
     * @param key the name of the attribute
     * @return the value of the attribute, or {@code null} if not set
     * @see #setUserAttribute
     */
    Object getUserAttribute(String key);
}
