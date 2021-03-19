package com.coy.gupaoedu.study.juc;

import org.junit.Assert;
import org.junit.Test;

import java.util.Random;
import java.util.concurrent.CompletableFuture;


/**
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
}
