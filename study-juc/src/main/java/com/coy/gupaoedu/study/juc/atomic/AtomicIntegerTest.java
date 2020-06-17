package com.coy.gupaoedu.study.juc.atomic;

import java.util.concurrent.atomic.AtomicInteger;

/**
 * 基于 AtomicInteger 实现轮询机制的负载均衡
 *
 * @author chenck
 * @date 2020/6/17 10:21
 */
public class AtomicIntegerTest {

    public static void main(String[] args) {
        AtomicInteger counter = new AtomicInteger(0);

        int hosts = 3;
        // 模拟40个请求
        for (int i = 0; i < 40; i++) {
            // 超限重置为0
            counter.compareAndSet(30, 0);

            int count = counter.addAndGet(1);
            // 相当于采用轮询机制来做负载均衡
            System.out.println(count + " " + count % hosts);
        }
    }
}
