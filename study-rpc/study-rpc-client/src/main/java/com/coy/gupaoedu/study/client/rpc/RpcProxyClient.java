package com.coy.gupaoedu.study.client.rpc;

import java.lang.reflect.Proxy;

/**
 * 客户端的代理
 *
 * @author chenck
 * @date 2019/6/6 16:57
 */
public class RpcProxyClient {

    public <T> T clientProxy(final Class<T> interfaces, final String host, final int port) {
        return clientProxy(interfaces, host, port, host);
    }

    public <T> T clientProxy(final Class<T> interfaces, final String host, final int port, String version) {
        return (T) Proxy.newProxyInstance(interfaces.getClassLoader(), new Class[]{interfaces},
                new RemoteInvocationHandler(host, port, version));
    }
}
