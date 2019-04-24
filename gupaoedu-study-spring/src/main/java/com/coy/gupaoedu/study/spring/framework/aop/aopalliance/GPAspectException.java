package com.coy.gupaoedu.study.spring.framework.aop.aopalliance;

/**
 * Superclass for all AOP infrastructure exceptions
 *
 * @author chenck
 * @date 2019/4/16 20:41
 */
public class GPAspectException extends RuntimeException {

    /**
     * Constructor for AspectException.
     *
     * @param message the exception message
     */
    public GPAspectException(String message) {
        super(message);
    }

    /**
     * Constructor for AspectException.
     *
     * @param message the exception message
     * @param cause   the root cause, if any
     */
    public GPAspectException(String message, Throwable cause) {
        super(message, cause);
    }

}