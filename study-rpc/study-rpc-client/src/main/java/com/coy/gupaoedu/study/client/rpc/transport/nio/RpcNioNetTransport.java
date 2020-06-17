package com.coy.gupaoedu.study.client.rpc.transport.nio;

import com.coy.gupaoedu.study.client.rpc.transport.RpcNetTransport;
import com.coy.gupaoedu.study.server.serializer.JavaSerializer;
import com.coy.gupaoedu.study.server.serializer.Serializer;
import com.coy.gupaoedu.study.server.rpc.RpcRequest;
import com.coy.gupaoedu.study.server.rpc.RpcUrl;
import lombok.extern.slf4j.Slf4j;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.InetSocketAddress;
import java.nio.ByteBuffer;
import java.nio.channels.SelectionKey;
import java.nio.channels.Selector;
import java.nio.channels.SocketChannel;
import java.util.Iterator;
import java.util.Set;

/**
 * @author chenck
 * @date 2020/6/8 12:18
 */
@Slf4j
public class RpcNioNetTransport extends RpcNetTransport {

    protected ByteBuffer lenBuffer = ByteBuffer.allocate(1024);

    private Serializer serializer;

    private Selector selector;

    public RpcNioNetTransport() {
        this(null);
    }

    public RpcNioNetTransport(Serializer serializer) {
        if (null == serializer) {
            this.serializer = new JavaSerializer();
        } else {
            this.serializer = serializer;
        }
        try {
            selector = Selector.open();
        } catch (IOException e) {
            log.error("", e);
        }
    }

    @Override
    public Object rpcInvoke(RpcRequest request, RpcUrl rpcUrl) {
        ObjectInputStream objectInputStream = null;
        ObjectOutputStream objectOutputStream = null;
        try {
            // create a socket channel
            SocketChannel sock = SocketChannel.open(new InetSocketAddress(rpcUrl.getHost(), rpcUrl.getPort()));
            sock.configureBlocking(false);
            sock.socket().setSoLinger(false, -1);
            sock.socket().setTcpNoDelay(true);

            sock.register(selector, SelectionKey.OP_READ);
            // 序列化
            byte[] bytes = serializer.serialize(request, RpcRequest.class);
            // 将数据发送给服务端
            sock.write(ByteBuffer.wrap(bytes));

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
                    SelectionKey key = keyIterator.next();
                    keyIterator.remove();
                    if (key.isReadable()) {
                        SocketChannel socketChannel = (SocketChannel) key.channel();
                        socketChannel.configureBlocking(false);
                        // 从通道中读取数据到缓存区
                        int len = socketChannel.read(lenBuffer);
                        if (len < 0) {
                            throw new IOException("Unable to read additional data from server , likely server has closed socket");
                        }
                        if (len == 0) {
                            continue;
                        }

                        // 翻转此缓存区，将limit这是为position，position设置为0
                        lenBuffer.flip();

                        // 反序列化
                        Object object = serializer.deserialize(lenBuffer.array(), Object.class);
                        return object;
                    }
                }
            }

        } catch (IOException | ClassNotFoundException e) {
            log.error("", e);
        } finally {
            if (objectInputStream != null) {
                try {
                    objectInputStream.close();
                } catch (IOException e) {
                    log.error("", e);
                }
            }
            if (objectOutputStream != null) {
                try {
                    objectOutputStream.close();
                } catch (IOException e) {
                    log.error("", e);
                }
            }
        }
        return null;
    }

}
