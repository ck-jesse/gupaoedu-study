package com.coy.gupaoedu.study.spring.framework.aop.framework;

/**
 * @author chenck
 * @date 2019/4/17 12:00
 */
public class AopConfigException extends RuntimeException {

    /**
     * Constructor for AopConfigException.
     *
     * @param msg the detail message
     */
    public AopConfigException(String msg) {
        super(msg);
    }

    /**
     * Constructor for AopConfigException.
     *
     * @param msg   the detail message
     * @param cause the root cause
     */
    public AopConfigException(String msg, Throwable cause) {
        super(msg, cause);
    }
}
