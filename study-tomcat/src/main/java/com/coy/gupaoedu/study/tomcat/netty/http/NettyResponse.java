package com.coy.gupaoedu.study.tomcat.netty.http;

import io.netty.buffer.Unpooled;
import io.netty.channel.ChannelHandlerContext;
import io.netty.handler.codec.http.DefaultFullHttpResponse;
import io.netty.handler.codec.http.FullHttpResponse;
import io.netty.handler.codec.http.HttpResponseStatus;
import io.netty.handler.codec.http.HttpVersion;

/**
 * @author chenck
 * @date 2019/6/19 10:51
 */
public class NettyResponse {

    // SocketChannel的封装
    private ChannelHandlerContext ctx;

    public NettyResponse(ChannelHandlerContext ctx) {
        this.ctx = ctx;
    }

    public void write(String out) throws Exception {
        try {
            if (out == null || out.length() == 0) {
                return;
            }
            // 设置 http协议及请求头信息
            FullHttpResponse response = new DefaultFullHttpResponse(
                    // 设置http版本为1.1
                    HttpVersion.HTTP_1_1,
                    // 设置响应状态码
                    HttpResponseStatus.OK,
                    // 将输出值写出 编码为UTF-8
                    Unpooled.wrappedBuffer(out.getBytes("UTF-8")));

            response.headers().set("Content-Type", "text/html;");
            // 当前是否支持长连接
//            if (HttpUtil.isKeepAlive(r)) {
//                // 设置连接内容为长连接
//                response.headers().set(CONNECTION, HttpHeaderValues.KEEP_ALIVE);
//            }
            ctx.write(response);
        } finally {
            ctx.flush();
            ctx.close();
        }
    }
}
