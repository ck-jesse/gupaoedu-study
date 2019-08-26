package com.coy.gupaoedu.study.juc.collection;

import java.util.concurrent.SynchronousQueue;

/**
 * SynchronousQueue 数据同步交换的队列
 * 一个虚假的队列，因为它实际上没有真正用于存储元素的空间，每个插入操作都必须有对应的取出操作，没取出时无法继续放入。
 * JAVA中一个使用场景就是Executors.newCachedThreadPool()，创建一个缓存线程池。
 * <p>
 * 取出：0
 * 放入：0
 * 放入：1
 * 取出：1
 * 取出：2
 * 放入：2
 * 取出：3
 * 放入：3
 * 取出：4
 * 放入：4
 * 取出：5
 * 放入：5
 *
 * @author chenck
 * @date 2019/8/26 12:40
 */
public class SynchronousQueueTest {

    public static void main(String[] args) {

        SynchronousQueue<Integer> queue = new SynchronousQueue();

        // 写入线程
        new Thread(() -> {
            try {
                for (int i = 0; ; i++) {
                    queue.put(i);
                    System.out.println("放入：" + i);
                }
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }).start();

        // 读取线程
        new Thread(() -> {
            try {
                while (true) {
                    System.out.println("取出：" + queue.take());
                    Thread.sleep((long) (Math.random() * 2000));
                }
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }).start();
    }
}
