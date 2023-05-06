package com.coy.gupaoedu.study.juc.forkjoin;

import java.util.concurrent.ForkJoinPool;
import java.util.function.Function;

/**
 * Java 8中的默认“paralellStream（）”使用公共ForkJoinPool，如果提交任务时公共池线程耗尽，会导致任务延迟执行。
 * <p>
 * CPU密集型：如果在ForkJoinPool中填充的任务，执行时间足够短，且CPU的可用能力足够，那么将不会出现上述延迟的问题。（大多数的使用场景）
 * I/O密集型：如果在ForkJoinPool中填充的任务，执行时间足够长，且是不受CPU限制的I/O任务，那么任务将延迟执行，并出现瓶颈。
 * 因此，如果我们有一个I/O任务，我们应该简单地允许ForkJoinPool在ManagedBlocker中管理它。
 * <p>
 * 对I/O阻塞型任务提供一个ManagedBlocker让线程池知道当前任务即将阻塞，因此需要创建新的补偿工作线程来执行新的提交任务.
 * <p>
 * ForkJoinPool 最适合的是计算密集型的任务，如果存在 I/O，线程间同步，sleep() 等会造成线程长时间阻塞的情况时，最好配合使用 ManagedBlocker。
 * <p>
 * 问题：如果不限制新创建的线程数量，可能导致oom，如何控制ForkJoinPool中新创建的最大备用线程数？
 * 分析：ForkJoinPool.common.commonMaxSpares 表示 tryCompensate 中备用线程创建的限制，默认为256
 * 缺陷：只能针对commonPool进行限制，并且tryCompensate方法不一定能会命中该限制，若未命中该限制，则可能无限制的创建补偿线程来避免阻塞，最终可能出现oom
 * 注意：ManagedBlocker将最大正在运行的线程数限制为32767.尝试创建大于最大数目的池导致IllegalArgumentException，只有当池被关闭或内部资源耗尽时，此实现才会拒绝提交的任务（即通过抛出RejectedExecutionException ）。
 * 方案：在管理阻塞时，通过自定义 {@LimitedThreadForkJoinWorkerThreadFactory} 来限制最大可创建的线程数，避免无限制的创建线程
 *
 * @author chenck
 * @date 2023/5/5 18:30
 */
public class MyManagedBlocker implements ForkJoinPool.ManagedBlocker {
    private Function function;
    private Object key;
    private Object result;
    private boolean done = false;

    public MyManagedBlocker(Object key, Function function) {
        this.key = key;
        this.function = function;
    }


    @Override
    public boolean block() throws InterruptedException {
        result = function.apply(key);
        done = true;
        return false;
    }

    @Override
    public boolean isReleasable() {
        return done;
    }

    public Object getResult() {
        return result;
    }

}
