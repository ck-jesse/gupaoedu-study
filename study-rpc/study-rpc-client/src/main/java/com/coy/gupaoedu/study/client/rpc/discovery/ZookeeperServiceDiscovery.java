package com.coy.gupaoedu.study.client.rpc.discovery;

import org.apache.curator.framework.CuratorFramework;
import org.apache.curator.framework.CuratorFrameworkFactory;
import org.apache.curator.framework.recipes.cache.NodeCache;
import org.apache.curator.framework.recipes.cache.NodeCacheListener;
import org.apache.curator.retry.ExponentialBackoffRetry;

/**
 * @author chenck
 * @date 2019/7/10 22:22
 */
public class ZookeeperServiceDiscovery implements ServiceDiscovery {

    private CuratorFramework curatorFramework;

    /**
     * 构造函数中初始化zookeeper连接 CuratorFramework
     */
    public ZookeeperServiceDiscovery(String connectString) {
        curatorFramework = CuratorFrameworkFactory.builder()
                .connectString(connectString)
                .sessionTimeoutMs(5000)// 超时时间
                .retryPolicy(new ExponentialBackoffRetry(1000, 3))// 设置重试策略
                .build();
        // 启动
        curatorFramework.start();
    }


    @Override
    public String discovery(String serviceName) {
        String path = "/rpc";
        NodeCache nodeCache = new NodeCache(curatorFramework, path, false);
        try {
            // curator实现了一直监听，zookeeper本身只能监听一次
            nodeCache.getListenable().addListener(new NodeCacheListener() {
                @Override
                public void nodeChanged() throws Exception {
                    String newData = new String(curatorFramework.getData().forPath(path));
                    System.out.println("node data changed, new data = " + newData);
                }
            });

            nodeCache.start(true);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }
}
