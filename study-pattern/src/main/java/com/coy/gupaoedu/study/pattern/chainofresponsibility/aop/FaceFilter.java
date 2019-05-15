package com.coy.gupaoedu.study.pattern.chainofresponsibility.aop;

/**
 * @author chenck
 * @date 2019/3/21 20:26
 */
public class FaceFilter implements Filter {

    /**
     * 将请求消息中的":)"替换成"笑脸"
     *
     * @param
     * @author chenck
     * @date 2019/3/21 20:26
     */
    @Override
    public void doFilter(Request request, Response response, FilterChain chain) {
        System.out.println("预处理FaceFilter");
        String msg = request.getRequest().replace(":)", "笑脸");
        request.setRequest(msg);

        chain.doFilter(request, response);

        response.setResponse(response.getResponse() + "--->FaceFilter");
    }
}
