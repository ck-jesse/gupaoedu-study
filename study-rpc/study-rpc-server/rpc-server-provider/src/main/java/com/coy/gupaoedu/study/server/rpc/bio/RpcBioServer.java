package com.coy.gupaoedu.study.server.rpc.bio;

import com.coy.gupaoedu.study.server.rpc.RpcInvoker;
import com.coy.gupaoedu.study.server.rpc.RpcServer;
import lombok.extern.slf4j.Slf4j;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

/**
 * 基于socket实现rpc通信服务端
 *
 * @author chenck
 * @date 2019/6/6 16:57
 */
@Slf4j
public class RpcBioServer implements RpcServer {

    private final int port;
    private RpcInvoker rpcInvoker;

    private static ExecutorService executorService = Executors.newCachedThreadPool();

    public RpcBioServer(int port) {
        this.port = port;
    }

    public RpcBioServer(int port, RpcInvoker rpcInvoker) {
        this.port = port;
        this.rpcInvoker = rpcInvoker;
        this.start();
    }

    /**
     * 监听客户端请求
     */
    @Override
    public void start() {
        ServerSocket serverSocket = null;
        try {
            serverSocket = new ServerSocket(port);
            log.info("BIO RPC Server 已启动，监听的端口是：{}", this.port);
            while (true) {
                // accept不断接受请求
                Socket socket = serverSocket.accept();//BIO
                // 每一个socket 交给一个Handler来处理
                executorService.execute(new RpcBioRequestHandler(socket, rpcInvoker));
            }
        } catch (IOException e) {
            log.error("port=" + this.port, e);
        } finally {
            if (serverSocket != null) {
                try {
                    serverSocket.close();
                } catch (IOException e) {
                    log.error("", e);
                }
            }
        }
    }
}
