package com.coy.gupaoedu.study.tomcat.netty.servlet;


import com.coy.gupaoedu.study.tomcat.netty.http.NettyRequest;
import com.coy.gupaoedu.study.tomcat.netty.http.NettyResponse;
import com.coy.gupaoedu.study.tomcat.netty.http.NettyServlet;

/**
 * @author chenck
 * @date 2019/6/18 20:39
 */
public class NettySecondServlet extends NettyServlet {

    public void doGet(NettyRequest request, NettyResponse response) throws Exception {
        this.doPost(request, response);
    }

    public void doPost(NettyRequest request, NettyResponse response) throws Exception {
        System.out.println(request.getUrl());
        System.out.println(request.getParameters());
        response.write("This is Second servlet");
    }

}