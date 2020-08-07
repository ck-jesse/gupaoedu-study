package com.coy.gupaoedu.study.jvm.problem;

/**
 * 模拟死锁
 *
 * @author chenck
 * @date 2020/8/7 16:43
 */
public class DeadLockDemo {

    /**
     * 通过 jstack pid 查看线程堆栈信息，分析是否有死锁
     */
    public static void main(String[] args) {
        DeadLock d1 = new DeadLock(true);
        DeadLock d2 = new DeadLock(false);
        Thread t1 = new Thread(d1);
        Thread t2 = new Thread(d2);
        t1.start();
        t2.start();
    }
}

//定义锁对象
class MyLock {
    public static Object obj1 = new Object();
    public static Object obj2 = new Object();
}

//死锁代码
class DeadLock implements Runnable {
    private boolean flag;

    DeadLock(boolean flag) {
        this.flag = flag;
    }

    @Override
    public void run() {
        if (flag) {
            while (true) {
                synchronized (MyLock.obj1) {
                    System.out.println(Thread.currentThread().getName() + "----if获得obj1锁");
                    synchronized (MyLock.obj2) {
                        System.out.println(Thread.currentThread().getName() + "----if获得obj2锁");
                    }
                }
            }
        } else {
            while (true) {
                synchronized (MyLock.obj2) {
                    System.out.println(Thread.currentThread().getName() + "----否则获得obj2锁");
                    synchronized (MyLock.obj1) {
                        System.out.println(Thread.currentThread().getName() + "----否则获得obj1锁");
                    }
                }
            }
        }
    }
}

