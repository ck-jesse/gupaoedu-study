package com.coy.gupaoedu.study.zookeeper;

import org.apache.curator.framework.CuratorFramework;
import org.apache.curator.framework.CuratorFrameworkFactory;
import org.apache.curator.framework.recipes.cache.NodeCache;
import org.apache.curator.framework.recipes.cache.NodeCacheListener;
import org.apache.curator.framework.recipes.cache.TreeCache;
import org.apache.curator.framework.recipes.cache.TreeCacheListener;
import org.apache.curator.framework.recipes.leader.LeaderSelector;
import org.apache.curator.framework.recipes.leader.LeaderSelectorListener;
import org.apache.curator.framework.recipes.leader.LeaderSelectorListenerAdapter;
import org.apache.curator.framework.recipes.locks.InterProcessMutex;
import org.apache.curator.retry.ExponentialBackoffRetry;
import org.apache.zookeeper.CreateMode;
import org.apache.zookeeper.data.Stat;

import java.util.concurrent.TimeUnit;

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
        byte[] result = curatorFramework.getData().forPath(path);
        return new String(result);
    }

    /**
     * 修改节点数据
     */
    public Stat setNodeData(String path, String data) {
        Stat result = null;
        try {
            result = curatorFramework.setData().forPath(path, data.getBytes());
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
            result = curatorFramework.checkExists().forPath(path);
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
     * 注册节点数据变化事件(监听单个节点变化)
     * <p>
     * 将Zookeeper作为配置中心的时候，最常用的是监听节点数据的变动。
     */
    public boolean registryWatcherNodeChanged(String path, NodeCacheListener nodeCacheListener) {

        NodeCache nodeCache = new NodeCache(curatorFramework, path, false);

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

    /**
     * 注册path路径下所有子节点的数据变化事件(递归监听子节点的子节点)
     */
    public boolean registryWatcherTreeNodeChanged(String path, TreeCacheListener treeCacheListener) {

        TreeCache treeCache = new TreeCache(curatorFramework, path);
        try {
            // curator实现了一直监听，zookeeper本身只能监听一次
            treeCache.getListenable().addListener(treeCacheListener);

            treeCache.start();
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
        return true;
    }

    /**
     * 分布式锁
     * <p>
     * 原理：
     * 1、创建一个临时有序节点（此时未持有锁）
     * 方法：org.apache.curator.framework.recipes.locks.LockInternalsDriver#createsTheLock
     * 2、自旋（死循环）判断当前节点是否能够持有锁
     * 方法：org.apache.curator.framework.recipes.locks.LockInternals#internalLockLoop
     * 3、获取到所有子节点列表，并且从小到大根据节点名称后10位数字进行排序；
     * 若能持有锁，则返回true；
     * 若不能持有锁，则获取到上一个临时节点的path，并添加一个watcher监听（也就是当前线程会监听自己节点的上一个节点的变化，而不是监听父节点下所有节点的变动）；
     * 然后当前线程进入wait，等待被唤醒；
     * 当监听的节点发生了变动时，那么就将线程从等待状态唤醒，重新开始下一轮的锁的争抢。
     * <p>
     * 总结：
     * 分布式锁的本质就是通过创建一个临时有序节点，和watcher监听上一个节点的变动来实现的。
     * <p>
     * 利用临时有序节点+watcher监听机制可以做很多事情，如分布式锁、Leader选举、分布式队列（FIFO）
     */
    public void distributedLock(String path, long waitTime, TimeUnit unit) {
        // Curator的几种锁方案
        // InterProcessMutex：分布式可重入排它锁
        // InterProcessSemaphoreMutex：分布式排它锁
        // InterProcessReadWriteLock：分布式读写锁
        // InterProcessMultiLock：将多个锁作为单个实体管理的容器

        InterProcessMutex lock = new InterProcessMutex(curatorFramework, path);
        String threadName = Thread.currentThread().getName();
        try {
            System.out.println(threadName + " 开始获取锁");
            // 获取锁
            if (lock.acquire(waitTime, unit)) {
                System.out.println(threadName + " 获取锁成功");
                System.out.println(threadName + " 开始sleep 5s");
                Thread.sleep(5000);
            } else {
                System.out.println(threadName + " 获取锁失败");
            }
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            try {
                if (lock.isOwnedByCurrentThread()) {
                    lock.release();
                    System.out.println(threadName + " 释放锁");
                } else {
                    System.out.println(threadName + " 无需释放锁，因为没有获得锁");
                }
            } catch (Exception e) {
                System.out.println(threadName + " 释放锁异常 " + e.getMessage());
                e.printStackTrace();
            }
        }
    }

    /**
     * leader选举
     * <p>
     * 原理：
     * 1、创建一个线程池，然后将在执行 LeaderSelector.start()方法时,往线程池中提交一个workloop任务
     * 2、调用方法internalRequeue，实质为执行任务对应方法 LeaderSelector.doWork()，其中使用了分布式锁InterProcessMutex.acquire()来无限等待获得锁
     * 3、若当前节点获得锁，则执行 LeaderSelectorListener.takeLeadership()方法，也就是自定义的Listener中的方法
     * 当该方法持有执行权限时，则表示当前节点被选举为leader了，退出该方法时，则表示放弃执行权限，放弃领导权
     * 4、若当前节点未获得锁，则无限等待获得锁（实质就是利用了分布式锁的特性来控制）
     * 5、当放弃领导权时，并且LeaderSelector.autoRequeue()为true，则再次调用internalRequeue来获取锁（获取leader）
     * <p>
     * 总结：
     * leader选举的本质就是利用的分布式锁的特性来实现的主从选举。watcher监听上一个临时有序节点，然后无限等待，直到获得锁，获得锁则表示被选举为了leader。
     */
    public void leaderElection(String path, String clientName, long sleepTime) {
        LeaderSelectorListener listener = new LeaderSelectorListenerAdapter() {
            public void takeLeadership(CuratorFramework client) throws Exception {
                // this callback will get called when you are the leader
                // do whatever leader work you need to and only exit
                // this method when you want to relinquish leadership
                // 当你是领导者，做你需要做的任何领导工作时，这个回调都会被调用，只有当你想放弃领导时才退出这个方法。
                System.out.println("[" + clientName + "] 我是Leader啦，听我号令，开干咯！我要一直保持不return该方法，一但return那么我就不是leader啦");
                try {
                    System.out.println("[" + clientName + "] 我将休眠10s，这段时间我是leader");
                    Thread.sleep(sleepTime);
                } finally {
                    System.out.println("[" + clientName + "] relinquishing leadership 放弃领导权");
                }
            }
        };

        // create a leader selector
        LeaderSelector selector = new LeaderSelector(curatorFramework, path, listener);
        // 自动重复参与选举(用于控制获得锁，并释放锁以后，再次进入等待获得锁，也就是重复参与锁的获取，实质为重复获取leader)
        selector.autoRequeue();  // not required, but this is behavior that you will probably expect
        selector.start();
    }
}
