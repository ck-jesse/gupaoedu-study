package com.coy.gupaoedu.study.io.nio;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.nio.ByteBuffer;
import java.nio.channels.SelectionKey;
import java.nio.channels.Selector;
import java.nio.channels.SocketChannel;
import java.nio.charset.Charset;
import java.util.Iterator;
import java.util.Set;

/**
 * @author chenck
 * @date 2019/6/12 23:23
 */
public class NioClient {
    private SocketChannel client;
    // 轮询器
    private Selector selector;
    // 缓存区
    private ByteBuffer buffer = ByteBuffer.allocate(1024);
    //
    private Charset charset = Charset.forName("UTF-8");

    public NioClient(int port) throws IOException {
        selector = Selector.open();
        client = SocketChannel.open(new InetSocketAddress("localhost", port));
        // 绑定Ip+port
//        client.bind(new InetSocketAddress("localhost", port));
        // 配置为非阻塞
        client.configureBlocking(false);
        // 将client的读取操作注册到Selector中
        client.register(selector, SelectionKey.OP_READ);
    }

    public void listen(String msg) {
        try {
            System.out.println("client发送内容：" + msg);
            // 将数据发送给服务端
            client.write(charset.encode(msg));

            // 从服务端接收数据
            while (true) {
                int readyChannels = selector.select();
                if (readyChannels == 0) {
                    continue;
                }
                // 获取准备好的操作key
                Set<SelectionKey> selectedKeys = selector.selectedKeys();
                Iterator<SelectionKey> keyIterator = selectedKeys.iterator();
                while (keyIterator.hasNext()) {
                    SelectionKey key = (SelectionKey) keyIterator.next();
                    keyIterator.remove();
                    process(key);
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }


    public void process(SelectionKey key) throws IOException {
        // 读取操作
        if (key.isReadable()) {
            SocketChannel socketChannel = (SocketChannel) key.channel();
            // 从通道中读取数据到缓存区
            int len = socketChannel.read(buffer);
            if (len > 0) {
                // 翻转此缓存区，将limit这是为position，position设置为0
                buffer.flip();
                String content = new String(buffer.array());
                System.out.println("client读取内容：" + content);
            }
        }
    }

    public static void main(String[] args) throws IOException {
        NioClient nioClient = new NioClient(8080);
        // 启动监听
        nioClient.listen("第一个信息");
    }
}
