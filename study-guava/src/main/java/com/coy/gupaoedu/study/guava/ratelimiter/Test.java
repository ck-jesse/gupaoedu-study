package com.coy.gupaoedu.study.guava.ratelimiter;

import com.ck.platform.common.util.DateUtils;
import com.google.common.util.concurrent.RateLimiter;

import java.util.Date;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

/**
 * @author chenck
 * @date 2020/4/20 20:07
 */
public class Test {

    @org.junit.Test
    public void testSample() {
        // RateLimiter使用的是一种叫令牌桶的流控算法，RateLimiter会按照一定的频率往桶里扔令牌，线程拿到令牌才能执行。
        // permitsPerSecond 每秒许可数，根据指定的稳定吞吐率创建RateLimiter，这里的吞吐率是指每秒多少许可数（通常是指QPS，每秒多少查询）。
        // RateLimiter 并不提供公平性的保证。
        // 速率是每秒两个许可
        RateLimiter rateLimiter = RateLimiter.create(3.0);

        ExecutorService executor = Executors.newFixedThreadPool(5);
        for (int i = 0; i < 10; i++) {
            // acquire 从RateLimiter获取一个许可，该方法会被阻塞直到获取到请求
            double microsToWait = rateLimiter.acquire(); // 也许需要等待
            System.out.println(microsToWait);
            int finalI = i;
            executor.execute(() -> {
                System.out.println(DateUtils.dateToStr(new Date(), DateUtils.YYYY_MM_DD_HH_MM_SS_SSS) + " thread" + finalI);
            });
        }
    }

    @org.junit.Test
    public void testSample1() throws InterruptedException {
        RateLimiter rateLimiter = RateLimiter.create(3.0);

        Thread.sleep(1000);

        ExecutorService executor = Executors.newFixedThreadPool(5);
        for (int i = 0; i < 10; i++) {
            int finalI = i;
            // 从RateLimiter 获取许可，如果该许可可以在无延迟下的情况下立即获取得到的话
            if (rateLimiter.tryAcquire()) {
//            if (rateLimiter.tryAcquire(300, TimeUnit.MILLISECONDS)) {
                executor.execute(() -> {
                    System.out.println(DateUtils.dateToStr(new Date(), DateUtils.YYYY_MM_DD_HH_MM_SS_SSS) + " thread" + finalI);
                });
            } else {
                System.out.println(DateUtils.dateToStr(new Date(), DateUtils.YYYY_MM_DD_HH_MM_SS_SSS) + " thread" + finalI + "  无可用许可");
            }
        }
    }
}
