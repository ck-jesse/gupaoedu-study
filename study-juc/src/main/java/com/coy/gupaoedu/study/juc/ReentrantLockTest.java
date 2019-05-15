package com.coy.gupaoedu.study.juc;

import java.util.concurrent.locks.ReentrantLock;

/**
 * @author chenck
 * @date 2019/5/15 21:11
 */
public class ReentrantLockTest {

    ReentrantLock lock = new ReentrantLock();

    public void get() {
        lock.lock();
        try {
            System.out.println("do something");
        } finally {
            lock.unlock();
        }
    }
}
