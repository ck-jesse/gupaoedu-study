package com.coy.gupaoedu.study.kafka;

import org.apache.kafka.clients.consumer.ConsumerConfig;
import org.apache.kafka.clients.consumer.ConsumerRecord;
import org.apache.kafka.clients.consumer.ConsumerRecords;
import org.apache.kafka.clients.consumer.KafkaConsumer;
import org.apache.kafka.common.PartitionInfo;
import org.apache.kafka.common.TopicPartition;
import org.apache.kafka.connect.util.ShutdownableThread;

import java.time.Duration;
import java.util.ArrayList;
import java.util.List;
import java.util.Properties;

/**
 * 消费方式二：通过 assign 指定具体的topic和partition来消费
 *
 * @author chenck
 * @date 2019/8/20 15:59
 */
public class ConsumerAssign extends ShutdownableThread {

    private final KafkaConsumer<Integer, String> consumer;
    private final String topic;

    public ConsumerAssign(String topic) {
        super("KafkaConsumerExample", false);
        Properties props = new Properties();
        props.put(ConsumerConfig.BOOTSTRAP_SERVERS_CONFIG, "localhost:9092");
        props.put(ConsumerConfig.GROUP_ID_CONFIG, "ConsumerAssignGroup");
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

        // 设置一次最大拉取的消息条数
        props.put("max.poll.records", "20");

        // 分配策略：消息分配给消费组中的消费者的策略
        // 范围（Range）对于每个主题，每个消费者负责一定的连续范围分区。假如消费者C1和消费者C2订阅了两个主题，这两个主题都有3个分区，那么使用这个策略会导致消费者C1负责每个主题的分区0和分区1（下标基于0开始），消费者C2负责分区2
        // 。可以看到，如果消费者数量不能整除分区数，那么第一个消费者会多出几个分区（由主题数决定）。
        // 轮询（RoundRobin）对于所有订阅的主题分区，按顺序一一的分配给消费者。用上面的例子来说，消费者C1负责第一个主题的分区0、分区2，以及第二个主题的分区1；其他分区则由消费者C2负责。可以看到，这种策略更加均衡，所有消费者之间的分区数的差值最多为1。
        // partition.assignment.strategy 设置了分配策略，默认为org.apache.kafka.clients.consumer.RangeAssignor（使用范围策略），你可以设置为org.apache.kafka.clients.consumer
        // .RoundRobinAssignor（使用轮询策略），或者自己实现一个分配策略然后将partition.assignment.strategy指向该实现类。
//        props.put(ConsumerConfig.PARTITION_ASSIGNMENT_STRATEGY_CONFIG, "org.apache.kafka.clients.consumer.RangeAssignor");
        props.put(ConsumerConfig.PARTITION_ASSIGNMENT_STRATEGY_CONFIG, "org.apache.kafka.clients.consumer.RoundRobinAssignor");

        consumer = new KafkaConsumer<>(props);
        this.topic = topic;
    }

    @Override
    public void execute() {

        // 方式二：
        // 分配topic和partition
        // 指定消费partition=0的分区。只消费partition=0的分区
        // consumer.assign(Arrays.asList(new TopicPartition(this.topic, 0)));

        // 获取主题下所有的分区。消费所有partition下的消息
        List<PartitionInfo> partitionInfos = consumer.partitionsFor(this.topic);
        if (null != partitionInfos) {
            List partitions = new ArrayList();
            for (PartitionInfo partitionInfo : partitionInfos) {
                partitions.add(new TopicPartition(this.topic, partitionInfo.partition()));
            }
            consumer.assign(partitions);
        }
        // 不改变当前offset，指定从这个topic和partition的开始位置获取
        // consumer.seekToBeginning(Arrays.asList(new TopicPartition(this.topic, 0)));
        // 重置为最新位点，相当于丢弃所有堆积消息
        // consumer.seekToEnd(Arrays.asList(new TopicPartition(this.topic, 0)));

        int pollNo = 1;
        while (true) {
            // 拉取消息(指定超时时间)
            // 默认一次最多拉取500条消息
            ConsumerRecords<Integer, String> records = consumer.poll(Duration.ofSeconds(1).toMillis());
            System.out.println("pollNo = " + pollNo + ", records=" + records.count());

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
        }
    }

}
