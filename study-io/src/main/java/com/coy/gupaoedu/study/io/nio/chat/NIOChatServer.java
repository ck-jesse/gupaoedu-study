package com.coy.gupaoedu.study.io.nio.chat;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.nio.ByteBuffer;
import java.nio.channels.Channel;
import java.nio.channels.SelectionKey;
import java.nio.channels.Selector;
import java.nio.channels.ServerSocketChannel;
import java.nio.channels.SocketChannel;
import java.nio.charset.Charset;
import java.util.HashSet;
import java.util.Iterator;
import java.util.Set;

/**
 * 网络多客户端聊天室
 * 功能1： 客户端通过Java NIO连接到服务端，支持多客户端的连接
 * 功能2：客户端初次连接时，服务端提示输入昵称，如果昵称已经有人使用，提示重新输入，如果昵称唯一，则登录成功，之后发送消息都需要按照规定格式带着昵称发送消息
 * 功能3：客户端登录后，发送已经设置好的欢迎信息和在线人数给客户端，并且通知其他客户端该客户端上线
 * 功能4：服务器收到已登录客户端输入内容，转发至其他登录客户端。
 */
public class NIOChatServer {

    private int port = 8080;
    private Charset charset = Charset.forName("UTF-8");
    //用来记录在线人数，以及昵称
    private static HashSet<String> users = new HashSet<String>();

    private static String USER_EXIST = "系统提示：该昵称已经存在，请换一个昵称";
    //相当于自定义协议格式，与客户端协商好
    private static String USER_CONTENT_SPILIT = "#@#";

    private Selector selector = null;

    public NIOChatServer(int port) throws IOException {

        this.port = port;

        ServerSocketChannel server = ServerSocketChannel.open();

        server.bind(new InetSocketAddress(this.port));
        // 设置false，表示异步IO
        server.configureBlocking(false);

        selector = Selector.open();

        // 将通道 Channel 注册到 Selector 选择器上（内部有一个数据存放Channel），并且设置通道关心的I/O事件类型（如accept/read/write）
        server.register(selector, SelectionKey.OP_ACCEPT);

        System.out.println("服务已启动，监听端口是：" + this.port);
    }

    /*
     * 开始监听
     */
    public void listen() throws IOException {
        while (true) {
            // select()会阻塞，知道至少有一个已注册的事件发生，当一个或者更多的事件发生时， select() 方法将返回所发生的事件的数量。
            int wait = selector.select();
            if (wait == 0) continue;

            // 可以通过这个方法，获取已经发生了某事件的通道的集合
            Set<SelectionKey> keys = selector.selectedKeys();

            // 通过迭代，每次处理一个事件
            Iterator<SelectionKey> iterator = keys.iterator();
            while (iterator.hasNext()) {
                SelectionKey key = (SelectionKey) iterator.next();
                iterator.remove();
                process(key);
            }
        }

    }


    public void process(SelectionKey key) throws IOException {
        // 表示接受了一个新的连接
        if (key.isAcceptable()) {
            ServerSocketChannel server = (ServerSocketChannel) key.channel();
            // 从ServerSocketChannel上获取一个已经连接的SocketChannel
            // 也就是说，不用担心 accept() 操作会阻塞：
            SocketChannel client = server.accept();

            // 非阻塞模式，异步
            client.configureBlocking(false);

            // 将SocketChannel注册到选择器，并设置为读取模式
            // 收到一个连接请求，然后起一个SocketChannel，并注册到selector上，之后这个连接的数据，就由这个SocketChannel处理
            client.register(selector, SelectionKey.OP_READ);

            // 将此对应的channel设置为准备接受其他客户端请求
            key.interestOps(SelectionKey.OP_ACCEPT);

            //System.out.println("有客户端连接，IP地址为 :" + client.getRemoteAddress());
            client.write(charset.encode("请输入你的昵称"));
        }
        // 处理来自客户端的数据读取请求
        if (key.isReadable()) {
            // 返回该SelectionKey对应的 Channel，其中有数据需要读取
            SocketChannel client = (SocketChannel) key.channel();
            ByteBuffer buff = ByteBuffer.allocate(1024);
            StringBuilder content = new StringBuilder();
            try {
                while (client.read(buff) > 0) {
                    // 翻转，从写模式切换到读模式，以便读取数据
                    buff.flip();
                    content.append(charset.decode(buff));
                }
                //System.out.println("从IP地址为：" + client.getRemoteAddress() + "的获取到消息: " + content);

                // 将此对应的channel设置为准备下一次接受数据
                key.interestOps(SelectionKey.OP_READ);
            } catch (IOException io) {
                key.cancel();
                if (key.channel() != null) {
                    key.channel().close();
                }
            }
            if (content.length() > 0) {
                String[] arrayContent = content.toString().split(USER_CONTENT_SPILIT);
                // 注册用户
                if (arrayContent != null && arrayContent.length == 1) {
                    String nickName = arrayContent[0];
                    if (users.contains(nickName)) {
                        client.write(charset.encode(USER_EXIST));
                    } else {
                        users.add(nickName);
                        int onlineCount = onlineCount();
                        String message = "欢迎 " + nickName + " 进入聊天室! 当前在线人数:" + onlineCount;
                        System.out.println(message);
                        broadCast(null, message);
                    }
                }
                // 注册完了，发送消息
                else if (arrayContent != null && arrayContent.length > 1) {
                    String nickName = arrayContent[0];
                    String message = content.substring(nickName.length() + USER_CONTENT_SPILIT.length());
                    message = nickName + " 说 " + message;
                    if (users.contains(nickName)) {
                        // 不回发给发送此内容的客户端
                        broadCast(client, message);
                    }
                }
            }

        }
    }

    /**
     * 在线人数
     */
    public int onlineCount() {
        int res = 0;
        for (SelectionKey key : selector.keys()) {
            Channel target = key.channel();
            // 当Channel为SocketChannel时，则认为是一个客户端
            if (target instanceof SocketChannel) {
                res++;
            }
        }
        return res;
    }

    /**
     * 将消息广播给其他的client
     */
    public void broadCast(SocketChannel client, String content) throws IOException {
        // 广播数据到所有的SocketChannel中
        for (SelectionKey key : selector.keys()) {
            Channel targetchannel = key.channel();
            // 如果client不为空，不回发给发送此内容的客户端
            // 将消息发送给其他客户端，不发送给自己
            if (targetchannel instanceof SocketChannel && targetchannel != client) {
                SocketChannel target = (SocketChannel) targetchannel;
                target.write(charset.encode(content));
            }
        }
    }


    public static void main(String[] args) throws IOException {
        new NIOChatServer(8080).listen();
    }
}
