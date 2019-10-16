package com.coy.gupaoedu.study.cache;

import com.google.common.base.Stopwatch;
import com.google.common.cache.CacheBuilder;
import com.google.common.cache.CacheLoader;
import com.google.common.cache.LoadingCache;
import com.google.common.util.concurrent.ListenableFuture;
import com.google.common.util.concurrent.ListeningExecutorService;
import com.google.common.util.concurrent.MoreExecutors;
import org.junit.Test;

import java.util.UUID;
import java.util.concurrent.Callable;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;

/**
 * refreshAfterWrite可以做到：只阻塞加载数据的线程，其余线程返回旧数据。
 *
 * @author chenck
 * @date 2019/10/16 18:10
 */
public class RefreshAfterWriteTest {

    // 模拟一个需要耗时2s的数据库查询任务
    private static Callable<String> callable = new Callable<String>() {
        @Override
        public String call() throws Exception {
            System.out.println(Thread.currentThread().getName() + " begin to mock query db...");
            Thread.sleep(2000);
            System.out.println(Thread.currentThread().getName() + " success to mock query db...");
            return UUID.randomUUID().toString();
        }
    };

    // guava线程池,用来产生ListenableFuture
    private static ListeningExecutorService executorService =
            MoreExecutors.listeningDecorator(Executors.newFixedThreadPool(10));

    // 1s后刷新缓存
    // 只有真正获取缓存的线程才会进入CacheLoader中
    private static LoadingCache<String, String> cache = CacheBuilder.newBuilder().refreshAfterWrite(1, TimeUnit.SECONDS)
            .build(new CacheLoader<String, String>() {
                /**
                 * 该方法在
                 */
                @Override
                public String load(String key) throws Exception {
                    System.out.println(Thread.currentThread().getName() + " 执行 CacheLoader.load 同步刷新" + key);
                    return callable.call();
                }

                /**
                 * 注意：reload() 默认调用 load() 来同步执行
                 *
                 * 此处扩展 reload()，将 load()中的逻辑 交给线程池来异步执行，也就是不会走到 load() 中了
                 */
                public ListenableFuture<String> reload(String key, String oldValue) throws Exception {
                    // 默认实现：reload() 直接调用了 load(key)
                    // return Futures.immediateFuture(load(key));

                    // 自定义扩展实现：将 load()中的逻辑抽取出来 交给线程池来异步执行
                    System.out.println(Thread.currentThread().getName() + " ......后台线程池异步刷新:" + key);
                    return executorService.submit(callable);
                }
            });

    private static CountDownLatch latch = new CountDownLatch(1);

    private static void startThread(int id) {
        Thread t = new Thread(new Runnable() {
            @Override
            public void run() {
                try {
                    System.out.println(Thread.currentThread().getName() + " begin");
                    latch.await();
                    Stopwatch watch = Stopwatch.createStarted();
                    System.out.println(Thread.currentThread().getName() + " value = " + cache.get("name"));
                    watch.stop();
                    System.out.println(Thread.currentThread().getName() + " finish, cost time=" + watch.elapsed(TimeUnit.SECONDS) + "s");
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        });

        t.setName("Thread-" + id);
        t.start();
    }

    /**
     * 测试cache中有数据的情况 refreshAfterWrite
     * 结果：只阻塞加载数据的线程，其余线程返回旧数据。
     * <p>
     * 问题：真正加载数据的那个线程一定会阻塞，我们希望这个加载过程是异步的。
     * 分析：refreshAfterWrite默认的刷新是同步的，会在调用者的线程中执行。
     * 处理：通过实现CacheLoader.reload()来异步刷新。
     */
    @Test
    public void refreshAfterWriteTest() throws Exception {

        // 手动添加一条缓存数据,睡眠1.5s让其过期
        cache.put("name", "coy");
        Thread.sleep(1500);

        for (int i = 0; i < 8; i++) {
            startThread(i);
        }
        // 让线程运行
        latch.countDown();

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
            startThread(i);
        }
        // 让线程运行
        latch.countDown();

        // hold住主线程
        while (true) {
        }
    }
}
