package com.coy.gupaoedu.study.server.rpc.nio;

import com.coy.gupaoedu.study.server.rpc.RpcInvoker;
import com.coy.gupaoedu.study.server.rpc.RpcServer;
import com.coy.gupaoedu.study.server.serializer.JavaSerializer;
import com.coy.gupaoedu.study.server.serializer.Serializer;
import lombok.extern.slf4j.Slf4j;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.nio.ByteBuffer;
import java.nio.channels.SelectionKey;
import java.nio.channels.Selector;
import java.nio.channels.ServerSocketChannel;
import java.nio.channels.SocketChannel;
import java.util.Iterator;
import java.util.Set;

/**
 * @author chenck
 * @date 2020/6/8 13:13
 */
@Slf4j
public class RpcNioServer implements RpcServer {

    private final int port;
    private RpcInvoker rpcInvoker;

    private Selector selector;
    //缓冲区 Buffer 等候区
    private ByteBuffer buffer;

    private Serializer serializer;

    public RpcNioServer(int port, RpcInvoker rpcInvoker) {
        this.serializer = new JavaSerializer();
        this.port = port;
        this.rpcInvoker = rpcInvoker;
        buffer = ByteBuffer.allocate(1024);
        try {
            selector = Selector.open();

            ServerSocketChannel server = ServerSocketChannel.open();
            server.bind(new InetSocketAddress(this.port));

            //BIO 升级版本 NIO，为了兼容BIO，NIO模型默认是采用阻塞式
            server.configureBlocking(false);

            server.register(selector, SelectionKey.OP_ACCEPT);

            this.start();
        } catch (IOException e) {
            log.error("", e);
        }
    }

    /**
     * 监听客户端请求
     */
    @Override
    public void start() {
        try {
            System.out.println("NIO RPC Registry 已启动，监听的端口是：" + this.port);
            while (true) {
                int readyChannels = selector.select();
                if (readyChannels == 0) {
                    continue;
                }
                Set<SelectionKey> keys = selector.selectedKeys();
                Iterator<SelectionKey> iter = keys.iterator();
                //同步体现在这里，因为每次只能拿一个key，每次只能处理一种状态
                //每一个key代表一种状态
                while (iter.hasNext()) {
                    SelectionKey key = iter.next();
                    iter.remove();
                    process(key);
                }
            }
        } catch (Exception e) {
            log.error("", e);
        }
    }

    //具体办业务的方法
    //每一次轮询就是调用一次process方法，而每一次调用，只能干一件事
    //在同一时间点，只能干一件事
    private void process(SelectionKey key) throws Exception {
        if (key.isAcceptable()) {
            ServerSocketChannel server = (ServerSocketChannel) key.channel();
            SocketChannel channel = server.accept();//accept()非阻塞，不管你数据有没有准备好
            channel.configureBlocking(false);//设置为非阻塞
            channel.register(key.selector(), SelectionKey.OP_READ);//当数据准备就绪的时候，将状态改为可读
        } else if (key.isReadable()) {
            SocketChannel channel = (SocketChannel) key.channel();//从多路复用器中拿到客户端的引用
            channel.configureBlocking(false);
            int len = channel.read(buffer);
            if (len > 0) {
                buffer.flip();// 翻转，Buffer从写模式变成读模式
                ByteBuffer result = new RpcNioRequestHandler(serializer, rpcInvoker, buffer).call();
                // 方式一：直接write
                //channel.write(result);

                // 方式二：在选择器上注册write事件
                channel.register(key.selector(), SelectionKey.OP_WRITE, result);
            }
        } else if (key.isWritable()) {
            SocketChannel channel = (SocketChannel) key.channel();
            channel.configureBlocking(false);

            ByteBuffer result = (ByteBuffer) key.attachment();
            channel.write(result);
        }
    }
}
