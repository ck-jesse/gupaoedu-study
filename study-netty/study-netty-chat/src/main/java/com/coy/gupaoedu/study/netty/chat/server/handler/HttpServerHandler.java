package com.coy.gupaoedu.study.netty.chat.server.handler;

import io.netty.channel.Channel;
import io.netty.channel.ChannelFuture;
import io.netty.channel.ChannelFutureListener;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.DefaultFileRegion;
import io.netty.channel.SimpleChannelInboundHandler;
import io.netty.handler.codec.http.DefaultHttpResponse;
import io.netty.handler.codec.http.FullHttpRequest;
import io.netty.handler.codec.http.HttpHeaders;
import io.netty.handler.codec.http.HttpResponse;
import io.netty.handler.codec.http.HttpResponseStatus;
import io.netty.handler.codec.http.LastHttpContent;
import lombok.extern.slf4j.Slf4j;

import java.io.File;
import java.io.RandomAccessFile;
import java.net.URL;

@Slf4j
public class HttpServerHandler extends SimpleChannelInboundHandler<FullHttpRequest> {

    //获取class路径
    private URL baseURL = HttpServerHandler.class.getResource("");
    private final String webroot = "webroot";

    /**
     * 获取文件
     */
    private File getResource(String fileName) throws Exception {
        String basePath = baseURL.toURI().toString();
        int start = basePath.indexOf("classes/");
        basePath = (basePath.substring(0, start) + "/" + "classes/").replaceAll("/+", "/");

        String path = basePath + webroot + "/" + fileName;
        path = !path.contains("file:") ? path : path.substring(5);
        path = path.replaceAll("//", "/");
        return new File(path);
    }

    @Override
    public void channelRead0(ChannelHandlerContext ctx, FullHttpRequest request) throws Exception {
        String uri = request.getUri();

        RandomAccessFile file = null;
        try {
            String page = uri.equals("/") ? "chat.html" : uri;
            // 根据url在服务器上找到对应的文件
            file = new RandomAccessFile(getResource(page), "r");
        } catch (Exception e) {
            // 文件不存在时，则认为请求是别的协议（如WebSocket），所以直接传递给下一个ChannelInboundHandler
            // 如果请求了WebSocket协议升级，则增加引用技术，并将它传递给下一个ChannelInboundHandler
            // 之所以需要调用retain（）方法，是因为调用channelRead（）方法完成之后，它将调用FullHttpRequest对象上的release（）方法以释放它的资源。
            ctx.fireChannelRead(request.retain());
            return;
        }

        HttpResponse response = new DefaultHttpResponse(request.getProtocolVersion(), HttpResponseStatus.OK);
        String contextType = "text/html;";
        if (uri.endsWith(".css")) {
            contextType = "text/css;";
        } else if (uri.endsWith(".js")) {
            contextType = "text/javascript;";
        } else if (uri.toLowerCase().matches(".*\\.(jpg|png|gif)$")) {
            String ext = uri.substring(uri.lastIndexOf("."));
            contextType = "image/" + ext;
        }
        response.headers().set(HttpHeaders.Names.CONTENT_TYPE, contextType + "charset=utf-8;");

        boolean keepAlive = HttpHeaders.isKeepAlive(request);

        if (keepAlive) {
            response.headers().set(HttpHeaders.Names.CONTENT_LENGTH, file.length());
            response.headers().set(HttpHeaders.Names.CONNECTION, HttpHeaders.Values.KEEP_ALIVE);
        }
        ctx.write(response);

        ctx.write(new DefaultFileRegion(file.getChannel(), 0, file.length()));

        ChannelFuture future = ctx.writeAndFlush(LastHttpContent.EMPTY_LAST_CONTENT);
        if (!keepAlive) {
            future.addListener(ChannelFutureListener.CLOSE);
        }

        file.close();
    }

    @Override
    public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause)
            throws Exception {
        Channel client = ctx.channel();
        log.info("Client:" + client.remoteAddress() + "异常");
        // 当出现异常就关闭连接
        cause.printStackTrace();
        ctx.close();
    }
}

