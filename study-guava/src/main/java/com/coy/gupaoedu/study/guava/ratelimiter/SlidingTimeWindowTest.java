package com.coy.gupaoedu.study.guava.ratelimiter;

import java.util.LinkedList;
import java.util.Random;

/**
 * 滑动时间窗口算法限流
 * 定义：滑动窗口算法是将时间周期分为N个小周期，分别记录每个小周期内访问次数，并且根据时间滑动删除过期的小周期。
 * <p>
 * 滑动时间窗口限流统计的精准度是由划分的格子多少决定的。当滑动窗口的格子划分的越多，那么滑动窗口的滚动就越平滑，限流的统计就会越精确。
 * 理解：
 * 把1秒中进行划分成多个时间段，比如2个格子的话，那么就是2段，0-500ms和501-1000ms。
 * 那么就会两个值进行存储统计请求量，比如数组[0,1] 各存储一个段的请求值。
 * 注：计数器算法其实就是滑动时间窗口算法。计算器算法是滑动窗口算法将时间段划分为1的特殊情况。
 * <p>
 * 计算器算法：
 * 定义：对单位时间内访问的次数限制
 * 问题：临界问题，在临界点的瞬间大量请求，会超过我的速率限制。
 * 方案：临界问题本质是统计精度太低，所以我们可以提高精度，那么就引入了滑动窗口算法。
 * <p>
 * 场景：
 * 假设某个服务最多只能每秒钟处理100个请求，我们可以设置一个1秒钟的滑动时间窗口，
 * 窗口中有10个格子，每个格子100毫秒，每100毫秒移动一次，每次移动都需要记录当前服务请求的次数
 *
 * @author chenck
 * @date 2020/4/23 19:39
 */
public class SlidingTimeWindowTest {

    // 时间窗口内最大请求数
    public final int limit = 100;
    // 服务访问次数
    Long counter = 0L;
    // 使用LinkedList来记录滑动窗口的10个格子。
    LinkedList<Long> slots = new LinkedList<Long>();
    // 时间划分多少段落
    int split = 10;
    // 是否限流了,true:限流了，false：允许正常访问。
    boolean isLimit = false;

    private void doCheck() throws InterruptedException {
        while (true) {
            slots.addLast(counter);
            if (slots.size() > split) {
                slots.removeFirst();// 超出了，就把第一个移出。
            }

            // 比较最后一个和第一个，两者相差100以上就限流
            if ((slots.peekLast() - slots.peekFirst()) > limit) {
                System.out.println("限流了。。");
                // 修改限流标记为true
                isLimit = true;
            } else {
                // 修改限流标记为false
                isLimit = false;
            }
            Thread.sleep(1000 / split);
        }
    }

    public static void main(String[] args) throws InterruptedException {
        SlidingTimeWindowTest timeWindow = new SlidingTimeWindowTest();
        //开启一个线程判断当前的限流情况.
        new Thread(new Runnable() {
            @Override
            public void run() {
                try {
                    timeWindow.doCheck();
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
        }).start();

        // 模拟请求.
        while (true) {
            //判断是否被限流了.
            if (!timeWindow.isLimit) {
                timeWindow.counter++;
                //未被限流执行相应的业务方法.
                //  executeBusinessCode();
                //模拟业务执行方法时间.
                Thread.sleep(new Random().nextInt(15));
                System.out.println("业务方法执行完了...");
            } else {
                System.out.println("被限流了，直接返回给用户");
            }
        }
    }
}
