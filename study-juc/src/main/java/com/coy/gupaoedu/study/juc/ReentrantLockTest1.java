package com.coy.gupaoedu.study.juc;

import java.util.concurrent.locks.ReentrantLock;

/**
 * 基于ReentrantLock测试 可重入锁
 *
 * @author chenck
 * @date 2019/5/15 21:11
 */
public class ReentrantLockTest1 {

    ReentrantLock lock = new ReentrantLock();

    public void test1() {
        lock.lock();
        try {
            System.out.println("test1 do something " + Thread.currentThread().getName());
            // 此处调用test2是为了测试同一个线程的锁可重入的场景
            test2();
        } finally {
            lock.unlock();
        }
    }

    /**
     * 可重入锁的测试，
     */
    public void test2() {
        lock.lock();
        try {
            System.out.println("test2 do something " + Thread.currentThread().getName());
        } finally {
            lock.unlock();
        }
    }

    /**
     * 可重入锁的测试：
     * 结果：可重入锁是为了解决同一个线程死锁的问题，所以若当前线程持有了锁，那么允许当前线程再次进入该锁，进入的次数记录在同步状态state字段上
     */
    public static void main(String[] args) {
        ReentrantLockTest1 lockTest = new ReentrantLockTest1();

        Thread threadA = new Thread(() -> lockTest.test1(), "threadA");
        Thread threadB = new Thread(() -> lockTest.test1(), "threadB");
        Thread threadC = new Thread(() -> lockTest.test1(), "threadC");

        threadA.start();
        threadB.start();
        threadC.start();
    }
}
