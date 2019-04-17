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
}
