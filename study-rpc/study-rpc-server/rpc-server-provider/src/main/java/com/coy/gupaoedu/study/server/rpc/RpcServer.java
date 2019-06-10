package com.coy.gupaoedu.study.server.rpc;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.Map;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

/**
 * 基于socket实现rpc通信服务端
 *
 * @author chenck
 * @date 2019/6/6 16:57
 */
public class RpcServer {

    private final int port;

    private static ExecutorService executorService = Executors.newCachedThreadPool();

    public RpcServer(int port) {
        this.port = port;
    }

    /**
     * 监听客户端请求
     */
    public void reciveRequest(Map<String, Object> handlerMap) {
        ServerSocket serverSocket = null;
        try {
            serverSocket = new ServerSocket(port);
            while (true) {
                // accept不断接受请求
                Socket socket = serverSocket.accept();//BIO
                // 每一个socket 交给一个Handler来处理
                executorService.execute(new RpcClientRequestHandler(socket, handlerMap));
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
