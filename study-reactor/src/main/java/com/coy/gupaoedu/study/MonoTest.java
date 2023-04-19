package com.coy.gupaoedu.study;

import org.junit.jupiter.api.Test;
import reactor.core.publisher.Mono;

/**
 * Hello world!
 */
public class MonoTest {

    @Test
    public void just() {
        // subscribe() 中的 lambda 表达式实际上是 java.util.Consumer，用于创建响应式流的 Subscriber。
        // 由于调用了 subscribe() 方法，数据开始流动了。
        Mono.just("Hello World!").subscribe(System.out::println);
    }

}
