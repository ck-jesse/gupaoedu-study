package com.coy.gupaoedu.study.client.rpc;

import com.coy.gupaoedu.study.server.rpc.RpcRequest;
import com.coy.gupaoedu.study.server.rpc.RpcUrl;

/**
 * rpc网络传输
 *
 * @author chenck
 * @date 2019/6/19 16:08
 */
public abstract class RpcNetTransport {

    /**
     * 执行rpc调用
     */
    public abstract Object rpcInvoke(RpcRequest request, RpcUrl rpcUrl);
}
