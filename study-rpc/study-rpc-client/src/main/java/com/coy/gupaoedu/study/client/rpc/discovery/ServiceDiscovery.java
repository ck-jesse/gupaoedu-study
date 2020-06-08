package com.coy.gupaoedu.study.client.rpc.discovery;

import com.coy.gupaoedu.study.server.rpc.RpcUrl;

/**
 * 服务发现
 *
 * TODO
 * 服务提供者：只注册服务ip+port
 * 服务消费者：只发现服务ip+port
 * 减少注册中心与提供者和消费者之间的通信压力。至于接口协议，可以由提供方提供一个jar给消费者，消费者通过动态代理包装接口来调用也是一样的。
 * 动态代理：对目标接口包装 负载均衡、服务调用、序列化反序列化
 *
 *
 * @author chenck
 * @date 2019/7/10 22:16
 */
public interface ServiceDiscovery {

    /**
     * 服务查找
     * 通过监听来实现
     */
    public RpcUrl discovery(String serviceName);

    public RpcUrl discovery(String serviceName, String version);
}
