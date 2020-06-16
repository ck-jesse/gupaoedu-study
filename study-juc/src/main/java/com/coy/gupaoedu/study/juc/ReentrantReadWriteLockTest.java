package com.coy.gupaoedu.study.juc;

import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantReadWriteLock;

/**
 * 读写锁维护了一对锁，一个读锁、一个写锁; 一般情况下，读写锁的性能都会比排它锁好，因为大多数场景读是多于写的。在读 多于写的情况下，读写锁能够提供比排它锁更好的并发性和吞吐量.
 * <p>
 * 读写锁是一种适合读多写少的场景下解决线程安全问题的工具，基本原则是：读和读不互斥、读和写互斥、写和写互斥。也就是说涉及到影响数据变化的操作都会存在互斥。
 *
 * @author chenck
 * @date 2020/6/16 11:17
 */
public class ReentrantReadWriteLockTest {
    static Map<String, Object> cacheMap = new HashMap<>();
    static ReentrantReadWriteLock rwl = new ReentrantReadWriteLock();

    static Lock read = rwl.readLock();
    static Lock write = rwl.writeLock();

    public static final Object get(String key) {
        System.out.println("开始读取数据");
        read.lock();
        try {
            return cacheMap.get(key);
        } finally {
            read.unlock();
        }
    }

    public static final Object put(String key, String value) {
        System.out.println("开始写入数据");
        write.lock();
        try {
            return cacheMap.put(key, value);
        } finally {
            write.unlock();
        }
    }

    public static void main(String[] args) {
        for (int i = 0; i < 100; i++) {
            int finalI = i;
            new Thread(() -> {
                put(("key" + finalI), "value");
            }).start();
        }
    }
}
