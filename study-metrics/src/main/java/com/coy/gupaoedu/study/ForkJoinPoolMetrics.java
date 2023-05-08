package com.coy.gupaoedu.study;

import io.micrometer.core.instrument.FunctionCounter;
import io.micrometer.core.instrument.Gauge;
import io.micrometer.core.instrument.MeterRegistry;
import io.micrometer.core.instrument.Tag;
import io.micrometer.core.instrument.Tags;
import io.micrometer.core.instrument.binder.MeterBinder;
import io.micrometer.core.instrument.internal.TimedExecutor;
import io.micrometer.core.instrument.internal.TimedExecutorService;
import io.micrometer.core.lang.Nullable;

import java.util.concurrent.Executor;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.ForkJoinPool;

import static java.util.Arrays.asList;

/**
 * @author chenck
 * @date 2023/5/8 17:34
 */
public class ForkJoinPoolMetrics implements MeterBinder {
    @Nullable
    private final ExecutorService executorService;

    private final Iterable<Tag> tags;

    public ForkJoinPoolMetrics(@Nullable ExecutorService executorService, String executorServiceName, Iterable<Tag> tags) {
        this.executorService = executorService;
        this.tags = Tags.concat(tags, "name", executorServiceName);
    }

    /**
     * Record metrics on the use of an {@link Executor}.
     *
     * @param registry     The registry to bind metrics to.
     * @param executor     The executor to instrument.
     * @param executorName Will be used to tag metrics with "name".
     * @param tags         Tags to apply to all recorded metrics.
     * @return The instrumented executor, proxied.
     */
    public static Executor monitor(MeterRegistry registry, Executor executor, String executorName, Iterable<Tag> tags) {
        if (executor instanceof ExecutorService) {
            return monitor(registry, (ExecutorService) executor, executorName, tags);
        }
        return new TimedExecutor(registry, executor, executorName, tags);
    }

    /**
     * Record metrics on the use of an {@link Executor}.
     *
     * @param registry     The registry to bind metrics to.
     * @param executor     The executor to instrument.
     * @param executorName Will be used to tag metrics with "name".
     * @param tags         Tags to apply to all recorded metrics.
     * @return The instrumented executor, proxied.
     */
    public static Executor monitor(MeterRegistry registry, Executor executor, String executorName, Tag... tags) {
        return monitor(registry, executor, executorName, asList(tags));
    }

    /**
     * Record metrics on the use of an {@link ExecutorService}.
     *
     * @param registry            The registry to bind metrics to.
     * @param executor            The executor to instrument.
     * @param executorServiceName Will be used to tag metrics with "name".
     * @param tags                Tags to apply to all recorded metrics.
     * @return The instrumented executor, proxied.
     */
    public static ExecutorService monitor(MeterRegistry registry, ExecutorService executor, String executorServiceName, Iterable<Tag> tags) {
        new ForkJoinPoolMetrics(executor, executorServiceName, tags).bindTo(registry);
        return new TimedExecutorService(registry, executor, executorServiceName, tags);
    }

    /**
     * Record metrics on the use of an {@link ExecutorService}.
     *
     * @param registry            The registry to bind metrics to.
     * @param executor            The executor to instrument.
     * @param executorServiceName Will be used to tag metrics with "name".
     * @param tags                Tags to apply to all recorded metrics.
     * @return The instrumented executor, proxied.
     */
    public static ExecutorService monitor(MeterRegistry registry, ExecutorService executor, String executorServiceName, Tag... tags) {
        return monitor(registry, executor, executorServiceName, asList(tags));
    }

    @Override
    public void bindTo(MeterRegistry registry) {
        if (executorService == null) {
            return;
        }

        if (executorService instanceof ForkJoinPool) {
            monitor(registry, (ForkJoinPool) executorService);
        }
    }

    private void monitor(MeterRegistry registry, ForkJoinPool fj) {
        FunctionCounter.builder("forkjoinpool.stealCount", fj, ForkJoinPool::getStealCount)
                .tags(tags)
                .description("Estimate of the total number of tasks stolen from " +
                        "one thread's work queue by another. The reported value " +
                        "underestimates the actual total number of steals when the pool " +
                        "is not quiescent")
                .register(registry);

        Gauge.builder("forkjoinpool.queuedTaskCount", fj, ForkJoinPool::getQueuedTaskCount)
                .tags(tags)
                .description("An estimate of the total number of tasks currently held in queues by worker threads (but not including tasks submitted to the pool that have not begun executing)")
                .register(registry);

        Gauge.builder("forkjoinpool.activeThreadCount", fj, ForkJoinPool::getActiveThreadCount)
                .tags(tags)
                .description("An estimate of the number of threads that are currently stealing or executing tasks")
                .register(registry);

        Gauge.builder("forkjoinpool.runningThreadCount", fj, ForkJoinPool::getRunningThreadCount)
                .tags(tags)
                .description("An estimate of the number of worker threads that are not blocked waiting to join tasks or for other managed synchronization threads")
                .register(registry);

        Gauge.builder("forkjoinpool.queuedSubmissionCount", fj, ForkJoinPool::getQueuedSubmissionCount)
                .tags(tags)
                .description("An estimate of the total number of tasks submitted to this pool that have not yet begun executing")
                .register(registry);

        Gauge.builder("forkjoinpool.poolSize", fj, ForkJoinPool::getPoolSize)
                .tags(tags)
                .description("Returns the number of worker threads that have started but not yet terminated")
                .register(registry);

    }
}
