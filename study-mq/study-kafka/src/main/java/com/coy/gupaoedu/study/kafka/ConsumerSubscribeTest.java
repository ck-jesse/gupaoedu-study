package com.coy.gupaoedu.study.kafka;

import org.junit.Test;

/**
 * 一个 partition 只能被同一个 group 内的一个 consumer 所消费（也就保障了一个消息只能被 group 内的一个 consuemr 所消费），但是多个 group 可以同时消费这个 partition。
 *
 * @author chenck
 * @date 2019/8/21 11:12
 */
public class ConsumerSubscribeTest {

    private static final String topic = "my-replicated-topic-p2";// 2个partition,3个replication

    @Test
    public void producerTest() {
        boolean isAsync = true;
        Producer producer = new Producer(topic, isAsync);
        //producer.start();
        producer.run();
    }

    /**
     * 问题：为什么kafka建议一个消费组下消费者的数量与topic的partition数量一致？
     * 验证：
     * topic：my-replicated-topic-p2 , 2个partition
     * 消费组：ConsumerSubscribeGroup , 4个消费者
     * <p>
     * 场景：通过 KafkaConsumer#subscribe(topic) 订阅my-replicated-topic-p2的消息
     * 结论：
     * 1、始终只有两个消费者可以消费到消息。当有消费者上线或下线时，会触发rebalance重平衡，保证只有两个消费者消费到消息。
     * 2、消费者只能消费某一个partition下的消息，也就是说一个partition的消息只会被一个消费者消费，这也就保证了消息的顺序消费。
     * 3、当消费者数量小于partition数量时，多余partition会被第一个消费者消费
     */
    @Test
    public void consumerTest() {
        ConsumerSubscribe consumerSubscribe = new ConsumerSubscribe(topic);
        //consumer.start();
        consumerSubscribe.execute();
    }

    @Test
    public void consumerTest1() {
        ConsumerSubscribe consumerSubscribe = new ConsumerSubscribe(topic);
        //consumer.start();
        consumerSubscribe.execute();
    }

    @Test
    public void consumerTest2() {
        ConsumerSubscribe consumerSubscribe = new ConsumerSubscribe(topic);
        //consumer.start();
        consumerSubscribe.execute();
    }

    @Test
    public void consumerTest3() {
        ConsumerSubscribe consumerSubscribe = new ConsumerSubscribe(topic);
        //consumer.start();
        consumerSubscribe.execute();
    }
}
