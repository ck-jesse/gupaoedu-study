package com.coy.gupaoedu.study.juc;

import java.util.concurrent.CountDownLatch;
import java.util.concurrent.Executor;
import java.util.concurrent.Executors;

/**
 * 基于线程池的CountDownLatch测试
 *
 * @author chenck
 * @date 2019/5/16 15:29
 */
public class CountDownLatchExecutorTest {

    public static void main(String[] args) throws InterruptedException {
        int threadNum = 3;
        CountDownLatch doneSignal = new CountDownLatch(threadNum);
        Executor e = Executors.newFixedThreadPool(2);

        for (int i = 0; i < threadNum; ++i)
            // 创建并启动线程
            e.execute(new WorkerRunnable(doneSignal, i));

        System.out.println("等待执行 CountDownLatch=" + doneSignal.getCount());
        // 等待所有线程执行完
        doneSignal.await();           // wait for all to finish
        System.out.println("执行完毕 CountDownLatch=" + doneSignal.getCount());
    }

    static class WorkerRunnable implements Runnable {
        private final CountDownLatch doneSignal;
        private final int i;

        WorkerRunnable(CountDownLatch doneSignal, int i) {
            this.doneSignal = doneSignal;
            this.i = i;
        }

        public void run() {
            doWork(i);
            doneSignal.countDown();
        }

        void doWork(int i) {
            System.out.println("do something " + i);
        }
    }
}
