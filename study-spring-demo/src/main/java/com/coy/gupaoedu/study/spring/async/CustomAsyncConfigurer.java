package com.coy.gupaoedu.study.spring.async;

import com.coy.gupaoedu.study.spring.common.NamedThreadFactory;
import lombok.extern.slf4j.Slf4j;
import org.springframework.aop.interceptor.AsyncUncaughtExceptionHandler;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.scheduling.annotation.AsyncConfigurer;
import org.springframework.scheduling.concurrent.ThreadPoolTaskExecutor;
import org.springframework.stereotype.Component;

import java.lang.reflect.Method;
import java.util.concurrent.Executor;

/**
 * 通过实现 AsyncConfigurer 自定义异步任务线程池，包含异常处理
 * <p>
 *
 * @author chenck
 * @date 2019/12/3 15:12
 */
@Component
@Slf4j
public class CustomAsyncConfigurer implements AsyncConfigurer {

    /**
     * 核心线程数
     */
    @Value("${thread.pool.corePoolSize:10}")
    private Integer corePoolSize;

    /**
     * 最大线程数
     * 注：因该线程池主要用于异步将订单结算信息入库，此操作属IO密集型，所以最大线程数可以适当大一些；
     * 假设每个任务执行50ms，则一个线程1s可执行20个任务，100个线程1s可执行2000个任务
     */
    @Value("${thread.pool.maxPoolSize:100}")
    private Integer maxPoolSize;

    /**
     * 队列容量
     */
    @Value("${thread.pool.queueCapacity:15000}")
    private Integer queueCapacity;

    @Override
    public Executor getAsyncExecutor() {
        ThreadPoolTaskExecutor taskExecutor = new ThreadPoolTaskExecutor();
        taskExecutor.setCorePoolSize(corePoolSize);
        taskExecutor.setMaxPoolSize(maxPoolSize);
        taskExecutor.setQueueCapacity(queueCapacity);
        taskExecutor.setWaitForTasksToCompleteOnShutdown(true);
        taskExecutor.setThreadFactory(new NamedThreadFactory("checkout"));
        taskExecutor.setTaskDecorator(new MDCTaskDecorator());
        taskExecutor.initialize();
        return taskExecutor;
    }

    @Override
    public AsyncUncaughtExceptionHandler getAsyncUncaughtExceptionHandler() {
        // 针对标注 @Async 的方法内出现的异常会进入该异常处理器
        return new AsyncUncaughtExceptionHandler() {
            @Override
            public void handleUncaughtException(Throwable ex, Method method, Object... params) {
                log.error("线程池执行任务发生异常.", ex);
                for (Object param : params) {
                    log.info("Parameter value - " + param);
                }
            }
        };
    }
}
