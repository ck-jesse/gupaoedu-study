package com.coy.gupaoedu.study.juc.forkjoin;

import java.util.concurrent.ForkJoinPool;

/**
 * @author chenck
 * @date 2023/5/6 13:49
 */
public class CustomForkJoinPoolExample {

    public static void main(String[] args) {
        // 创建自定义的ForkJoinWorkerThreadFactory
        int maxThreads = 10; // 最大线程数
        ForkJoinPool.ForkJoinWorkerThreadFactory workerThreadFactory = new LimitedThreadForkJoinWorkerThreadFactory(maxThreads);

        // 创建自定义的ForkJoinPool
        ForkJoinPool forkJoinPool = new ForkJoinPool(maxThreads, workerThreadFactory, null, false);

        // 执行任务...

        // 关闭ForkJoinPool
        forkJoinPool.shutdown();
    }

}
