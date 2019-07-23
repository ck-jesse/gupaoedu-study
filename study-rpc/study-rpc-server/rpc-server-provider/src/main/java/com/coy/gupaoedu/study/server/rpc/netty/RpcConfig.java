package com.coy.gupaoedu.study.server.rpc.netty;

import lombok.Data;

/**
 * @author chenck
 * @date 2019/7/23 18:04
 */
@Data
public class RpcConfig {

    public RpcConfig(int port, String connectString) {
        this.serverPort = port;
        this.zkConnectString = connectString;
    }

    /**
     * 服务端port
     */
    private int serverPort;
    /**
     * zk 连接字符串
     */
    private String zkConnectString;
}
