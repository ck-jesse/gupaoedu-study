package com.coy.gupaoedu.study.juc;

/**
 * 重入锁的目的是为了避免线程死锁的问题
 */
public class ReentrantTest {

    public synchronized void demo() {// main线程获得锁
        System.out.println("demo");
    }

    public void demo2() {
        synchronized (this) {// 重入锁（记录锁的重入次数）
            System.out.println("demo2");
        }// 释放锁的重入次数
    }

    public static void main(String[] args) throws InterruptedException {
        ReentrantTest app = new ReentrantTest();
        app.demo();
        app.wait();
        app.notify();
    }
}
