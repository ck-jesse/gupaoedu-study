package com.coy.gupaoedu.study.juc;

import java.util.concurrent.locks.Condition;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

/**
 * Condition 并发通信控制
 * <p>
 * Synchronized
 * wait/notify/notifyAll - JVM层面提供的线程通信机制
 * <p>
 * AQS-ReentrantLock
 * Condition - await/signal/signalAll - JDK代码层面提供的线程通信机制
 * <p>
 * Synchronized 和 ReentrantLock 竞争锁失败时，会将线程放入同步队列
 * wait和 await 等待并释放锁，会将线程放入等待队列
 * notify和signal 唤醒并竞争锁，会将线程从等待队列移到同步队列，并竞争锁
 * <p>
 * 【总结】Synchronized 和 AQS 底层原理是类似的，都是通过一个同步队列和等待队列来实现并发控制以及线程间的通信的。
 *
 * @author chenck
 * @date 2020/6/18 16:41
 */
public class ConditionTest {

    public static void main(String[] args) {
        ReentrantLock lock = new ReentrantLock();
        Condition condition = lock.newCondition();
        new Thread(new ThreadWait(lock, condition)).start();
        new Thread(new ThreadNotify(lock, condition)).start();
    }
}

class ThreadNotify implements Runnable {

    Lock lock;
    Condition condition;

    public ThreadNotify(Lock lock, Condition condition) {
        this.lock = lock;
        this.condition = condition;
    }

    @Override
    public void run() {
        try {
            lock.lock();
            System.out.println("ThreadNotify begin");
            condition.signal();
            System.out.println("ThreadNotify end");
        } finally {
            lock.unlock();
        }
    }
}

class ThreadWait implements Runnable {

    Lock lock;
    Condition condition;

    public ThreadWait(Lock lock, Condition condition) {
        this.lock = lock;
        this.condition = condition;
    }

    @Override
    public void run() {
        try {
            lock.lock();
            System.out.println("ThreadWait begin");
            condition.await();
            System.out.println("ThreadWait end");
        } catch (InterruptedException e) {
            e.printStackTrace();
        } finally {
            lock.unlock();
        }
    }
}
