package com.coy.gupaoedu.study.kafka;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * Spring Cloud Stream 和 Kafka 的整合
 *
 * Spring Cloud Stream 是高度统一的流式处理分布式框架
 *
 * Spring Cloud Stream 是 Spring Cloud Stream DataFlow 和 Spring Cloud Bus 的核心依赖
 *
 * @author chenck
 * @date 2019/9/4 13:05
 */
@SpringBootApplication
@RestController
public class SpringCloudStreamApplication {

    public static void main(String[] args) {
        SpringApplication.run(SpringCloudStreamApplication.class, args);
    }

    /**
     * 构造器注入
     */
    private KafkaTemplate<String, Object> kafkaTemplate;

    /**
     * 构造器注入
     */
    public SpringCloudStreamApplication(KafkaTemplate<String, Object> kafkaTemplate) {
        this.kafkaTemplate = kafkaTemplate;
    }

    /**
     * 发送 kafka 消息
     */
    @GetMapping("/send")
    public String sendMsg(String name) {
        // 通过spring的KafkaTemplate来发送kafka消息
        kafkaTemplate.send("test", name.getBytes());
        return name;
    }

    /**
     * 监听 kafka topic 消息并消費
     * 通过注解的方式实现监听，也可以通过API的方式实现监听
     *
     * TODO @StreamListener
     */
    @KafkaListener(topics = {"test"})
    public void listen(String name) {
        System.out.println(name);
    }
}
