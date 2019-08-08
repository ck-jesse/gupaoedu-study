package com.coy.gupaoedu.study.juc.deadlock;

/**
 * 基于jstack分析线程堆栈
 *
 * @author chenck
 * @date 2019/8/7 14:52
 */
public class JStackTest {

    public static void main(String[] args) {
        Thread t1 = new Thread(new DeadLockclass(true));//建立一个线程
        Thread t2 = new Thread(new DeadLockclass(false));//建立另一个线程
        t1.start();//启动一个线程
        t2.start();//启动另一个线程
    }

}


class DeadLockclass implements Runnable {
    public boolean falg;// 控制线程

    DeadLockclass(boolean falg) {
        this.falg = falg;
    }

    public void run() {
        /**
         * 如果falg的值为true则调用t1线程
         */
        if (falg) {
            while (true) {
                synchronized (Suo.o1) {
                    System.out.println("o1 " + Thread.currentThread().getName());
                    synchronized (Suo.o2) {
                        System.out.println("o2 " + Thread.currentThread().getName());
                    }
                }
            }
        }
        /**
         * 如果falg的值为false则调用t2线程
         */
        else {
            while (true) {
                synchronized (Suo.o2) {
                    System.out.println("o2 " + Thread.currentThread().getName());
                    synchronized (Suo.o1) {
                        System.out.println("o1 " + Thread.currentThread().getName());
                    }
                }
            }
        }
    }
}

class Suo {
    static Object o1 = new Object();
    static Object o2 = new Object();
}


/**
 * 1、jps -l 查看java进程的pid
 * 5032 com.coy.gupaoedu.study.juc.deadlock.JStackTest
 * 2、jstack 5032
 * <p>
 * Found one Java-level deadlock:
 * =============================
 * "Thread-1":
 * waiting to lock monitor 0x000000001c917398 (object 0x000000076ba69090, a java.lang.Object),
 * which is held by "Thread-0"
 * "Thread-0":
 * waiting to lock monitor 0x000000001c919cd8 (object 0x000000076ba690a0, a java.lang.Object),
 * which is held by "Thread-1"
 * <p>
 * Java stack information for the threads listed above:
 * ===================================================
 * "Thread-1":
 * at com.coy.gupaoedu.study.juc.deadlock.DeadLockclass.run(JStackTest.java:52)
 * - waiting to lock <0x000000076ba69090> (a java.lang.Object)
 * - locked <0x000000076ba690a0> (a java.lang.Object)
 * at java.lang.Thread.run(Thread.java:745)
 * "Thread-0":
 * at com.coy.gupaoedu.study.juc.deadlock.DeadLockclass.run(JStackTest.java:39)
 * - waiting to lock <0x000000076ba690a0> (a java.lang.Object)
 * - locked <0x000000076ba69090> (a java.lang.Object)
 * at java.lang.Thread.run(Thread.java:745)
 * <p>
 * Found 1 deadlock.
 * <p>
 * 分析：
 * Thread-1在想要执行第52行的时候，当前锁住了资源<0x000000076ba690a0>,但是他在等待资源<0x000000076ba69090>
 * Thread-0在想要执行第27行的时候，当前锁住了资源<0x000000076ba69090>,但是他在等待资源<0x000000076ba690a0>
 * 由于这两个线程都持有资源，并且都需要对方的资源，所以造成了死锁。
 * 原因我们找到了，就可以具体问题具体分析，解决这个死锁了。
 */
