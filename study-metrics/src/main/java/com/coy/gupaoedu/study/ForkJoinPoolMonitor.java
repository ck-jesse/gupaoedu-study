package com.coy.gupaoedu.study;

import io.micrometer.core.instrument.Gauge;
import io.micrometer.core.instrument.MeterRegistry;
import io.micrometer.core.instrument.binder.jvm.ExecutorServiceMetrics;
import org.springframework.context.annotation.Configuration;

import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.util.concurrent.ForkJoinPool;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.ScheduledThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

/**
 * 通过 micrometer 去监控 ForkJoinPool，可以使用 Micrometer 提供的适配器和指标来收集和记录相关的性能指标。
 *
 * @author chenck
 * @date 2023/5/8 15:45
 */
@Configuration
public class ForkJoinPoolMonitor {

    /**
     * 问题：不同的 ForkJoinPool实例 应该如何被监控？
     * 分析：在 ForkJoinPool 创建时？
     */
    public ForkJoinPoolMonitor(MeterRegistry meterRegistry) {

        String poolName1 = "forkjoin-monitor-test1";
        String poolName2 = "forkjoin-monitor-test2";
        ForkJoinPool pool1 = createPoolAndMock(poolName1);
        ForkJoinPool pool2 = createPoolAndMock(poolName2);

        // 模拟监控多个ForkJoinPool线程池

        // 创建并注册ForkJoinPool的指标
        // 通过Micrometer提供的API，可以访问和查询已注册的指标，以获取关于ForkJoinPool的性能数据。
//        ExecutorServiceMetrics.monitor(meterRegistry, pool1, poolName1);
//        ExecutorServiceMetrics.monitor(meterRegistry, pool2, poolName2);

        ForkJoinPoolMetrics.monitor(meterRegistry, pool1, poolName1);
        ForkJoinPoolMetrics.monitor(meterRegistry, pool2, poolName2);

        // 访问和查询指标
        // 通过MeterRegistry的find()方法查找activeThreadCount指标，使用gauge()方法获取该指标的Gauge实例。
        // 然后，我们可以使用value()方法获取指标的值。
        Gauge activeThreadGauge1 = meterRegistry.find(poolName1 + ".activeThreadCount").gauge();
        if (null != activeThreadGauge1) {
            double activeThreads = activeThreadGauge1.value();
            System.out.println("activeThreads=" + activeThreads);
        }

        // 通过Micrometer的适配器和指标，可以方便地收集和记录ForkJoinPool的性能指标，并与其他监控系统集成，以实现实时监控、报警和性能分析。

    }

    /**
     * 模拟创建ForkJoinPool线程池
     * 问题：如何监控 ForkJoinPool 线程池中单个线程是否健康呢？
     * 分析：
     * ForkJoinPool中的线程是由线程工作窃取算法来执行任务的，每个线程都可能从其他线程中窃取任务执行。
     * 因此，并没有直接的方法来监控某个具体线程的健康状况。
     * ForkJoinPool中的线程是由线程池自动管理的，你不能直接访问或监控单个线程。
     * 然而，我们可以间接地监控ForkJoinPool中的线程健康情况。以下是一种可能的方法：
     * 1.监控活动线程数：可以使用Gauge指标来监控ForkJoinPool的活动线程数。活动线程数较高可能表示线程池中的线程正在执行任务，而较低的活动线程数可能表示线程池中的线程处于空闲或阻塞状态。
     * 2.监控任务队列大小：可以使用Gauge指标来监控ForkJoinPool的任务队列大小。如果任务队列较大，则表示有更多的任务在等待执行，而较小的任务队列可能表示线程池中的线程正在忙于执行任务。
     * 通过监控这些指标，可以获取线程池整体的健康状态和工作负载情况。如果活动线程数持续高于预期或任务队列持续增长，可能表示线程池中的线程不够健康，可能需要进行调优或采取其他措施。
     */
    private ForkJoinPool createPoolAndMock(String poolName) {
        // 模拟执行ForkJoinPool任务
        ForkJoinPool pool = new ForkJoinPool(10, new LimitedThreadForkJoinWorkerThreadFactory(10, poolName), null, false);

        ScheduledExecutorService scheduler = new ScheduledThreadPoolExecutor(1, new DaemonThreadFactory("scheduled-monitor-test"));
        scheduler.scheduleAtFixedRate(() -> {
            for (int i = 0; i < 100; i++) {
                pool.execute(() -> {
                    System.out.println(threadDateTimeInfo() + "test forkjoin monitor");
                    try {
                        Thread.sleep(5100);
                    } catch (InterruptedException e) {
                        throw new RuntimeException(e);
                    }
                });
            }
        }, 3000, 10 * 1000, TimeUnit.MILLISECONDS);
        return pool;
    }

    public static String threadDateTimeInfo() {
        return DateTimeFormatter.ISO_TIME.format(LocalTime.now()) + " " + Thread.currentThread().getName() + " ";
    }
}
