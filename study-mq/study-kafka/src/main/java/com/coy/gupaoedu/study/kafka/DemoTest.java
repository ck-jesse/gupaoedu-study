package com.coy.gupaoedu.study.kafka;

import org.junit.Test;

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

    @Test
    public void producerTest() {
        boolean isAsync = true;
        Producer producer = new Producer(topic, isAsync);
        //producer.start();
        producer.run();
    }

    @Test
    public void consumerTest() {
        Consumer consumer = new Consumer(topic);
        //consumer.start();
        consumer.execute();
    }
}
