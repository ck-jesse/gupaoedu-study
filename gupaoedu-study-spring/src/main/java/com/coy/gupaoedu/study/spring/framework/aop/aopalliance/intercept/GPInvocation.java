package com.coy.gupaoedu.study.spring.framework.aop.aopalliance.intercept;

/**
 * 执行器
 *
 * @author chenck
 * @date 2019/4/16 20:40
 */
public interface GPInvocation extends GPJoinPoint {
    /**
     * Get the arguments as an array object.
     * It is possible to change element values within this
     * array to change the arguments.
     *
     * @return the argument of the invocation
     */
    Object[] getArguments();
}
