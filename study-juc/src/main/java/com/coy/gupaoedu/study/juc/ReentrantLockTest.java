package com.coy.gupaoedu.study.juc;

import java.util.concurrent.locks.ReentrantLock;

/**
 * 基于ReentrantLock测试多线程情况下AQS同步队列的原理及实现方式
 *
 * 通过ReentrantLock入手，分析AQS的独占锁的实现方式
 *
 * @author chenck
 * @date 2019/5/15 21:11
 */
public class ReentrantLockTest {

    ReentrantLock lock = new ReentrantLock();

    public void test1() {
        lock.lock();
        try {
            // Condition condition = lock.newCondition();
            System.out.println("test1 do something " + Thread.currentThread().getName());
        } finally {
            lock.unlock();
        }
    }

    public static void main(String[] args) {
        // 同一个对象多线程下的锁测试
        sameObjectTest();

        // 不同对象多线程下的锁测试
        diffObjectTest();
    }

    /**
     * 同一个对象多线程下的锁测试
     * 结果：不同线程对同一个对象进行操作，其中lock也是同一个，所以会有阻塞的情况
     */
    public static void sameObjectTest() {

        ReentrantLockTest lockTest = new ReentrantLockTest();

        Thread threadA = new Thread(() -> lockTest.test1(), "threadA");
        Thread threadB = new Thread(() -> lockTest.test1(), "threadB");
        Thread threadC = new Thread(() -> lockTest.test1(), "threadC");

        threadA.start();
        threadB.start();
        threadC.start();
    }

    /**
     * 不同对象多线程下的锁测试
     * 结果：不同的对象中lock也是不同的对象，所以互不相干
     */
    public static void diffObjectTest() {

        Thread threadA = new Thread(() -> new ReentrantLockTest().test1(), "threadA");
        Thread threadB = new Thread(() -> new ReentrantLockTest().test1(), "threadB");

        threadA.start();
        threadB.start();
    }
}
