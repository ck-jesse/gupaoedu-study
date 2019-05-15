package com.coy.gupaoedu.study.pattern.chainofresponsibility.aop;

/**
 * @author chenck
 * @date 2019/3/21 20:25
 */
public class SensitiveFilter implements Filter {
    /**
     * 将请求消息中的"被就业"替换成"就业"
     *
     * @param
     * @author chenck
     * @date 2019/3/21 20:25
     */
    @Override
    public void doFilter(Request request, Response response, FilterChain chain) {
        System.out.println("预处理SensitiveFilter");
        String msg = request.getRequest().replace("被就业", "就业");
        request.setRequest(msg);

        chain.doFilter(request, response);

        response.setResponse(response.getResponse() + "--->SensitiveFilter");
    }
}
