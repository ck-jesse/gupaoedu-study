package com.coy.gupaoedu.study.server.registry;

import com.coy.gupaoedu.study.server.rpc.RpcUrl;

/**
 * 服务注册中心
 *
 * @author chenck
 * @date 2019/7/10 22:02
 */
public interface RegistryCenter {

    public void registry(RpcUrl rpcUrl);
}
