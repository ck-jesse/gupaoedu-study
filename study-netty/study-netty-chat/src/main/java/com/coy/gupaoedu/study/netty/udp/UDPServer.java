package com.coy.gupaoedu.study.netty.udp;

import io.netty.bootstrap.Bootstrap;
import io.netty.channel.Channel;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.ChannelOption;
import io.netty.channel.EventLoopGroup;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.nio.NioDatagramChannel;

/**
 * @author chenck
 * @date 2019/11/22 16:58
 */
public class UDPServer {

    public static void main(String[] args) {
        EventLoopGroup eventLoopGroup = new NioEventLoopGroup();
        try {
            // udp不能使用ServerBootstrap
            Bootstrap bootstrap = new Bootstrap()
                    .group(eventLoopGroup)
                    // 设置UDP通道NioDatagramChannel（无连接的）
                    .channel(NioDatagramChannel.class)
                    // 设置 SO_BROADCAST 套接字选项 -> 广播
                    .option(ChannelOption.SO_BROADCAST, true)
                    .option(ChannelOption.SO_BACKLOG, 128)
                    .option(ChannelOption.SO_RCVBUF, 1024 * 1024)// 设置UDP读缓冲区为1M
                    .option(ChannelOption.SO_SNDBUF, 1024 * 1024)// 设置UDP写缓冲区为1M
                    .handler(new ChannelInitializer<Channel>() {
                        @Override
                        protected void initChannel(Channel ch) throws Exception {
                            ch.pipeline().addLast(new LogEventServerDecoder())
                                    .addLast(new LogEventServerHandler());

                        }
                    });
            bootstrap.bind(9999).sync().channel().closeFuture().await();
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            eventLoopGroup.shutdownGracefully();
            System.out.println("UDP server close");
        }
    }
}
