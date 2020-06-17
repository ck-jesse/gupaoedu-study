package com.coy.gupaoedu.study.server.rpc;

import lombok.Data;

/**
 * @author chenck
 * @date 2019/7/23 18:04
 */
@Data
public class RpcConfig {

    public RpcConfig(int port, String connectString) {
        this(port, connectString, RpcConsts.NIO);
    }

    public RpcConfig(int port, String connectString, String rpcServerType) {
        this.serverPort = port;
        this.zkConnectString = connectString;
        this.rpcServerType = rpcServerType;
    }

    /**
     * 服务端port
     */
    private int serverPort;
    /**
     * zk 连接字符串
     */
    private String zkConnectString;

    /**
     * rpc服务类型 bio,nio,netty
     */
    private String rpcServerType;
}
