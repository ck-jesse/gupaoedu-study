package com.coy.gupaoedu.study.client.rpc;

import com.alibaba.fastjson.JSON;
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
        // 如果传进来是一个已实现的具体类（本次演示略过此逻辑)
        if (Object.class.equals(method.getDeclaringClass())) {
            try {
                return method.invoke(this, args);
            } catch (Throwable t) {
                t.printStackTrace();
                return null;
            }
        }
        // 如果是接口，则认为是rpc
        // 请求数据的包装
        RpcRequest rpcRequest = new RpcRequest();
        rpcRequest.setClassName(method.getDeclaringClass().getName());
        rpcRequest.setMethodName(method.getName());
        rpcRequest.setParameters(args);
        rpcRequest.setVersion(version);
        System.out.println();
        System.out.println("[client]请求参数: " + JSON.toJSONString(rpcRequest));
        // 执行rpc调用
        Object result = rpcNetTransport.rpcInvoke(rpcRequest);
        System.out.println("[client]响应参数: " + JSON.toJSONString(result));
        return result;
    }
}
