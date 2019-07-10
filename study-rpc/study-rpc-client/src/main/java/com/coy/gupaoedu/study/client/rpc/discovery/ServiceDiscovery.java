package com.coy.gupaoedu.study.client.rpc.discovery;

/**
 * 服务发现
 *
 * @author chenck
 * @date 2019/7/10 22:16
 */
public interface ServiceDiscovery {

    /**
     * 服务查找
     * 通过监听来实现
     */
    public String discovery(String serviceName);
}
