package com.coy.gupaoedu.study.server.registry;

import com.coy.gupaoedu.study.server.rpc.RpcUrl;
import com.coy.gupaoedu.study.server.rpc.netty.RpcConfig;
import lombok.extern.slf4j.Slf4j;
import org.apache.curator.framework.CuratorFramework;
import org.apache.curator.framework.CuratorFrameworkFactory;
import org.apache.curator.retry.ExponentialBackoffRetry;
import org.apache.zookeeper.CreateMode;

import java.net.URLDecoder;
import java.net.URLEncoder;

/**
 * @author chenck
 * @date 2019/7/10 22:22
 */
@Slf4j
public class ZookeeperRegistryCenter implements RegistryCenter {

    private CuratorFramework curatorFramework;

    private RpcConfig rpcConfig;

    /**
     * 构造函数中初始化zookeeper连接 CuratorFramework
     */
    public ZookeeperRegistryCenter(RpcConfig rpcConfig) {
        curatorFramework = CuratorFrameworkFactory.builder()
                .connectString(rpcConfig.getZkConnectString())
                .sessionTimeoutMs(5000)// 超时时间
                .retryPolicy(new ExponentialBackoffRetry(1000, 3))// 设置重试策略
                .build();
        // 启动
        curatorFramework.start();
        this.rpcConfig = rpcConfig;
    }

    @Override
    public void registry(RpcUrl rpcUrl) {
        try {
            if (rpcUrl.getPort() <= 0) {
                rpcUrl.setPort(rpcConfig.getServerPort());
            }
            // 将服务信息作为path的最后一个节点
            String path = getPath(rpcUrl) + URLEncoder.encode(rpcUrl.serialize(), "UTF-8");
            log.info("[server]registry service {}", URLDecoder.decode(path, "UTF-8"));
            curatorFramework.create().creatingParentContainersIfNeeded().withMode(CreateMode.EPHEMERAL).forPath(path, rpcUrl.getHost().getBytes());
        } catch (Exception e) {
            log.error("", e);
        }
    }

    private String getPath(RpcUrl rpcUrl) {
        String serviceName = rpcUrl.getInterfaceName();
        if (serviceName.startsWith("/")) {
            serviceName = serviceName.substring(1);
        }
        if (serviceName.endsWith("/")) {
            serviceName = serviceName.substring(0, serviceName.length() - 1);
        }
        String path = "/rpc/" + serviceName + "/" + rpcUrl.getSide() + "/";
        return path;
    }
}
