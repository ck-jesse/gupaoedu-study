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
 * @author chenck
 * @date 2019/8/20 15:59
 */
public class Consumer extends ShutdownableThread {

    private final KafkaConsumer<Integer, String> consumer;
    private final String topic;

    public Consumer(String topic) {
        super("KafkaConsumerExample", false);
        Properties props = new Properties();
        props.put(ConsumerConfig.BOOTSTRAP_SERVERS_CONFIG, "localhost:9092");
        props.put(ConsumerConfig.GROUP_ID_CONFIG, "DemoConsumer");
        props.put(ConsumerConfig.ENABLE_AUTO_COMMIT_CONFIG, "true");
        props.put(ConsumerConfig.AUTO_COMMIT_INTERVAL_MS_CONFIG, "1000");
        props.put(ConsumerConfig.SESSION_TIMEOUT_MS_CONFIG, "30000");
        props.put(ConsumerConfig.KEY_DESERIALIZER_CLASS_CONFIG, "org.apache.kafka.common.serialization.IntegerDeserializer");
        props.put(ConsumerConfig.VALUE_DESERIALIZER_CLASS_CONFIG, "org.apache.kafka.common.serialization.StringDeserializer");
        // 消费的位置
        //props.put(ConsumerConfig.AUTO_OFFSET_RESET_CONFIG, "latest");

        consumer = new KafkaConsumer<>(props);
        this.topic = topic;
    }

    @Override
    public void execute() {
        // 方式一：
        //
        // 订阅消息
        consumer.subscribe(Collections.singletonList(this.topic));

        // 方式二：
        // 分配topic和partition
        // consumer.assign(Arrays.asList(new TopicPartition(this.topic, 0)));
        // 不改变当前offset，指定从这个topic和partition的开始位置获取
        // consumer.seekToBeginning(Arrays.asList(new TopicPartition(this.topic, 0)));

        int pollNo = 1;
        while (true) {
            // 拉取消息(指定超时时间)
            // 默认一次最多拉取500条消息
            ConsumerRecords<Integer, String> records = consumer.poll(Duration.ofSeconds(1).toMillis());
            System.out.println("pollNo = " + pollNo + ", records=" + records.count());
            for (ConsumerRecord<Integer, String> record : records) {
                System.out.println("Received message: (" + record.key() + ", " + record.value() + ") at offset " + record.offset());
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
