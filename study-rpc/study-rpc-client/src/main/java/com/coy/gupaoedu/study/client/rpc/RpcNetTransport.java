package com.coy.gupaoedu.study.client.rpc;

import com.coy.gupaoedu.study.server.rpc.RpcRequest;

/**
 * rpc网络传输
 *
 * @author chenck
 * @date 2019/6/19 16:08
 */
public abstract class RpcNetTransport {

    protected String host;
    protected int port;

    public RpcNetTransport(String host, int port) {
        this.host = host;
        this.port = port;
    }

    /**
     * 执行rpc调用
     */
    public abstract Object rpcInvoke(RpcRequest request);
}
