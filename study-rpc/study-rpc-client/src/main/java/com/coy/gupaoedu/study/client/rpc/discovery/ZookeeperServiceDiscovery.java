package com.coy.gupaoedu.study.client.rpc.discovery;

import com.alibaba.fastjson.JSON;
import com.coy.gupaoedu.study.server.rpc.RpcUrl;
import org.apache.curator.framework.CuratorFramework;
import org.apache.curator.framework.CuratorFrameworkFactory;
import org.apache.curator.framework.recipes.cache.ChildData;
import org.apache.curator.framework.recipes.cache.TreeCache;
import org.apache.curator.framework.recipes.cache.TreeCacheEvent;
import org.apache.curator.framework.recipes.cache.TreeCacheListener;
import org.apache.curator.retry.ExponentialBackoffRetry;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

/**
 * @author chenck
 * @date 2019/7/10 22:22
 */
public class ZookeeperServiceDiscovery implements ServiceDiscovery {

    private CuratorFramework curatorFramework;

    /**
     * Map<service, Map<host:port(registry), List<provider>>>
     */
    //private final Map<String, ConcurrentMap<String, List<RpcUrl>>> serviceRegistryProviders = new ConcurrentHashMap<>();
    /**
     * Map<service, List<provider>>
     */
    private final Map<String, List<RpcUrl>> serviceProviders = new ConcurrentHashMap<>();

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

        startDiscoveryService();
    }

    protected void startDiscoveryService() {
        String path = "/rpc";
        // TreeCache 监视路径下所有的创建、更新、删除事件
        TreeCache treeCache = new TreeCache(curatorFramework, path);
        try {
            // curator实现了一直监听，zookeeper本身只能监听一次
            treeCache.getListenable().addListener(new TreeCacheListener() {
                @Override
                public void childEvent(CuratorFramework client, TreeCacheEvent event) throws Exception {
                    ChildData childData = event.getData();
                    if (TreeCacheEvent.Type.NODE_ADDED == event.getType()) {
                        System.out.println(event.getType());
                        // 反序列化service url node
                        RpcUrl rpcUrl = RpcUrl.deserialize(childData.getPath());
                        if (null != rpcUrl) {
                            System.out.println(JSON.toJSONString(rpcUrl));
                            List<RpcUrl> serviceList = serviceProviders.get(rpcUrl.getServiceName());
                            if (null == serviceList) {
                                serviceList = new ArrayList<>();
                                serviceProviders.put(rpcUrl.getServiceName(), serviceList);
                            }
                            serviceList.add(rpcUrl);
                        }
                    }
                    if (TreeCacheEvent.Type.NODE_REMOVED == event.getType()) {
                        System.out.println(event.getType());
                        // 反序列化service url node
                        RpcUrl rpcUrl = RpcUrl.deserialize(childData.getPath());
                        List<RpcUrl> serviceList = serviceProviders.get(rpcUrl.getServiceName());

                        RpcUrl rpcUrl1 = getService(rpcUrl, serviceList);
                        if (null != rpcUrl1) {
                            serviceList.remove(rpcUrl1);
                        }
                    }
                    System.out.println();
                }
            });

            treeCache.start();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private RpcUrl getService(RpcUrl rpcUrl, List<RpcUrl> serviceList) {
        if (null == serviceList) {
            return null;
        }
        for (RpcUrl rpcUrl1 : serviceList) {
            if (rpcUrl1.serialize().equals(rpcUrl.serialize())) {
                return rpcUrl1;
            }
        }
        return null;
    }


    @Override
    public RpcUrl discovery(String serviceName) {
        return discovery(serviceName, null);
    }

    @Override
    public RpcUrl discovery(String serviceName, String version) {
        if (null != version && !"".equals(version)) {
            serviceName = serviceName + "-" + version;
        }
        List<RpcUrl> serviceList = serviceProviders.get(serviceName);
        if (null == serviceList) {
            return null;
        }
        if (serviceList.size() == 1) {
            return serviceList.get(0);
        }

        // 随机负载
        RandonLoadBalance loadBalance = new RandonLoadBalance();
        RpcUrl rpcUrl = loadBalance.select(serviceList);
        return rpcUrl;
    }
}
