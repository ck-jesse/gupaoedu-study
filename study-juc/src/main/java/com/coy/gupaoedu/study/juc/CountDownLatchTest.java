package com.coy.gupaoedu.study.juc;

import java.util.concurrent.CountDownLatch;

/**
 * 通过CountDownLatch，分析AQS共享锁的实现方式
 *
 * @author chenck
 * @date 2019/5/16 15:45
 */
public class CountDownLatchTest {

    /**
     * 通过两个CountDownLatch，来达到同一时间点开始执行所有线程，并且等待所有线程执行完毕后再继续执行的场景
     * 注：一个控制所有线程同时启动执行，一个控制等待所有线程执行完再继续执行
     */
    public static void main(String[] args) throws InterruptedException {
        int num = 3;
        // 启动信号，用于控制拥有startSignal的线程同时开始执行
        CountDownLatch startSignal = new CountDownLatch(1);
        // 结束信号，用于控制拥有doneSignal的线程都执行完毕后再启动因doneSignal.await()阻塞的线程，此案例中为主线程
        CountDownLatch doneSignal = new CountDownLatch(num);

        for (int i = 0; i < num; ++i) {
            // 创建并启动线程
            String threadName = ("thread" + i);
            System.out.println("创建线程 " + threadName);
            new Thread(new Worker(startSignal, doneSignal), threadName).start();
        }

        System.out.println("主线程-准备启动所有线程");

        // 开始启动所有的线程
        startSignal.countDown();      // let all threads proceed
        System.out.println("主线程-启动所有阻塞的线程");

        // 等待所有线程执行完毕
        System.out.println("主线程-等待所有线程执行完毕");
        doneSignal.await();           // wait for all to finish
        System.out.println("主线程-所有线程执行完毕");
    }

    static class Worker implements Runnable {
        private final CountDownLatch startSignal;
        private final CountDownLatch doneSignal;

        Worker(CountDownLatch startSignal, CountDownLatch doneSignal) {
            this.startSignal = startSignal;
            this.doneSignal = doneSignal;
        }

        public void run() {
            try {
                // 等待启动
                System.out.println("等待唤醒 " + Thread.currentThread().getName());
                startSignal.await();
                System.out.println("唤醒成功 " + Thread.currentThread().getName());
                doWork();
                // 倒数同步计数器减1
                System.out.println("开始倒数 " + doneSignal.getCount() + " " + Thread.currentThread().getName());
                doneSignal.countDown();
                System.out.println("结束倒数 " + doneSignal.getCount() + " " + Thread.currentThread().getName());
            } catch (InterruptedException ex) {
            } // return;
        }

        void doWork() {
            System.out.println("do samething " + Thread.currentThread().getName());
        }
    }
}
