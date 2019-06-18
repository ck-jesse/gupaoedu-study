package com.coy.gupaoedu.study.io.bio;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

/**
 * BIO 服务端
 * 同步阻塞IO模型
 *
 * @author chenck
 * @date 2019/6/12 21:41
 */
public class BioServer {

    private static ExecutorService executorService = Executors.newCachedThreadPool();
    public static final int port = 8080;

    public static void main(String[] args) {
        ServerSocket serverSocket = null;
        try {
            serverSocket = new ServerSocket(port);
            System.out.println("BIO服务已启动，监听端口:" + port);

            // 通过死循环监听连接请求
            while (true) {
                // 等待客户端连接，阻塞方法
                Socket socket = serverSocket.accept();
                System.out.println("收到请求");
                // 将socket 交给线程池处理
                executorService.execute(new BioSocketHandler(socket));
            }
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            if (serverSocket != null) {
                try {
                    serverSocket.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
    }
}
