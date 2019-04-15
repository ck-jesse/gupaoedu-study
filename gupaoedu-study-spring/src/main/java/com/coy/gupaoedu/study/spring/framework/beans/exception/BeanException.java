package com.coy.gupaoedu.study.spring.framework.beans.exception;

/**
 * @author chenck
 * @date 2019/4/13 21:11
 */
public class BeanException extends RuntimeException {

    public BeanException() {
        super();
    }

    public BeanException(String message) {
        super(message);
    }

    public BeanException(String message, Throwable cause) {
        super(message, cause);
    }
}
