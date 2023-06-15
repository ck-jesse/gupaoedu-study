package com.coy.gupaoedu.study.juc.forkjoin;

import org.junit.Assert;
import org.junit.Test;

import java.util.Random;
import java.util.concurrent.CompletableFuture;


/**
 * Future模式的高阶版本—— CompletableFuture
 * Future模式虽然好用，但也有一个问题，那就是将任务提交给线程后，调用线程并不知道这个任务什么时候执行完，如果执行调用get()方法或者isDone()方法判断，可能会进行不必要的等待，那么系统的吞吐量很难提高。
 * 为了解决这个问题，JDK对Future模式又进行了加强，创建了一个CompletableFuture，它可以理解为Future模式的升级版本，
 * 它最大的作用是提供了一个回调机制，可以在任务完成后，自动回调一些后续的处理，这样，整个程序可以把“结果等待”完全给移除了。
 * <p>
 * 使用Future获得异步执行结果时，要么调用阻塞方法get()，要么轮询看isDone()是否为true，这两种方法都不是很好，因为主线程也会被迫等待。
 * 从Java 8开始引入了CompletableFuture，它针对Future做了改进，可以传入回调对象，当异步任务完成或者发生异常时，自动调用回调对象的回调方法。
 * <p>
 * CompletableFuture比一般的Future更具有实用性，因为它可以在Future执行成功后，自动回调进行下一步的操作，因此整个程序不会有任何阻塞的地方
 * （也就是说你不用去到处等待Future的执行，而是让Future执行成功后，自动来告诉你）。
 *
 * 理解：CompletableFuture模式本质上是消费者模式的一种实际应用。
 *
 * @author chenck
 * @date 2021/3/17 15:06
 */
public class CompletableFutureTest {

    private void randomSleep(int sleep) {
        try {
            if (sleep <= 0) {
                Random rand = new Random(10000);
                sleep = rand.ints(1000, 10000).findFirst().getAsInt();
            }
            System.out.println("sleep=" + sleep);
            Thread.sleep(sleep);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

    @Test
    public void example1() {
        CompletableFuture cf = CompletableFuture.completedFuture("message");
        Assert.assertTrue(cf.isDone());
        Assert.assertEquals("message", cf.getNow(null));
    }

    @Test
    public void thenApplyExample() {
        // thenApply 表示当前阶段(apply)执行完后，该阶段(thenApply)将以当前阶段(apply)的结果作为参数然后执行，并返回一个值
        CompletableFuture cf = CompletableFuture.completedFuture("message").thenApply(s -> {
            Assert.assertFalse(Thread.currentThread().isDaemon());
            randomSleep(2000);
            return s.toUpperCase();
        });
        // thenApply 的执行会阻塞，所以getNow()只有在 thenApply 完成后才会返回
        Assert.assertEquals("MESSAGE", cf.getNow(null));
    }

    @Test
    public void thenApplyAsyncExample() {
        // thenApplyAsync()用于串行化另一个CompletableFuture
        CompletableFuture cf = CompletableFuture.completedFuture("message").thenApplyAsync(s -> {
            Assert.assertTrue(Thread.currentThread().isDaemon());
            randomSleep(0);
            return s.toUpperCase();
        });
        Assert.assertNull(cf.getNow(null));
        Assert.assertEquals("MESSAGE", cf.join());
    }


    @Test
    public void runAsyncExample() {
        // 异步执行（使用ForkJoinPool.commonPool()）
        CompletableFuture cf = CompletableFuture.runAsync(() -> {
            Assert.assertTrue(Thread.currentThread().isDaemon());
            randomSleep(0);
        });
        Assert.assertFalse(cf.isDone());
        Assert.assertTrue(cf.isDone());
    }

    @Test
    public void thenAcceptExample() {
        // thenAccept 表示当前阶段(apply)执行完后，该阶段(thenAccept)将以当前阶段(apply)的结果作为参数然后执行，但是没有返回值
        StringBuilder result = new StringBuilder();
        CompletableFuture.completedFuture("thenAccept message").thenAccept(s -> result.append(s));
        Assert.assertTrue("Result was empty", result.length() > 0);
    }

    /**
     * 除了anyOf()可以实现“任意个CompletableFuture只要一个成功”，
     * allOf()可以实现“所有CompletableFuture都必须成功”，这些组合操作可以实现非常复杂的异步流程控制。
     * anyOf()和allOf()用于并行化多个CompletableFuture
     */
    @Test
    public void anyOfExample() throws InterruptedException {
        // 两个CompletableFuture执行异步查询:
        CompletableFuture<String> cfQueryFromSina = CompletableFuture.supplyAsync(() -> {
            return queryCode("中国石油sina", "https://finance.sina.com.cn/code/");
        });
        CompletableFuture<String> cfQueryFrom163 = CompletableFuture.supplyAsync(() -> {
            return queryCode("中国石油163", "https://money.163.com/code/");
        });

        // 用anyOf合并为一个新的CompletableFuture:
        // 任意个CompletableFuture只要一个成功
        CompletableFuture<Object> cfQuery = CompletableFuture.anyOf(cfQueryFromSina, cfQueryFrom163);

        // 两个CompletableFuture执行异步查询:
        CompletableFuture<Double> cfFetchFromSina = cfQuery.thenApplyAsync((code) -> {
            return fetchPrice((String) code, "https://finance.sina.com.cn/price/");
        });
        CompletableFuture<Double> cfFetchFrom163 = cfQuery.thenApplyAsync((code) -> {
            return fetchPrice((String) code, "https://money.163.com/price/");
        });

        // 用anyOf合并为一个新的CompletableFuture:
        CompletableFuture<Object> cfFetch = CompletableFuture.anyOf(cfFetchFromSina, cfFetchFrom163);

        // 最终结果:
        cfFetch.thenAccept((result) -> {
            System.out.println("price: " + result);
        });
        // 主线程不要立刻结束，否则CompletableFuture默认使用的线程池会立刻关闭:
        Thread.sleep(200);

    }

    static String queryCode(String name, String url) {
        System.out.println("query code from " + url + "..." + name);
        try {
            Thread.sleep((long) (Math.random() * 100));
        } catch (InterruptedException e) {
        }
        return "601857" + name;
    }

    static Double fetchPrice(String code, String url) {
        System.out.println("query price from " + url + "..." + code);
        try {
            Thread.sleep((long) (Math.random() * 100));
        } catch (InterruptedException e) {
        }
        return 5 + Math.random() * 20;
    }
}
