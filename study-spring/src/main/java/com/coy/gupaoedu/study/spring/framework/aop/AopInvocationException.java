package com.coy.gupaoedu.study.spring.framework.aop;

/**
 * @author chenck
 * @date 2019/4/23 17:16
 */
public class AopInvocationException extends RuntimeException {

    /**
     * Constructor for AopInvocationException.
     *
     * @param msg the detail message
     */
    public AopInvocationException(String msg) {
        super(msg);
    }

    /**
     * Constructor for AopInvocationException.
     *
     * @param msg   the detail message
     * @param cause the root cause
     */
    public AopInvocationException(String msg, Throwable cause) {
        super(msg, cause);
    }
}
