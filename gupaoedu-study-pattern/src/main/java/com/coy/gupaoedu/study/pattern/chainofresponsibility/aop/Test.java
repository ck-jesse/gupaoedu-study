package com.coy.gupaoedu.study.pattern.chainofresponsibility.aop;

/**
 * AOP思想+责任链模式的一种体现
 * <p>
 * 不纯的责任链模式：在一个不纯的职责链模式中，允许某个请求被一个具体处理者部分处理后再向下传递，
 * 或者一个具体处理者处理完某请求后其后继处理者继续处理该请求，而且一个请求可以最终不被任何处理者对象所接收。
 * <p>
 * 本示例基本模拟了Java Web 中过滤器的工作流程，也反映了AOP思想和责任链模式的精髓。
 * <p>
 * 降低耦合度，使请求的发送者和接收者解耦，便于灵活的、可插拔的定义请求处理过程
 *
 * @author chenck
 * @date 2019/3/21 20:00
 */
public class Test {

    public static void main(String[] args) {
        // 待处理消息
        String msg = "大家好 :),<script>,敏感,被就业,网络授课没感觉...";

        // 设置请求消息
        Request request = new Request();
        request.setRequest(msg);

        // 设置响应消息
        Response response = new Response();
        response.setResponse("Response");

        // 设置处理链
        FilterChain chain = new FilterChain();
        chain.addFilter(new HTMLFilter()).addFilter(new SensitiveFilter())
                .addFilter(new FaceFilter());

        // 开始处理
        chain.doFilter(request, response);

        // 消息的预处理结果
        System.out.println(request.getRequest());

        // 消息的后处理结果
        System.out.println(response.getResponse());
    }
}
