package com.coy.gupaoedu.study.kafka;

import org.apache.kafka.clients.producer.KafkaProducer;
import org.apache.kafka.clients.producer.ProducerConfig;
import org.apache.kafka.clients.producer.ProducerRecord;
import org.apache.kafka.common.serialization.IntegerSerializer;
import org.apache.kafka.common.serialization.StringSerializer;

import java.util.Properties;
import java.util.concurrent.ExecutionException;

/**
 * @author chenck
 * @date 2019/8/20 15:59
 */
public class Producer extends Thread {
    private final KafkaProducer<Integer, String> producer;
    private final String topic;
    private final Boolean isAsync;

    public Producer(String topic, Boolean isAsync) {
        Properties props = new Properties();
        props.put(ProducerConfig.BOOTSTRAP_SERVERS_CONFIG, "localhost:9092");
        props.put(ProducerConfig.CLIENT_ID_CONFIG, "DemoProducer");
        // 键的序列化
        props.put(ProducerConfig.KEY_SERIALIZER_CLASS_CONFIG, IntegerSerializer.class.getName());
        // 值的序列化
        props.put(ProducerConfig.VALUE_SERIALIZER_CLASS_CONFIG, StringSerializer.class.getName());
        // 发送消息的确认机制，默认0
        // acks=0：如果设置为0，生产者不会等待kafka的响应。
        // acks=1：这个配置意味着kafka会把这条消息写到leader的本地日志文件中，但是不会等待集群中其他机器的成功响应。
        // acks=all/-1：这个配置意味着leader会等待所有的follower同步完成。这个确保消息不会丢失，除非kafka集群中所有机器挂掉。这是最强的可用性保证。
        props.put(ProducerConfig.ACKS_CONFIG, "1");

        this.producer = new KafkaProducer<Integer, String>(props);
        this.topic = topic;
        this.isAsync = isAsync;
    }

    @Override
    public void run() {
        int messageNo = 1;
        while (true) {
            String messageStr = "Message_" + messageNo;
            long startTime = System.currentTimeMillis();
            if (isAsync) { // Send asynchronously
                // 异步发送消息
                // 未指定分区，但指定了key，则对key进行hash，再取模分区数的余数，得到具体的分区
                // 如果分区和key都未指定，则以轮询方式选择分区
                producer.send(new ProducerRecord<>(topic, messageNo, messageStr), new DemoCallBack(startTime, messageNo, messageStr));
            } else { // Send synchronously
                try {
                    // 同步发送消息
                    producer.send(new ProducerRecord<>(topic, messageNo, messageStr)).get();
                    System.out.println("Sent message: (" + messageNo + ", " + messageStr + ")");
                } catch (InterruptedException | ExecutionException e) {
                    e.printStackTrace();
                }
            }
            ++messageNo;
            try {
                Thread.sleep(1000);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
    }
}

