package com.coy.gupaoedu.study.zookeeper;

import org.junit.Test;

import java.util.concurrent.TimeUnit;

/**
 * 基于zookeeper 实现分布式锁 测试
 *
 * @author chenck
 * @date 2019/7/10 22:21
 */
public class DistributedLockTest {

    public static final String connectString = "127.0.0.1:2181";

    private ZookeeperClient curatorFramework = new ZookeeperClient(connectString);


    @Test
    public void test() {
        final String lockPath = "/lock";
        final long waitTime = 20;

        new Thread(new Runnable() {
            @Override
            public void run() {
                System.out.println("thread1-start");
                curatorFramework.distributedLock(lockPath, waitTime, TimeUnit.SECONDS);
                System.out.println("thread1-end");
            }
        }, "thread1").start();

        new Thread(new Runnable() {
            @Override
            public void run() {
                System.out.println("thread2-start");
                curatorFramework.distributedLock(lockPath, waitTime, TimeUnit.SECONDS);
                System.out.println("thread2-end");
            }
        }, "thread2").start();

        new Thread(new Runnable() {
            @Override
            public void run() {
                System.out.println("thread3-start");
                curatorFramework.distributedLock(lockPath, waitTime, TimeUnit.SECONDS);
                System.out.println("thread3-end");
            }
        }, "thread3").start();

        System.out.println("main start");
        while (true) {
        }
    }


}
