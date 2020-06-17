package com.coy.gupaoedu.study.server.rpc.nio;

import com.alibaba.fastjson.JSON;
import com.coy.gupaoedu.study.server.rpc.RpcInvoker;
import com.coy.gupaoedu.study.server.rpc.RpcRequest;
import com.coy.gupaoedu.study.server.serializer.Serializer;
import lombok.extern.slf4j.Slf4j;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.nio.ByteBuffer;
import java.util.concurrent.Callable;

/**
 * @author chenck
 * @date 2020/6/8 13:47
 */
@Slf4j
public class RpcNioRequestHandler implements Callable<ByteBuffer> {

    private Serializer serializer;
    private RpcInvoker rpcInvoker;
    private ByteBuffer buffer;

    public RpcNioRequestHandler(Serializer serializer, RpcInvoker rpcInvoker, ByteBuffer buffer) {
        this.serializer = serializer;
        this.rpcInvoker = rpcInvoker;
        this.buffer = buffer;
    }

    @Override
    public ByteBuffer call() throws Exception {
        ObjectInputStream objectInputStream = null;
        ObjectOutputStream objectOutputStream = null;
        try {
            // 反序列化
            RpcRequest rpcRequest = serializer.deserialize(buffer.array(), RpcRequest.class);
            System.out.println();
            log.info("请求参数：{}", JSON.toJSONString(rpcRequest));
            Object result = rpcInvoker.invoke(rpcRequest);
            log.info("响应参数：{}", JSON.toJSONString(result));

            // 序列化
            byte[] bytes = serializer.serialize(result, Object.class);
            return ByteBuffer.wrap(bytes);
        } catch (Exception e) {
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
