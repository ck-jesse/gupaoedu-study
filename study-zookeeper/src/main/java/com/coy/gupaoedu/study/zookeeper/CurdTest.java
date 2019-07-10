package com.coy.gupaoedu.study.zookeeper;

import org.apache.curator.framework.recipes.cache.NodeCacheListener;
import org.apache.zookeeper.data.Stat;
import org.junit.Before;
import org.junit.Test;

/**
 * 基于zookeeper 的CURD 增删改查操作
 *
 * @author chenck
 * @date 2019/7/10 21:59
 */
public class CurdTest {

    public static final String connectString = "127.0.0.1:2181";

    ZookeeperClient curatorFramework;

    /**
     * 初始化zookeeper连接 CuratorFramework
     */
    @Before
    public void init() {
        curatorFramework = new ZookeeperClient(connectString);
    }


    /**
     * 创建节点
     */
    @Test
    public void createNode() throws Exception {
        String result = curatorFramework.createNode("/curd/create2", "create");
        System.out.println(result);
    }

    /**
     * 获取节点数据
     */
    @Test
    public void getNodeData() throws Exception {
        String result = curatorFramework.getNodeData("/curd/create1");
        System.out.println(new String(result));
    }

    /**
     * 修改节点数据
     */
    @Test
    public void setNodeData() throws Exception {
        Stat result = curatorFramework.setNodeData("/curd/create1", "update_data");
        System.out.println(result);
    }

    /**
     * 删除节点
     */
    @Test
    public void deleteNode() throws Exception {
        boolean result = curatorFramework.deleteNode("/curd/create1");
        System.out.println(result);
    }

    /**
     * 判断节点是否存在
     */
    @Test
    public void isExistNode() throws Exception {
        boolean result = curatorFramework.isExistNode("/curd/create1");
        System.out.println(result);
    }

    /**
     * 注册节点数据变化事件
     * <p>
     * 将Zookeeper作为配置中心的时候，最常用的是监听节点数据的变动。
     */
    @Test
    public void registryWatcherNodeChanged() throws Exception {

        curatorFramework.registryWatcherNodeChanged("/curd/create1", new NodeCacheListener() {
            @Override
            public void nodeChanged() throws Exception {
                String newData = curatorFramework.getNodeData("/curd/create1");
                System.out.println("node data changed, new data = " + newData);
            }
        });

        while (true) {

        }
    }

}
