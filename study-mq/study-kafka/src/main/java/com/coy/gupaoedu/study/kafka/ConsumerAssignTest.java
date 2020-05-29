package com.coy.gupaoedu.study.kafka;

import org.junit.Test;

/**
 * @author chenck
 * @date 2019/8/21 11:12
 */
public class ConsumerAssignTest {

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
     * 消费组：ConsumerAssignGroup , 4个消费者
     * <p>
     * 场景：通过 KafkaConsumer#assign(partitions) 订阅my-replicated-topic-p2下指定partition的消息
     * 结论：
     * 1、只会消费指定partition下的消息。
     */
    @Test
    public void consumerTest() {
        ConsumerAssign consumer = new ConsumerAssign(topic);
        consumer.execute();
    }

    @Test
    public void consumerTest1() {
        ConsumerAssign consumer = new ConsumerAssign(topic);
        consumer.execute();
    }

    @Test
    public void consumerTest2() {
        ConsumerAssign consumer = new ConsumerAssign(topic);
        consumer.execute();
    }

    @Test
    public void consumerTest3() {
        ConsumerAssign consumer = new ConsumerAssign(topic);
        consumer.execute();
    }
}
