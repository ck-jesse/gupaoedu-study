package com.coy.gupaoedu.study.jdk8.functional;

import java.util.function.Consumer;

/**
 * java.util.function.Consumer<T> 表示接受单个输入参数且不返回结果的操作。
 *
 * @author chenck
 * @date 2020/6/10 12:15
 */
public class ConsumerTest {

    private String name;

    public ConsumerTest(String name) {
        this.name = name;
    }

    public void consumer(Consumer<String> consumer) {
        consumer.accept(this.name);
    }

    public static void main(String[] args) {
        ConsumerTest test = new ConsumerTest("hello");

        test.consumer(s -> System.out.println("Consumer.accept() param = " + s));
    }
}
