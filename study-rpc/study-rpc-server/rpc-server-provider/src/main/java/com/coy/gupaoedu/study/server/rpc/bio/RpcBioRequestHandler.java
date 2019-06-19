package com.coy.gupaoedu.study.server.rpc.bio;

import com.coy.gupaoedu.study.server.rpc.RpcInvoker;
import com.coy.gupaoedu.study.server.rpc.RpcRequest;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;

/**
 * 基于BIO的客户端请求处理器
 *
 * @author chenck
 * @date 2019/6/6 17:16
 */
public class RpcBioRequestHandler implements Runnable {

    private Socket socket;
    private RpcInvoker rpcInvoker;

    public RpcBioRequestHandler(Socket socket, RpcInvoker rpcInvoker) {
        this.socket = socket;
        this.rpcInvoker = rpcInvoker;
    }

    @Override
    public void run() {
        ObjectInputStream objectInputStream = null;
        ObjectOutputStream objectOutputStream = null;
        try {
            objectInputStream = new ObjectInputStream(socket.getInputStream());
            // 请求哪个类，方法名称、参数
            RpcRequest rpcRequest = (RpcRequest) objectInputStream.readObject();

            // 通过反射调用本地服务
            Object result = rpcInvoker.invoke(rpcRequest);

            objectOutputStream = new ObjectOutputStream(socket.getOutputStream());
            // 输出响应结果
            objectOutputStream.writeObject(result);
            objectOutputStream.flush();
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            if (objectInputStream != null) {
                try {
                    objectInputStream.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
            if (objectOutputStream != null) {
                try {
                    objectOutputStream.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
    }

}
