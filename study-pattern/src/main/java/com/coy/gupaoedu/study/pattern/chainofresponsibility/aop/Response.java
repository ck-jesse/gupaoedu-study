package com.coy.gupaoedu.study.pattern.chainofresponsibility.aop;

/**
 * 对响应消息的抽象
 *
 * @author chenck
 * @date 2019/3/21 20:01
 */
public class Response {

    /**
     * 响应消息
     */
    private String response;

    public String getResponse() {
        return response;
    }

    public void setResponse(String response) {
        this.response = response;
    }

}
