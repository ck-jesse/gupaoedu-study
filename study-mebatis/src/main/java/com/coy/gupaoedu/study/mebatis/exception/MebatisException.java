package com.coy.gupaoedu.study.mebatis.exception;

/**
 * @author chenck
 * @date 2019/5/7 15:18
 */
public class MebatisException extends RuntimeException {

    public MebatisException() {
        super();
    }

    public MebatisException(String message) {
        super(message);
    }

    public MebatisException(String message, Throwable cause) {
        super(message, cause);
    }

    public MebatisException(Throwable cause) {
        super(cause);
    }

}
