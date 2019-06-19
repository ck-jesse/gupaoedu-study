package com.coy.gupaoedu.study.tomcat.netty.http;

import io.netty.channel.ChannelHandlerContext;
import io.netty.handler.codec.http.HttpRequest;
import io.netty.handler.codec.http.QueryStringDecoder;

import java.util.List;
import java.util.Map;

/**
 * @author chenck
 * @date 2019/6/19 10:51
 */
public class NettyRequest {

    private ChannelHandlerContext ctx;

    private HttpRequest request;

    public NettyRequest(ChannelHandlerContext ctx, HttpRequest request) {
        this.ctx = ctx;
        this.request = request;
    }

    public String getUrl() {
        return request.uri();
    }

    public String getMethod() {
        return request.method().name();
    }


    public Map<String, List<String>> getParameters() {
        QueryStringDecoder decoder = new QueryStringDecoder(request.uri());
        return decoder.parameters();
    }

    public String getParameter(String name) {
        Map<String, List<String>> params = getParameters();
        List<String> param = params.get(name);
        if (null == param) {
            return null;
        }
        return param.get(0);
    }
}
