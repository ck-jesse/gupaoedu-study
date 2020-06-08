package com.coy.gupaoedu.study.client.rpc.transport.bio;

import com.coy.gupaoedu.study.client.rpc.transport.RpcNetTransport;
import com.coy.gupaoedu.study.server.rpc.RpcRequest;
import com.coy.gupaoedu.study.server.rpc.RpcUrl;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;

/**
 * 基于BIO的rpc网络传输
 *
 * @author chenck
 * @date 2019/6/9 16:08
 */
public class RpcBioNetTransport extends RpcNetTransport {

    /**
     * 执行rpc调用
     */
    @Override
    public Object rpcInvoke(RpcRequest request, RpcUrl rpcUrl) {
        Socket socket = null;
        Object result = null;
        ObjectOutputStream outputStream = null;
        ObjectInputStream inputStream = null;
        try {
            // 建立连接
            socket = new Socket(rpcUrl.getHost(), rpcUrl.getPort());

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
