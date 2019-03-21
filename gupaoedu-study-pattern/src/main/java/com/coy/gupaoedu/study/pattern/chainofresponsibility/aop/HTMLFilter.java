package com.coy.gupaoedu.study.pattern.chainofresponsibility.aop;

/**
 * @author chenck
 * @date 2019/3/21 20:23
 */
public class HTMLFilter implements Filter {

    /**
     * 将请求消息中的"<>"替换成"[]"
     *
     * @param
     * @author chenck
     * @date 2019/3/21 20:24
     */
    @Override
    public void doFilter(Request request, Response response, FilterChain chain) {
        System.out.println("预处理HTMLFilter");
        // process HTML Tag
        String msg = request.getRequest().replace("<", "[").replace(">", "]");
        request.setRequest(msg);

        chain.doFilter(request, response);

        response.setResponse(response.getResponse() + "--->HTMLFilter");
    }
}
