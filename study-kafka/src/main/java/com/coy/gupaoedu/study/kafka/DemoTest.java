package com.coy.gupaoedu.study.kafka;

/**
 * @author chenck
 * @date 2019/8/21 11:12
 */
public class DemoTest {

    private static final String topic = "topic1";

    public static void main(String[] args) {
        boolean isAsync = args.length == 0 || !args[0].trim().equalsIgnoreCase("sync");
        Producer producerThread = new Producer(topic, isAsync);
        producerThread.start();

        Consumer consumerThread = new Consumer(topic);
        consumerThread.start();

    }
}
