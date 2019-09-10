package com.coy.gupaoedu.study.mybatisplus.common.domain;


import com.coy.gupaoedu.study.mybatisplus.common.consts.ResponseCodeEnum;

import java.io.Serializable;
import java.util.List;

/**
 * 分页响应出参对象
 *
 * @author chenck
 * @date 2019/9/5 10:37
 */
public class PageResponse<T> implements Serializable {

    private static final long serialVersionUID = -2257027771150025406L;
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
    private List<T> data;
    /**
     * 总记录数
     */
    private Long total;

    public PageResponse() {
    }

    public PageResponse(List<T> data) {
        this.data = data;
    }

    public PageResponse(String code, String msg) {
        this.code = code;
        this.msg = msg;
    }

    public PageResponse(ResponseCodeEnum codeEnum, String msg) {
        this.code = codeEnum.getCode();
        this.msg = msg;
    }

    public PageResponse(List<T> data, String code, String msg) {
        this.data = data;
        this.code = code;
        this.msg = msg;
    }

    public PageResponse(List<T> data, Long total) {
        this.data = data;
        this.total = total;
    }

    /**
     * 设置默认错误码和错误描述
     */
    public PageResponse<T> error(String msg) {
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

    public List<T> getData() {
        return data;
    }

    public void setData(List<T> data) {
        this.data = data;
    }

    public Long getTotal() {
        return total;
    }

    public void setTotal(Long total) {
        this.total = total;
    }
}
