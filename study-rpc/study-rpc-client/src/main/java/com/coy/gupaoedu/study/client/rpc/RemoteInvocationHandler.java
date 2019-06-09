package com.coy.gupaoedu.study.client.rpc;

import com.coy.gupaoedu.study.server.rpc.RpcRequest;

import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Method;

/**
 * 代理对象处理器
 *
 * @author chenck
 * @date 2019/6/9 16:06
 */
public class RemoteInvocationHandler implements InvocationHandler {

    private String host;
    private int port;
    private String version;

    public RemoteInvocationHandler(String host, int port) {
        this(host, port, null);
    }

    public RemoteInvocationHandler(String host, int port, String version) {
        this.host = host;
        this.port = port;
        this.version = version;
    }

    @Override
    public Object invoke(Object proxy, Method method, Object[] args) throws Throwable {
        // 请求会进入到这里
        System.out.println("[client]prepare to request server");
        // 请求数据的包装
        RpcRequest rpcRequest = new RpcRequest();
        rpcRequest.setClassName(method.getDeclaringClass().getName());
        rpcRequest.setMethodName(method.getName());
        rpcRequest.setParameters(args);
        rpcRequest.setVersion(version);
        // 远程通信
        RpcNetTransport netTransport = new RpcNetTransport(host, port);
        Object result = netTransport.send(rpcRequest);
        return result;
    }
}
