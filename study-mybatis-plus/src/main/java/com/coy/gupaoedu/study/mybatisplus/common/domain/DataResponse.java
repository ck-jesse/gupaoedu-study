package com.coy.gupaoedu.study.mybatisplus.common.domain;


import com.coy.gupaoedu.study.mybatisplus.common.consts.ResponseCodeEnum;

import java.io.Serializable;

/**
 * 基础的响应出参对象
 * <p>
 * 约定：业务接口方法的返回参数对象定义为该对象，以便统一规范
 *
 * @author chenck
 * @date 2019/9/5 10:37
 */
public class DataResponse<T> implements Serializable {

    private static final long serialVersionUID = 7505997295595095971L;

    /**
     * 响应码
     */
    private String code = ResponseCodeEnum.SUCCESS.getCode();
    /**
     * 响应描述
     */
    private String msg = ResponseCodeEnum.SUCCESS.getMsg();
    /**
     * 返回的业务数据
     */
    private T data;

    public DataResponse() {
    }

    public DataResponse(T data) {
        this.data = data;
    }

    public DataResponse(String code, String msg) {
        this.code = code;
        this.msg = msg;
    }

    public DataResponse(ResponseCodeEnum codeEnum, String msg) {
        this.code = codeEnum.getCode();
        this.msg = msg;
    }

    public DataResponse(T data, String code, String msg) {
        this.data = data;
        this.code = code;
        this.msg = msg;
    }

    /**
     * 设置默认错误码和错误描述
     */
    public DataResponse<T> error(String msg) {
        this.code = ResponseCodeEnum.ERROR.getCode();
        this.msg = msg;
        return this;
    }

    /**
     * 校验是否成功
     */
    public boolean isSucc() {
        return ResponseCodeEnum.SUCCESS.getCode().equals(code);
    }

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public String getMsg() {
        return msg;
    }

    public void setMsg(String msg) {
        this.msg = msg;
    }

    public T getData() {
        return data;
    }

    public void setData(T data) {
        this.data = data;
    }
}
