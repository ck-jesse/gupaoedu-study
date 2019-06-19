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

    private String version;
    private RpcNetTransport rpcNetTransport;


    public RemoteInvocationHandler(String version, RpcNetTransport rpcNetTransport) {
        this.version = version;
        this.rpcNetTransport = rpcNetTransport;
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
        // 执行rpc调用
        Object result = rpcNetTransport.rpcInvoke(rpcRequest);
        return result;
    }
}
