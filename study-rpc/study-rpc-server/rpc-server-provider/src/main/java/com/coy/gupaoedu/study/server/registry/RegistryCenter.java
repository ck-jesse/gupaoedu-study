package com.coy.gupaoedu.study.server.registry;

/**
 * 服务注册中心
 *
 * @author chenck
 * @date 2019/7/10 22:02
 */
public interface RegistryCenter {

    public void registry(String serviceName, String serviceAddress);
}
