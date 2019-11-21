package com.study.jmeter.common;

import java.io.Serializable;

/**
 * @author chenck
 * @date 2019/11/20 19:08
 */
public class ServiceResult<T> implements Serializable {

    /**
     * 返回码
     */
    private String code;

    /**
     * 描述信息
     */
    private String msg;

    /**
     * 返回业务数据
     */
    private T data;


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