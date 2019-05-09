package com.coy.gupaoedu.study.mebatis.exception;

/**
 * @author chenck
 * @date 2019/5/9 13:40
 */
public class PluginException extends MebatisException {

    public PluginException() {
        super();
    }

    public PluginException(String message) {
        super(message);
    }

    public PluginException(String message, Throwable cause) {
        super(message, cause);
    }

    public PluginException(Throwable cause) {
        super(cause);
    }
}
