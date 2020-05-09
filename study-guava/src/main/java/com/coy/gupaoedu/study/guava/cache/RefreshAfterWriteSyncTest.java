package com.coy.gupaoedu.study.guava.cache;

import com.google.common.cache.CacheBuilder;
import com.google.common.cache.CacheLoader;
import com.google.common.cache.LoadingCache;
import org.junit.Test;

import java.util.UUID;
import java.util.concurrent.TimeUnit;

/**
 * refreshAfterWrite 同步刷新缓存：只阻塞加载数据的线程，其余线程返回旧数据。
 * 注：缓存项只有在被检索时才会真正刷新
 *
 * @author chenck
 * @date 2019/10/16 18:10
 */
public class RefreshAfterWriteSyncTest {

    public static final String KEY = "name";

    // 1s后刷新缓存
    // 只有真正获取缓存的线程才会进入CacheLoader中
    private static LoadingCache<String, String> cache = CacheBuilder.newBuilder()
            .refreshAfterWrite(1, TimeUnit.SECONDS)
            .recordStats() // 开启统计信息
            .build(new CacheLoader<String, String>() {

                @Override
                public String load(String key) throws Exception {
                    System.out.println(Thread.currentThread().getName() + " 执行 CacheLoader.load 同步刷新" + key);
                    System.out.println(Thread.currentThread().getName() + " begin to mock query db...");
                    Thread.sleep(1000);
                    System.out.println(Thread.currentThread().getName() + " success to mock query db...");
                    return UUID.randomUUID().toString();
                }

            });

    /**
     * 测试cache中有数据的情况 refreshAfterWrite
     * 结果：只阻塞更新数据的线程，其余线程返回旧数据。
     * <p>
     * 问题：真正更新数据的那个线程一定会阻塞，我们希望这个更新过程是异步的。
     * 场景：当缓存的key很多时，高并发条件下大量线程同时获取不同key对应的缓存，此时依然会造成大量线程阻塞，并且给数据库带来很大压力。
     * 分析：refreshAfterWrite默认的刷新是同步的，会在调用者的线程中执行。解决办法就是将刷新缓存值的任务交给后台线程。
     * 处理：通过实现CacheLoader.reload()来异步刷新。
     * 结论：所有的用户请求线程均返回旧的缓存值，这样就不会有用户线程被阻塞了。
     */
    @Test
    public void refreshAfterWriteTest() throws Exception {

        // 手动添加一条缓存数据,睡眠1.5s让其过期
        cache.put(KEY, "coy");
        Thread.sleep(1500);

        for (int i = 0; i < 10; i++) {
            GetThread.startThread(i, KEY, cache);
        }
        // 让多个线程同时运行
        GetThread.latch.countDown();

        // 模拟取最新的最新的值
        Thread.sleep(1500);
        System.out.println(Thread.currentThread().getName() + " value = " + cache.get(KEY));
        Thread.sleep(2000);
        GetThread.startThread(20, KEY, cache);
        // hold住主线程
        while (true) {
        }
    }

    /**
     * 测试cache中无数据的情况
     * 结果：由于缓存没有数据，导致一个线程去加载数据的时候，别的线程都阻塞了(因为没有旧值可以返回)
     * 所以一般系统启动的时候，我们需要将数据预先加载到缓存，不然就会出现这种情况。
     */
    @Test
    public void notDataOfCache() throws Exception {

        for (int i = 0; i < 8; i++) {
            GetThread.startThread(i, KEY, cache);
        }
        // 让多个线程同时运行
        GetThread.latch.countDown();

        // hold住主线程
        while (true) {
        }
    }

    @Test
    public void test() throws InterruptedException {
        cache.put(KEY, "coy");

        // 单独启动一个线程打印信息
        new Thread(new Runnable() {
            @Override
            public void run() {
                while (true) {
                    try {
                        System.out.println(Thread.currentThread().getName() + "打印缓存:" + cache.asMap());
                        Thread.sleep(1000);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                }
            }
        }).start();

        // 开启一个获取缓存的线程，模拟获取新的值
        new Thread(new Runnable() {
            @Override
            public void run() {
                while (true) {
                    try {
                        System.out.println(Thread.currentThread().getName() + "获取缓存:" + cache.get(KEY));
                        Thread.sleep(1500);
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }
            }
        }).start();

        // hold住主线程
        while (true) {
        }
    }
}
