package com.coy.gupaoedu.study.zookeeper;

import org.apache.curator.framework.CuratorFramework;
import org.apache.curator.framework.CuratorFrameworkFactory;
import org.apache.curator.framework.recipes.cache.NodeCache;
import org.apache.curator.framework.recipes.cache.NodeCacheListener;
import org.apache.curator.retry.ExponentialBackoffRetry;
import org.apache.zookeeper.CreateMode;
import org.apache.zookeeper.data.Stat;

/**
 * @author chenck
 * @date 2019/7/10 23:35
 */
public class ZookeeperClient {

    private CuratorFramework curatorFramework;

    /**
     * 构造函数中初始化zookeeper连接 CuratorFramework
     */
    public ZookeeperClient(String connectString) {
        curatorFramework = CuratorFrameworkFactory.builder()
                .connectString(connectString)
                .sessionTimeoutMs(5000)// 超时时间
                .retryPolicy(new ExponentialBackoffRetry(1000, 3))// 设置重试策略
                .build();
        // 启动
        curatorFramework.start();
    }


    /**
     * 创建节点
     */
    public String createNode(String path, String data) throws Exception {
        String result = curatorFramework.create()
                .creatingParentsIfNeeded()// 如果需要则创建父级node
                .withMode(CreateMode.PERSISTENT)// 设置节点模式
                .forPath(path, data.getBytes());// 指定节点路径和值
        return result;
    }

    /**
     * 获取节点数据
     */
    public String getNodeData(String path) throws Exception {
        byte[] result = curatorFramework.getData()
                .forPath(path);
        return new String(result);
    }

    /**
     * 修改节点数据
     */
    public Stat setNodeData(String path, String data) {
        Stat result = null;
        try {
            result = curatorFramework.setData()
                    .forPath(path, data.getBytes());
        } catch (Exception e) {
            e.printStackTrace();
        }
        return result;
    }

    /**
     * 删除节点
     */
    public boolean deleteNode(String path) {
        try {
            curatorFramework.delete()
                    .deletingChildrenIfNeeded()// 如果需要则删除子节点
                    .forPath(path);
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
        return true;
    }

    /**
     * 判断节点是否存在
     */
    public boolean isExistNode(String path) {
        Stat result = null;
        try {
            result = curatorFramework.checkExists()
                    .forPath(path);
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
        System.out.println("节点是否存在：" + (null != result));
        if (null != result) {
            if (result.getEphemeralOwner() > 0) {
                System.out.println("持久节点");
            } else {
                System.out.println("临时节点");
            }
        }
        return null != result;
    }

    /**
     * 注册节点数据变化事件
     * <p>
     * 将Zookeeper作为配置中心的时候，最常用的是监听节点数据的变动。
     */
    public boolean registryWatcherNodeChanged(String path, NodeCacheListener nodeCacheListener) {

        NodeCache nodeCache = new NodeCache(curatorFramework, "/curd/create1", false);

        try {
            // curator实现了一直监听，zookeeper本身只能监听一次
            nodeCache.getListenable().addListener(nodeCacheListener);

            nodeCache.start(true);
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
        return true;
    }
}
