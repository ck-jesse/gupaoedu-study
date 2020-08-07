package com.coy.gupaoedu.study.jvm.problem;

import java.util.concurrent.atomic.AtomicInteger;

/**
 * 模拟上下文切换频繁
 *
 * @author chenck
 * @date 2020/8/7 16:43
 */
public class ThreadSwapTest {

    public static void main(String[] args) {
        System.out.println("模拟线程切换");
        for (int i = 0; i < 10; i++) {
            new Thread(new ThreadSwap(), "thread-swap-" + i).start();
        }
    }

    public static class ThreadSwap implements Runnable {
        AtomicInteger count = new AtomicInteger(0);

        @Override
        public void run() {
            while (true) {
                count.addAndGet(1);
                Thread.yield();// 让出CPU资源，让CPU重新调度，CPU有可能继续调度该线程
            }
        }
    }
}


