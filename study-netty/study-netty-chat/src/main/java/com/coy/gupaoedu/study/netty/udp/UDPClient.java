package com.coy.gupaoedu.study.netty.udp;

import io.netty.bootstrap.Bootstrap;
import io.netty.channel.Channel;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.ChannelOption;
import io.netty.channel.EventLoopGroup;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.nio.NioDatagramChannel;

import java.net.InetSocketAddress;

/**
 * @author chenck
 * @date 2019/11/22 16:58
 */
public class UDPClient {

    /**
     * 单播(unicast): 是指封包在计算机网络的传输中，目的地址为单一目标的一种传输方式。它是现今网络应用最为广泛，通常所使用的网络协议或服务大多采用单播传输，例如一切基于TCP的协议。
     * 组播(multicast): 也叫多播， 多点广播或群播。 指把信息同时传递给一组目的地址。它使用策略是最高效的，因为消息在每条网络链路上只需传递一次，而且只有在链路分叉的时候，消息才会被复制。
     * 广播(broadcast):是指封包在计算机网络中传输时，目的地址为网络中所有设备的一种传输方式。实际上，这里所说的“所有设备”也是限定在一个范围之中，称为“广播域”。
     * 任播(anycast):是一种网络寻址和路由的策略，使得资料可以根据路由拓朴来决定送到“最近”或“最好”的目的地。
     * <p>
     * 在Linux运行ifconfig, 如果网卡信息中包含UP BROADCAST RUNNING MULTICAST，则支持广播和组播。
     * <p>
     * 广播地址(Broadcast Address)是专门用于同时向网络中所有工作站进行发送的一个地址。
     * 在使用TCP/IP 协议的网络中，主机标识段host ID 为全1 的IP 地址为广播地址，广播的分组传送给host ID段所涉及的所有计算机。
     * 例如，对于10.1.1.0 （255.255.255.0 ）网段，其广播地址为10.1.1.255 （255 即为2 进制的11111111 ），当发出一个目的地址为10.1.1.255 的分组（封包）时，它将被分发给该网段上的所有计算机。
     * 本地广播地址为255.255.255.255。
     */
    public static void main(String[] args) {
        // 在Java UDP中单播与广播的代码是相同的,要实现具有广播功能的程序只需要使用广播地址即可。
        // 单播地址
        InetSocketAddress socketAddress = new InetSocketAddress("127.0.0.1", 9999);
        // 广播地址
//        InetSocketAddress socketAddress = new InetSocketAddress("255.255.255.255", 9999);

        EventLoopGroup eventLoopGroup = new NioEventLoopGroup();
        try {
            Bootstrap bootstrap = new Bootstrap()
                    .group(eventLoopGroup)
                    // 设置UDP通道NioDatagramChannel（无连接的）
                    .channel(NioDatagramChannel.class)
                    // 设置 SO_BROADCAST 套接字选项 -> 广播
                    .option(ChannelOption.SO_BROADCAST, true)
                    .handler(new ChannelInitializer<Channel>() {
                        @Override
                        protected void initChannel(Channel ch) throws Exception {
                            ch.pipeline().addLast(new LogEventClientEncoder(socketAddress));// 编码
                            ch.pipeline().addLast(new LogEventClientHandler());// 消息处理器
                        }
                    });

            // 不需要建立连接，绑定0端口是表示系统为我们设置端口监听
            bootstrap.bind(0).sync().channel().closeFuture().await();
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            eventLoopGroup.shutdownGracefully();
            System.out.println("UDP client close");
        }
    }
}
