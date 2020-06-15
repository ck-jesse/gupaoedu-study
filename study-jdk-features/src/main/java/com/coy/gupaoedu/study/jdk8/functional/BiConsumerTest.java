package com.coy.gupaoedu.study.jdk8.functional;

import java.util.function.BiConsumer;

/**
 * java.util.function.Consumer<T> 表示接受两个输入参数且不返回结果的操作。
 *
 * @author chenck
 * @date 2020/6/12 9:41
 */
public class BiConsumerTest {

    private String name;
    private String addr;

    public BiConsumerTest(String name, String addr) {
        this.name = name;
        this.addr = addr;
    }

    public void consumer(BiConsumer<String, String> consumer) {
        consumer.accept(this.name, this.addr);
    }

    public static void main(String[] args) {
        BiConsumerTest test = new BiConsumerTest("hello", "world");

        test.consumer((s, s2) -> System.out.println("BiConsumerTest.accept() param = " + s + ",addr=" + s2));
    }
}
