package com.coy.gupaoedu.study.zookeeper;

import org.apache.curator.framework.CuratorFramework;
import org.apache.curator.framework.recipes.cache.ChildData;
import org.apache.curator.framework.recipes.cache.NodeCacheListener;
import org.apache.curator.framework.recipes.cache.TreeCacheEvent;
import org.apache.curator.framework.recipes.cache.TreeCacheListener;
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

    @Test
    public void registryWatcherTreeNodeChanged() throws Exception {

        String path = "/watcher";

        curatorFramework.registryWatcherTreeNodeChanged(path, new TreeCacheListener() {
            @Override
            public void childEvent(CuratorFramework client, TreeCacheEvent event) throws Exception {
                ChildData childData = event.getData();
                System.out.println(event.getType());
                if (TreeCacheEvent.Type.NODE_ADDED == event.getType()) {
                    System.out.println("[TreeNode]add node, path=" + childData.getPath() + ", data=" + new String(childData.getData()));
                } else if (TreeCacheEvent.Type.NODE_UPDATED == event.getType()) {
                    System.out.println("[TreeNode]updated node, path=" + childData.getPath() + ", data=" + new String(childData.getData()));
                } else if (TreeCacheEvent.Type.NODE_REMOVED == event.getType()) {
                    System.out.println("[TreeNode]removed node, path=" + childData.getPath() + ", data=" + new String(childData.getData()));
                }
            }
        });

        while (true) {

        }
    }

    @Test
    public void create() throws Exception {
        curatorFramework.createNode("/watcher/w3", "watcher");
    }
}
