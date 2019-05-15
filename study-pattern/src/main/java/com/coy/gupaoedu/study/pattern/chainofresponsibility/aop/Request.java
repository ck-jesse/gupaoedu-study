package com.coy.gupaoedu.study.pattern.chainofresponsibility.aop;

/**
 * 对请求消息的抽象
 *
 * @author chenck
 * @date 2019/3/21 20:01
 */
public class Request {

    /**
     * 请求消息
     */
    private String request;

    public String getRequest() {
        return request;
    }

    public void setRequest(String request) {
        this.request = request;
    }
}
