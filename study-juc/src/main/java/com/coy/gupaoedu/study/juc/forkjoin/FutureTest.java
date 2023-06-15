package com.coy.gupaoedu.study.juc.forkjoin;

import org.junit.Test;

import java.util.concurrent.Callable;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;

/**
 *
 * 在执行多个任务的时候，使用Java标准库提供的线程池是非常方便的。我们提交的任务只需要实现Runnable接口，就可以让线程池去执行：
 * Runnable接口有个问题，它的方法没有返回值。如果任务需要一个返回结果，那么只能保存到变量，还要提供额外的方法读取，非常不便。
 * 所以，Java标准库还提供了一个Callable接口，和Runnable接口比，它多了一个返回值，并且Callable接口是一个泛型接口，可以返回指定类型的结果。
 * <p>
 * 现在的问题是，如何获得异步执行的结果？
 * 如果仔细看ExecutorService.submit()方法，可以看到，它返回了一个Future类型，一个Future类型的实例代表一个未来能获取结果的对象：
 * <p>
 * 当我们提交一个Callable任务后，我们会同时获得一个Future对象，然后，我们在主线程某个时刻调用Future对象的get()方法，就可以获得异步执行的结果。
 * 在调用get()时，如果异步任务已经完成，我们就直接获得结果。如果异步任务还没有完成，那么get()会阻塞，直到任务完成后才返回结果。
 *
 * 理解：Future模式本质上是代理模式的一种实际应用。
 *
 * https://zhuanlan.zhihu.com/p/364041672 （JAVA Future类详解）
 *
 * @author chenck
 * @date 2022/6/10 22:39
 */
public class FutureTest {

    @Test
    public void test() throws ExecutionException, InterruptedException {
        ExecutorService executor = Executors.newFixedThreadPool(4);
        // 提交任务并获得Future:
        Future<String> future = (Future<String>) executor.submit(() -> {
            try {
                Thread.sleep(1000);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            return "longTimeCalculation";
        });
        // 从Future获取异步执行返回的结果:
        String result = future.get(); // 可能阻塞
        System.out.println(result);
    }
}
