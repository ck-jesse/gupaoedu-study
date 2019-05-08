package com.coy.gupaoedu.study.mebatis.exception;

/**
 * @author chenck
 * @date 2019/5/6 21:00
 */
public class BindingException extends MebatisException {

    public BindingException() {
        super();
    }

    public BindingException(String message) {
        super(message);
    }

    public BindingException(String message, Throwable cause) {
        super(message, cause);
    }

    public BindingException(Throwable cause) {
        super(cause);
    }
}
