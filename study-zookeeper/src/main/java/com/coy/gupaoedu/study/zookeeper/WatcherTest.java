package com.coy.gupaoedu.study.zookeeper;

import org.apache.curator.framework.recipes.cache.NodeCacheListener;
import org.junit.Test;

/**
 * 基于zookeeper 的watcher机制 测试
 *
 * @author chenck
 * @date 2019/7/10 22:10
 */
public class WatcherTest {

    public static final String connectString = "127.0.0.1:2181";

    ZookeeperClient curatorFramework = new ZookeeperClient(connectString);

    /**
     * 注册节点数据变化事件
     * <p>
     * 将Zookeeper作为配置中心的时候，最常用的是监听节点数据的变动。
     */
    @Test
    public void registryWatcherNodeChanged() throws Exception {

        String path = "/watcher";

        curatorFramework.registryWatcherNodeChanged(path, new NodeCacheListener() {
            @Override
            public void nodeChanged() throws Exception {
                String newData = curatorFramework.getNodeData(path);
                System.out.println("node data changed, new data = " + newData);
            }
        });

        while (true) {

        }
    }
}
