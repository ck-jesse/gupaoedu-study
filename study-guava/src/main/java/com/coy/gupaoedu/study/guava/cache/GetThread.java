package com.coy.gupaoedu.study.guava.cache;

import com.google.common.base.Stopwatch;
import com.google.common.cache.LoadingCache;

import java.util.concurrent.CountDownLatch;
import java.util.concurrent.TimeUnit;

/**
 * @author chenck
 * @date 2020/3/18 18:08
 */
public class GetThread {

    public static CountDownLatch latch = new CountDownLatch(1);

    public static void startThread(int id, String key, LoadingCache<String, String> cache) {
        Thread t = new Thread(new Runnable() {
            @Override
            public void run() {
                try {
                    System.out.println(Thread.currentThread().getName() + " cache 统计信息=" + cache.stats());
                    System.out.println(Thread.currentThread().getName() + " begin");
                    latch.await();
                    // Stopwatch 秒表，主要是实现当前从启动开始已消耗的时间，有点类似计算一个操作耗时，实现精度纳秒。
                    Stopwatch watch = Stopwatch.createStarted();
                    System.out.println(Thread.currentThread().getName() + " value = " + cache.get(key));
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
}
