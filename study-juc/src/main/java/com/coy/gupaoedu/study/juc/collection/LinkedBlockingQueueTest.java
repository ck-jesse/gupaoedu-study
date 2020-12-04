package com.coy.gupaoedu.study.juc.collection;

import java.util.concurrent.LinkedBlockingQueue;

/**
 * @author chenck
 * @date 2020/12/3 16:34
 */
public class LinkedBlockingQueueTest {

    public static void main(String[] args) {

        LinkedBlockingQueue queue = new LinkedBlockingQueue(2);
        System.out.println(queue.offer("key1"));;
        System.out.println(queue.offer("key1"));;
        System.out.println(queue.offer("key2"));
        System.out.println(queue.offer("key3"));
        System.out.println(queue.contains("key1"));
        System.out.println(queue.poll());;
        System.out.println(queue.contains("key1"));
    }
}
