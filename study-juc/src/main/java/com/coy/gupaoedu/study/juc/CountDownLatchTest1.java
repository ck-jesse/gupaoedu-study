package com.coy.gupaoedu.study.juc;

import java.util.concurrent.CountDownLatch;

/**
 * 通过该测试用例，阅读CountDownLatch的源码，是基于AQS的同步状态（作为计数器）+ 共享锁模式 来实现的线程同步
 *
 * @author chenck
 * @date 2019/5/16 15:45
 */
public class CountDownLatchTest1 {

    public static void main(String[] args) throws InterruptedException {
        int num = 3;
        // 结束信号，用于控制拥有doneSignal的线程都执行完毕后再启动因doneSignal.await()阻塞的线程，此案例中为主线程
        CountDownLatch doneSignal = new CountDownLatch(num);

        for (int i = 0; i < num; ++i) {
            // 创建并启动线程
            String threadName = ("Thread-" + i);
            System.out.println(threadName + " 被创建 ");
            new Thread(new Worker(doneSignal), threadName).start();
        }

        // 等待所有线程执行完毕
        System.out.println("主线程-等待所有线程执行完毕");
        doneSignal.await();           // wait for all to finish
        System.out.println("主线程-所有线程执行完毕");
    }

    static class Worker implements Runnable {
        private final CountDownLatch doneSignal;

        Worker(CountDownLatch doneSignal) {
            this.doneSignal = doneSignal;
        }

        public void run() {
            doWork();
            // 倒数同步计数器减1
            System.out.println(Thread.currentThread().getName() + " 开始倒数 " + doneSignal.getCount());
            doneSignal.countDown();
            System.out.println(Thread.currentThread().getName() + " 结束倒数 " + doneSignal.getCount());
        }

        void doWork() {
            System.out.println(Thread.currentThread().getName() + " do samething ");
        }
    }
}
