package com.coy.gupaoedu.study.tomcat.netty;


import com.coy.gupaoedu.study.tomcat.WebXmlParseUtil;
import com.coy.gupaoedu.study.tomcat.netty.http.NettyRequest;
import com.coy.gupaoedu.study.tomcat.netty.http.NettyResponse;
import com.coy.gupaoedu.study.tomcat.netty.http.NettyServlet;
import io.netty.bootstrap.ServerBootstrap;
import io.netty.channel.ChannelFuture;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelInboundHandlerAdapter;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.ChannelOption;
import io.netty.channel.EventLoopGroup;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.SocketChannel;
import io.netty.channel.socket.nio.NioServerSocketChannel;
import io.netty.handler.codec.http.HttpRequest;
import io.netty.handler.codec.http.HttpRequestDecoder;
import io.netty.handler.codec.http.HttpResponseEncoder;

import java.util.HashMap;
import java.util.Map;

/**
 * 简易版的tomcat
 * 基于Netty Nio来实现
 * <p>
 * tomcat7中自动选取BIO来实现
 * tomcat8中自动选取NIO来实现
 *
 * @author chenck
 * @date 2019/6/18 20:28
 */
public class NettyTomcat {
    private int port;

    private Map<String, NettyServlet> servletMapping = new HashMap<String, NettyServlet>();

    public NettyTomcat(int port) {
        this.port = port;
    }

    public void start() {
        //1、加载配置文件，初始化ServeltMapping
        servletMapping = WebXmlParseUtil.<NettyServlet>parse("web-netty.properties");

        // Netty封装了NIO，Reactor模型，Boss，worker
        // boos线程 默认线程数为cup*2
        EventLoopGroup bossGroup = new NioEventLoopGroup();
        // work线程
        EventLoopGroup workGroup = new NioEventLoopGroup();

        try {
            // Netty服务
            ServerBootstrap server = new ServerBootstrap();
            // 链式调用
            // 设置线程模型
            server.group(bossGroup, workGroup)
                    // 主线程通道处理类，底层用反射，针对ServerSocketChannel
                    .channel(NioServerSocketChannel.class)
                    // 子线程处理类，针对SocketChannel
                    .childHandler(new ChannelInitializer<SocketChannel>() {
                        /**
                         * 客户端通道初始化处理
                         * Channel注册后将调用此方法，该方法返回后，此实例将从管道pipeline中删除
                         */
                        @Override
                        protected void initChannel(SocketChannel ch) throws Exception {
                            // 无锁化串行编程
                            // 注意：Netty对HTTP协议的封装，顺序有要求
                            // HttpResponseEncoder 编码器
                            ch.pipeline().addLast(new HttpResponseEncoder());
                            // HttpRequestDecoder 解码器
                            ch.pipeline().addLast(new HttpRequestDecoder());
                            // 业务逻辑处理
                            ch.pipeline().addLast(new NettyTomcatHandler());
                        }
                    })
                    // 针对主线程配置线程最大数量
                    .option(ChannelOption.SO_BACKLOG, 128)
                    // 针对子线程配置保持长连接
                    .childOption(ChannelOption.SO_KEEPALIVE, true);
            // 启动服务器
            ChannelFuture cf = server.bind(port).sync();
            System.out.println("Netty Tomcat 已启动，监听的端口是：" + this.port);
            cf.channel().closeFuture().sync();
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            // 关闭线程池
            bossGroup.shutdownGracefully();
            workGroup.shutdownGracefully();
        }
    }

    /**
     * 处理器
     */
    public class NettyTomcatHandler extends ChannelInboundHandlerAdapter {
        @Override
        public void channelRead(ChannelHandlerContext ctx, Object msg) throws Exception {
            if (msg instanceof HttpRequest) {
                HttpRequest req = (HttpRequest) msg;

                // 转交给我们自己的request实现
                NettyRequest request = new NettyRequest(ctx, req);
                // 转交给我们自己的response实现
                NettyResponse response = new NettyResponse(ctx);
                // 实际业务处理
                String url = request.getUrl();
                if (url.indexOf("?") != -1) {
                    url = url.substring(0, url.indexOf("?"));
                }

                if (servletMapping.containsKey(url)) {
                    servletMapping.get(url).service(request, response);
                } else {
                    response.write("404 - Not Found");
                }
            }
        }

        @Override
        public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) throws Exception {

        }
    }

    /**
     * 简易版的tomcat
     * 使用：
     * 浏览器中输入地址： http://127.0.0.1:8080/firstServlet.do
     * 浏览器中输入地址： http://127.0.0.1:8080/secondServlet.do
     */
    public static void main(String[] args) {
        new NettyTomcat(8080).start();
    }
}
