package com.coy.gupaoedu.study.kafka;

import org.apache.kafka.clients.consumer.ConsumerConfig;
import org.apache.kafka.clients.consumer.ConsumerRecord;
import org.apache.kafka.clients.consumer.ConsumerRecords;
import org.apache.kafka.clients.consumer.KafkaConsumer;
import org.apache.kafka.connect.util.ShutdownableThread;

import java.time.Duration;
import java.util.Collections;
import java.util.Properties;

/**
 * 消费方式一：Subscribe 订阅消息
 *
 * @author chenck
 * @date 2019/8/20 15:59
 */
public class ConsumerSubscribe extends ShutdownableThread {

    private final KafkaConsumer<Integer, String> consumer;
    private final String topic;

    public ConsumerSubscribe(String topic) {
        super("KafkaConsumerExample", false);
        Properties props = new Properties();
        props.put(ConsumerConfig.BOOTSTRAP_SERVERS_CONFIG, "localhost:9092");
        props.put(ConsumerConfig.GROUP_ID_CONFIG, "ConsumerSubscribeGroup");
        props.put(ConsumerConfig.ENABLE_AUTO_COMMIT_CONFIG, "true");// 自动提交offset（默认true）将在后台定期提交
        props.put(ConsumerConfig.AUTO_COMMIT_INTERVAL_MS_CONFIG, "1000");// 自动提交间隔
        props.put(ConsumerConfig.SESSION_TIMEOUT_MS_CONFIG, "30000");
        props.put(ConsumerConfig.KEY_DESERIALIZER_CLASS_CONFIG, "org.apache.kafka.common.serialization.IntegerDeserializer");
        props.put(ConsumerConfig.VALUE_DESERIALIZER_CLASS_CONFIG, "org.apache.kafka.common.serialization.StringDeserializer");
        // 消费的位置
        // earliest: 当各分区下有已提交的offset时，从提交的offset开始消费；无提交的offset时，从头开始消费 。
        // latest: 当各分区下有已提交的offset时，从提交的offset开始消费；无提交的offset时，消费新产生的该分区下的数据 。
        // none: topic各分区都存在已提交的offset时，从offset后开始消费；只要有一个分区不存在已提交的offset，则抛出异常
        //props.put(ConsumerConfig.AUTO_OFFSET_RESET_CONFIG, "latest");

        // 设置一次最大拉取的消息条数（默认500）
        props.put(ConsumerConfig.MAX_POLL_RECORDS_CONFIG, "20");
        // 设置poll最大时间间隔（默认300s），如果超时过期之前未调用poll（），则认为consumer失败，该组将重新平衡，以便将分区重新分配给另一个成员。
        // 问题：假如一次拉取500条消息，而处理这500条消息耗时需要400s，那么就会出现consumer没有提交offset，导致消息被重复消费的情况出现。
        // 方案：consumer每次poll的数据业务处理时间不能超过max.poll.interval.ms，可以考虑调大超时时间或者调小每次poll的数据量。
        props.put(ConsumerConfig.MAX_POLL_INTERVAL_MS_CONFIG, "3000");

        // 分区分配策略：消息分配给消费组中的消费者的策略
        // 范围（Range）对于每个主题，每个消费者负责一定的连续范围分区。假如消费者C1和消费者C2订阅了两个主题，这两个主题都有3个分区，那么使用这个策略会导致消费者C1负责每个主题的分区0和分区1（下标基于0开始），消费者C2负责分区2
        // 。可以看到，如果消费者数量不能整除分区数，那么第一个消费者会多出几个分区（由主题数决定）。
        // 轮询（RoundRobin）对于所有订阅的主题分区，按顺序一一的分配给消费者。用上面的例子来说，消费者C1负责第一个主题的分区0、分区2，以及第二个主题的分区1；其他分区则由消费者C2负责。可以看到，这种策略更加均衡，所有消费者之间的分区数的差值最多为1。
        // partition.assignment.strategy 设置了分配策略，默认为org.apache.kafka.clients.consumer.RangeAssignor（使用范围策略），
        // 可以设置为org.apache.kafka.clients.consumer.RoundRobinAssignor（使用轮询策略），或者自己实现一个分配策略然后将partition.assignment.strategy指向该实现类。
//        props.put(ConsumerConfig.PARTITION_ASSIGNMENT_STRATEGY_CONFIG, "org.apache.kafka.clients.consumer.RangeAssignor");
        props.put(ConsumerConfig.PARTITION_ASSIGNMENT_STRATEGY_CONFIG, "org.apache.kafka.clients.consumer.RoundRobinAssignor");

        consumer = new KafkaConsumer<>(props);
        this.topic = topic;
    }

    @Override
    public void execute() {
        // 方式一：
        // 订阅消息
        consumer.subscribe(Collections.singletonList(this.topic));

        int pollNo = 1;
        while (true) {
            // 拉取消息(指定超时时间)
            ConsumerRecords<Integer, String> records = consumer.poll(Duration.ofSeconds(1).toMillis());
            System.out.println("pollNo = " + pollNo + ", records=" + records.count());

            // 处理消息：要保证消息的幂等处理；
            // 同时为了增加consumer的消费能力，可以使用线程池消费，或者业务处理改为异步处理
            for (ConsumerRecord<Integer, String> record : records) {
                System.out.println(String.format("[Received message] topic=%s, partition=%d, key=%s, value=%s, offset=%s", record.topic(),
                        record.partition(), record.key(), record.value(), record.offset()));
            }
            try {
                Thread.sleep(1000);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            ++pollNo;
            consumer.commitSync();
        }
    }

}
