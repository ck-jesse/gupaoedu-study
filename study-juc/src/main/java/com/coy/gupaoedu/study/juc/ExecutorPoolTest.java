package com.coy.gupaoedu.study.juc;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

/**
 * @author chenck
 * @date 2020/6/28 20:02
 */
public class ExecutorPoolTest implements Runnable {

    @Override
    public void run() {
        System.out.println(Thread.currentThread().getName());
    }

    public static void main(String[] args) {
        ExecutorService executorService = null;
        // 单个线程的线程池
        executorService = Executors.newSingleThreadExecutor();
        // 固定线程数的线程池
        executorService = Executors.newFixedThreadPool(3);
        // 无限数量的线程池，默认最大空闲60s
        executorService = Executors.newCachedThreadPool();
        // 延迟或定期执行的调度线程池
        executorService = Executors.newScheduledThreadPool(3);

        for (int i = 0; i < 10; i++) {
            // 往线程池中提交的是Runnable或者Callable
            executorService.submit(new ExecutorPoolTest());
        }
    }
}
