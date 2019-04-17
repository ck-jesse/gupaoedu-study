package com.coy.gupaoedu.study.spring.framework.beans.exception;

/**
 * @author chenck
 * @date 2019/4/13 21:11
 */
public class GPBeansException extends RuntimeException {

    public GPBeansException() {
        super();
    }

    public GPBeansException(String message) {
        super(message);
    }

    public GPBeansException(String message, Throwable cause) {
        super(message, cause);
    }
}
