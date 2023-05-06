package com.coy.gupaoedu.study.juc.forkjoin;

import java.util.concurrent.ForkJoinPool;
import java.util.function.Function;

/**
 * Java 8中的默认“paralellStream（）”使用公共ForkJoinPool，如果提交任务时公共池线程耗尽，这可能是一个延迟问题。
 * <p>
 * CPU密集型：如果在ForkJoinPool中填充的任务，执行时间足够短，且CPU的可用能力足够，那么将不会出现上述延迟的问题。（大多数的使用场景）
 * I/O密集型：如果在ForkJoinPool中填充的任务，执行时间足够长，且是不受CPU限制的I/O任务，那么将出现延迟，并出现瓶颈。
 * 因此，如果我们有一个I/O任务，我们应该简单地允许ForkJoinPool在ManagedBlocker中管理它。
 * <p>
 * 对I/O阻塞型任务提供一个ManagedBlocker让线程池知道当前任务即将阻塞，因此需要创建新的补偿工作线程来执行新的提交任务.
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
