package com.coy.gupaoedu.study.client.rpc;

import com.coy.gupaoedu.study.server.rpc.RpcRequest;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;

/**
 * @author chenck
 * @date 2019/6/9 16:08
 */
public class RpcNetTransport {

    private String host;
    private int port;

    public RpcNetTransport(String host, int port) {
        this.host = host;
        this.port = port;
    }

    /**
     * 发送请求到server
     */
    public Object send(RpcRequest request) {
        Socket socket = null;
        Object result = null;
        ObjectOutputStream outputStream = null;
        ObjectInputStream inputStream = null;
        try {
            // 建立连接
            socket = new Socket(host, port);

            // 网络socket io
            outputStream = new ObjectOutputStream(socket.getOutputStream());
            // 序列化
            outputStream.writeObject(request);
            outputStream.flush();

            inputStream = new ObjectInputStream(socket.getInputStream());
            // 反序列化
            result = inputStream.readObject();
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            if (inputStream != null) {
                try {
                    inputStream.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
            if (outputStream != null) {
                try {
                    outputStream.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
        return result;
    }
}
