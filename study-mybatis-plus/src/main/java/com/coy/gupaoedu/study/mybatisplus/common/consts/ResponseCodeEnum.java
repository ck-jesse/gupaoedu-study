package com.coy.gupaoedu.study.mybatisplus.common.consts;

/**
 * 响应码定义
 * <p>
 * 注意：如果需要根据错误码来处理不同的业务逻辑，那么可以自定义一个错误码，其他情况一般无需再自定义错误码
 *
 * @author chenck
 * @date 2019/9/5 11:21
 */
public enum ResponseCodeEnum {
    SUCCESS("20000", "success"),
    ERROR("50000", "system error"),
    ERROR_PARAM("50001", "parameter error"),
    ERROR_NOT_LOGIN("50002", "user not logged in."),
    ERROR_SESSION_TIMEOUT("50003", "session time out."),
    ERROR_RESOURCE_NOT_FOUND("50004", "Resource not found."),
    ;

    private String code;
    private String msg;

    private ResponseCodeEnum(String code, String msg) {
        this.code = code;
        this.msg = msg;
    }

    public String getCode() {
        return this.code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public String getMsg() {
        return this.msg;
    }

    public void setMsg(String msg) {
        this.msg = msg;
    }
}
