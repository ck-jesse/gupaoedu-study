package com.coy.gupaoedu.study.server.rpc.netty;

import com.coy.gupaoedu.study.server.rpc.RpcInvoker;
import io.netty.bootstrap.ServerBootstrap;
import io.netty.channel.ChannelFuture;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.ChannelOption;
import io.netty.channel.EventLoopGroup;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.SocketChannel;
import io.netty.channel.socket.nio.NioServerSocketChannel;
import io.netty.handler.codec.LengthFieldBasedFrameDecoder;
import io.netty.handler.codec.LengthFieldPrepender;
import io.netty.handler.codec.serialization.ClassResolvers;
import io.netty.handler.codec.serialization.ObjectDecoder;
import io.netty.handler.codec.serialization.ObjectEncoder;

/**
 * 基于netty的rpc注册中心
 *
 * @author chenck
 * @date 2019/6/19 15:01
 */
public class RpcNettyServer {

    private final int port;

    private RpcInvoker rpcInvoker;

    public RpcNettyServer(int port) {
        this.port = port;
    }

    public RpcNettyServer(int port, RpcInvoker rpcInvoker) {
        this.port = port;
        this.rpcInvoker = rpcInvoker;
    }

    public void start() {
        // boos线程
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
                            // 自定义协议解码器
                            /** 入参有5个，分别解释如下
                             maxFrameLength：框架的最大长度。如果帧的长度大于此值，则将抛出TooLongFrameException。
                             lengthFieldOffset：长度字段的偏移量：即对应的长度字段在整个消息数据中得位置
                             lengthFieldLength：长度字段的长度。如：长度字段是int型表示，那么这个值就是4（long型就是8）
                             lengthAdjustment：要添加到长度字段值的补偿值
                             initialBytesToStrip：从解码帧中去除的第一个字节数
                             */
                            ch.pipeline().addLast(new LengthFieldBasedFrameDecoder(Integer.MAX_VALUE, 0, 4, 0, 4));
                            //自定义协议编码器
                            ch.pipeline().addLast(new LengthFieldPrepender(4));
                            //对象参数类型编码器
                            ch.pipeline().addLast("encoder", new ObjectEncoder());
                            //对象参数类型解码器
                            ch.pipeline().addLast("decoder", new ObjectDecoder(Integer.MAX_VALUE, ClassResolvers.cacheDisabled(null)));
                            ch.pipeline().addLast(new RpcNettyRequestHandler(rpcInvoker));
                        }
                    })
                    // 针对主线程配置线程最大数量
                    .option(ChannelOption.SO_BACKLOG, 128)
                    // 针对子线程配置保持长连接
                    .childOption(ChannelOption.SO_KEEPALIVE, true);
            // 启动服务器
            ChannelFuture cf = server.bind(port).sync();
            System.out.println("Netty RPC Registry 已启动，监听的端口是：" + this.port);
            cf.channel().closeFuture().sync();
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            // 关闭线程池
            bossGroup.shutdownGracefully();
            workGroup.shutdownGracefully();
        }
    }
}
