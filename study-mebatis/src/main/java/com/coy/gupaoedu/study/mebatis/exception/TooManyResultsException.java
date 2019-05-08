package com.coy.gupaoedu.study.mebatis.exception;

/**
 * @author chenck
 * @date 2019/5/6 21:00
 */
public class TooManyResultsException extends MebatisException {

    public TooManyResultsException() {
        super();
    }

    public TooManyResultsException(String message) {
        super(message);
    }

    public TooManyResultsException(String message, Throwable cause) {
        super(message, cause);
    }

    public TooManyResultsException(Throwable cause) {
        super(cause);
    }
}
