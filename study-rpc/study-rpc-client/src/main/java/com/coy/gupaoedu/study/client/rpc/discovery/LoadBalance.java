package com.coy.gupaoedu.study.client.rpc.discovery;

import com.coy.gupaoedu.study.server.rpc.RpcUrl;

import java.util.List;

/**
 * 负载均衡策略
 *
 * @author chenck
 * @date 2019/7/10 22:41
 */
public interface LoadBalance {

    /**
     * 选择服务
     */
    public RpcUrl selectService(List<RpcUrl> serviceList);
}
