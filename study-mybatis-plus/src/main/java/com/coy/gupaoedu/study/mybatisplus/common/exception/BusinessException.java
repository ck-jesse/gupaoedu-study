package com.coy.gupaoedu.study.mybatisplus.common.exception;

import com.coy.gupaoedu.study.mybatisplus.common.consts.ResponseCodeEnum;
import org.apache.commons.lang3.builder.ToStringBuilder;

/**
 * @author: XuZhu
 * Email: duanyg@akulaku.com
 * Create on: 2019-08-22
 * Description:
 */
public class BusinessException extends RuntimeException {

    private static final long serialVersionUID = 3936204046357731348L;

    private static final String DEFAULT_CODE = ResponseCodeEnum.ERROR.getCode();

    private String errorCode;

    private String errorMessage;

    public BusinessException(final Throwable cause) {
        super(cause.getMessage(), cause);
        this.errorCode = DEFAULT_CODE;
        this.errorMessage = cause.getMessage();
    }

    public BusinessException(final String message) {
        super(message);
        this.errorCode = DEFAULT_CODE;
        this.errorMessage = message;
    }

    public BusinessException(final String code, final String message) {
        super(message);
        this.errorCode = code;
        this.errorMessage = message;
    }

    public BusinessException(final String message, final Throwable cause) {
        super(message, cause);
        this.errorCode = DEFAULT_CODE;
        this.errorMessage = message;
    }

    public BusinessException(final String code, final String message, final Throwable cause) {
        this(message, cause);
        this.errorCode = code;
    }

    public String getErrorCode() {
        return errorCode;
    }

    public void setErrorCode(String errorCode) {
        this.errorCode = errorCode;
    }

    public String getErrorMessage() {
        return errorMessage;
    }

    public void setErrorMessage(String errorMessage) {
        this.errorMessage = errorMessage;
    }

    @Override
    public String toString() {
        return ToStringBuilder.reflectionToString(this);
    }

}
