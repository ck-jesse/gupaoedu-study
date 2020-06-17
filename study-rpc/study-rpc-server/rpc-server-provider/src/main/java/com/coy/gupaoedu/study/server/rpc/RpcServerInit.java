package com.coy.gupaoedu.study.server.rpc;

import com.coy.gupaoedu.study.server.rpc.bio.RpcBioServer;
import com.coy.gupaoedu.study.server.rpc.netty.RpcNettyServer;
import com.coy.gupaoedu.study.server.rpc.nio.RpcNioServer;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

/**
 * 初始化启动服务监听
 *
 * @author chenck
 * @date 2020/6/17 16:09
 */
@Component
public class RpcServerInit implements InitializingBean {

    @Autowired
    RpcInvoker rpcInvoker;

    @Autowired
    RpcConfig rpcConfig;

    @Override
    public void afterPropertiesSet() throws Exception {
        RpcServer rpcServer = null;
        if (RpcConsts.BIO.equalsIgnoreCase(rpcConfig.getRpcServerType())) {
            rpcServer = new RpcBioServer(rpcConfig.getServerPort(), rpcInvoker);
        } else if (RpcConsts.NIO.equalsIgnoreCase(rpcConfig.getRpcServerType())) {
            rpcServer = new RpcNioServer(rpcConfig.getServerPort(), rpcInvoker);
        } else if (RpcConsts.NETTY.equalsIgnoreCase(rpcConfig.getRpcServerType())) {
            rpcServer = new RpcNettyServer(rpcConfig.getServerPort(), rpcInvoker);
        } else {
            throw new IllegalArgumentException("暂不支持该RPC通信类型" + rpcConfig.getRpcServerType());
        }
        rpcServer.start();
    }
}
