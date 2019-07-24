package com.coy.gupaoedu.study.client.rpc;

import com.coy.gupaoedu.study.client.rpc.discovery.ServiceDiscovery;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;
import java.lang.reflect.Proxy;

/**
 * 客户端的代理
 *
 * @author chenck
 * @date 2019/6/6 16:57
 */
@Component
public class RpcProxyClient {

    @Resource
    RpcNetTransport rpcNetTransport;
    @Resource
    ServiceDiscovery serviceDiscovery;

    public <T> T clientProxy(final Class<T> interfaces) {
        return clientProxy(interfaces, null);
    }

    public <T> T clientProxy(final Class<T> interfaces, String version) {
        return (T) Proxy.newProxyInstance(interfaces.getClassLoader(), new Class[]{interfaces},
                new RemoteInvocationHandler(version, serviceDiscovery, rpcNetTransport));
    }
}
