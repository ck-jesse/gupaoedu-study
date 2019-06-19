package com.coy.gupaoedu.study.tomcat.netty.http;

/**
 * @author chenck
 * @date 2019/6/19 10:50
 */
public abstract class NettyServlet {

    public void service(NettyRequest request, NettyResponse response) throws Exception {
        if ("GET".equalsIgnoreCase(request.getMethod())) {
            doGet(request, response);
        } else {
            doPost(request, response);
        }
    }

    public abstract void doGet(NettyRequest request, NettyResponse response) throws Exception;

    public abstract void doPost(NettyRequest request, NettyResponse response) throws Exception;
}
