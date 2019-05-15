package com.coy.gupaoedu.study.spring.framework.aop.aopalliance.intercept;

import java.lang.reflect.Method;

/**
 * 方法执行器
 *
 * @author chenck
 * @date 2019/4/16 20:41
 */
public interface GPMethodInvocation extends GPInvocation {

    /**
     * Get the method being called.
     *
     * @return the method being called
     */
    Method getMethod();

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
