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
 * http://www.codexy.cn/manual/javaapi9/java/util/concurrent/ForkJoinPool.html
 * <p>
 * 静态commonPool()可用，适用于大多数应用。 公共池被任何未显式提交到指定池的ForkJoinTask使用。 使用公共池通常会减少资源使用（其线程在不使用期间缓慢回收，并在后续使用时恢复）。
 * <p>
 * 对于需要单独或自定义池的应用程序，可以使用给定的目标并行级别构建一个ForkJoinPool ; 默认情况下，等于可用处理器的数量。
 * 池尝试通过动态添加，挂起或恢复内部工作线程来维护足够的活动（或可用）线程，即使某些任务停止等待加入其他线程。
 * 但是，面对阻塞的I/O或其他非托管同步，不能保证这样的调整。 嵌套的ForkJoinPool.ManagedBlocker接口可以扩展所容纳的同步类型。
 * 可以使用具有与类ThreadPoolExecutor中记录的参数对应的参数的构造函数来覆盖默认策略。
 *
 * @author chenck
 * @date 2023/5/5 18:44
 */
public class ManagedBlockerTest {

    public static void main(String[] args) throws InterruptedException {

        // 线程池并行度（线程数）
        int parallelism = 5;
        // 最大线程数
        int maxThreads = 10;
        // 创建自定义的ForkJoinPool
        // 在管理阻塞时，通过自定义ForkJoinWorkerThreadFactory来限制最大可创建的线程数，避免无限制的创建线程
        // 适用于面对IO阻塞型任务时，通过扩展线程池中的线程数，来提高执行效率的场景
        ForkJoinPool pool = new ForkJoinPool(parallelism, new LimitedThreadForkJoinWorkerThreadFactory(maxThreads), null, false);

        String key = "key";
        for (int i = 0; i < 100; i++) {
            int finalI = i;
            // 构建任务并提交到线程池
            pool.execute(new RecursiveTask<Object>() {
                @Override
                protected Object compute() {
                    try {
                        MyManagedBlocker myManagedBlocker = new MyManagedBlocker(key + "" + finalI, key -> {
                            System.out.println(threadDateTimeInfo() + ", 休眠2s");
                            try {
                                Thread.sleep(2000);
                            } catch (InterruptedException e) {
                                throw new RuntimeException(e);
                            }
                            return key;
                        });


                        // 运行指定的阻塞任务。当ForkJoinTask 在 ForkJoinPool 中运行时，此方法可能会安排在必要时激活备用线程，以确保当前线程在 ManagedBlockerblock.block() 中阻塞时有足够的并行性。
                        // 对IO阻塞型任务提供一个ManagedBlocker让线程池知道当前任务即将阻塞，因此需要创建新的补偿工作线程来执行新的提交任务
                        // 问题：如何控制新创建的最大补偿线程数？
                        // 分析：ForkJoinPool.common.commonMaxSpares tryCompensate 中备用线程构造的限制，默认为256
                        // 缺陷：只能针对commonPool进行限制，并且tryCompensate方法不一定能会命中该限制，若未命中该限制，则可能无限制的创建补偿线程来避免阻塞，最终可能出现oom
                        // 实现注意事项 ：此实现将最大正在运行的线程数限制为32767.尝试创建大于最大数目的池导致IllegalArgumentException
                        // 只有当池被关闭或内部资源耗尽时，此实现才会拒绝提交的任务（即通过抛出RejectedExecutionException ）。
                        // 方案：在管理阻塞时，通过自定义ForkJoinWorkerThreadFactory来限制最大可创建的线程数，避免无限制的创建线程
                        ForkJoinPool.managedBlock(myManagedBlocker);

                        System.out.println(threadDateTimeInfo() + ", 休眠2s, result=" + myManagedBlocker.getResult() + ", ActiveThreadCount=" + pool.getActiveThreadCount() + ", PoolSize=" + pool.getPoolSize());
                        setRawResult(myManagedBlocker.getResult());
                        return getRawResult();
                    } catch (InterruptedException e) {
                        throw new RuntimeException(e);
                    }
                }
            });

        }

        while (true) {
            Thread.sleep(2000);
            System.out.println(threadDateTimeInfo() + ", pool=" + pool.toString());
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
