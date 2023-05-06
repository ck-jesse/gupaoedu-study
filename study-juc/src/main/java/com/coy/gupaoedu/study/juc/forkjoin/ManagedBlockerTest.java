package com.coy.gupaoedu.study.juc.forkjoin;

import org.junit.Test;

import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.ForkJoinPool;
import java.util.concurrent.RecursiveTask;
import java.util.concurrent.TimeUnit;
import java.util.stream.Collectors;
import java.util.stream.Stream;

/**
 * @author chenck
 * @date 2023/5/5 18:44
 */
public class ManagedBlockerTest {

    public static void main(String[] args) throws InterruptedException {

        ForkJoinPool pool = new ForkJoinPool(2);

        String key = "key";
        for (int i = 0; i < 10; i++) {
            int finalI = i;
            // 构建任务并提交到线程池
            pool.execute(new RecursiveTask<Object>() {
                @Override
                protected Object compute() {
                    try {
                        MyManagedBlocker myManagedBlocker = new MyManagedBlocker(key + "" + finalI, key -> {
                            System.out.println("ThreadName=" + Thread.currentThread().getName() + ", 休眠2s");
                            try {
                                Thread.sleep(2000);
                            } catch (InterruptedException e) {
                                throw new RuntimeException(e);
                            }
                            return key;
                        });

                        System.out.println("ThreadName=" + Thread.currentThread().getName() + ", managedBlock , ActiveThreadCount=" + pool.getActiveThreadCount());

                        // 对IO阻塞型任务提供一个ManagedBlocker让线程池知道当前任务即将阻塞，因此需要创建新的补偿工作线程来执行新的提交任务
                        // 如何控制新创建的最大补偿线程数？
                        ForkJoinPool.managedBlock(myManagedBlocker);

                        System.out.println("ThreadName=" + Thread.currentThread().getName() + ", 休眠2s, result=" + myManagedBlocker.getResult() + ", ActiveThreadCount=" + pool.getActiveThreadCount());
                        setRawResult(myManagedBlocker.getResult());
                        return getRawResult();
                    } catch (InterruptedException e) {
                        throw new RuntimeException(e);
                    }
                }
            });

        }

        while (true) {
            Thread.sleep(5000);
            System.out.println(pool.getActiveThreadCount());
        }
    }

    static String threadDateTimeInfo() {
        return DateTimeFormatter.ISO_TIME.format(LocalTime.now()) + " " + Thread.currentThread().getName() + " ";
    }

    @Test
    public void test() {
        // 构建任务
        List<RecursiveTask> tasks = Stream.generate(() -> new RecursiveTask() {
            @Override
            protected Object compute() {
                System.out.println(threadDateTimeInfo() + "simulate io task blocking for 2 seconds···");
                try {
                    MyManagedBlocker myManagedBlocker = new MyManagedBlocker("key1", key -> {
                        System.out.println(threadDateTimeInfo() + ", 休眠2s");
                        try {
                            Thread.sleep(2000);
                        } catch (InterruptedException e) {
                            throw new RuntimeException(e);
                        }
                        return key;
                    });
                    ForkJoinPool.managedBlock(myManagedBlocker);

                    //线程休眠2秒模拟IO调用阻塞
                    TimeUnit.SECONDS.sleep(2);

                    return threadDateTimeInfo() + " io blocking task returns successfully: " + myManagedBlocker.getResult();
                } catch (InterruptedException e) {
                    throw new Error(e);
                }
            }
        }).limit(10).collect(Collectors.toList());

        tasks.forEach(recursiveTask -> recursiveTask.fork());

        tasks.forEach(recursiveTask -> {
            try {
                System.out.println(recursiveTask.get());
            } catch (InterruptedException e) {
                throw new RuntimeException(e);
            } catch (ExecutionException e) {
                throw new RuntimeException(e);
            }
        });
    }
}
