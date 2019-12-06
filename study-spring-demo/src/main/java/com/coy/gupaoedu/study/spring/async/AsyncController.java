package com.coy.gupaoedu.study.spring.async;

import com.coy.gupaoedu.study.spring.common.ApplicationContextUtil;
import lombok.extern.slf4j.Slf4j;
import org.springframework.scheduling.annotation.Async;
import org.springframework.scheduling.annotation.AsyncResult;
import org.springframework.scheduling.annotation.EnableAsync;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.concurrent.Future;
import java.util.concurrent.atomic.AtomicInteger;

/**
 * @author chenck
 * @date 2019/12/4 11:14
 */
@Slf4j
@EnableAsync
@RestController
public class AsyncController {

    AtomicInteger count = new AtomicInteger();

    /**
     * 在本类中获取bean后，再调用 @Async 方法 => 异步生效
     */
    @RequestMapping(value = "/testAsync")
    public String testAsync() {
        log.info("success" + count.getAndIncrement());
        AsyncController application = ApplicationContextUtil.getBean(AsyncController.class);
        application.asyncMethod();
        return "success";
    }

    /**
     * 直接调用本类中的 @Async 方法 => 异步不生效
     */
    @RequestMapping(value = "/testAsync2")
    public String testAsync2() {
        log.info("success" + count.getAndIncrement());
        asyncMethod();
        return "success";
    }

    /**
     * 无参无返回值方法
     * 注：@Async 是基于AOP实现，而AOP是基于动态代理实现，如果在对象内部调用该方法，因为没有经过spring容器，也就是使用的是对象本身而不是代理对象，所以会出现失效的情况。
     * 方法一定要从另一个类中调用，也就是从类的外部调用，类的内部调用是无效的。
     * 如果需要从类的内部调用，需要先获取其代理类。
     */
    @Async
    public void asyncMethod() {
        log.info("请求数=" + count.getAndIncrement());
        try {
            Thread.sleep(10);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

    /**
     * 有参无返回值方法
     */
    @Async
    public void asyncMethod1(String str) {
        log.info("请求数=" + count.getAndIncrement() + " " + str);
        throw new IllegalArgumentException("inputError");
    }

    /**
     * 有参有返回值方法
     */
    @Async
    public Future asyncMethod2(String str) {
        log.info("请求数=" + count.getAndIncrement() + " " + str);
        Future future;
        try {
            Thread.sleep(3000);
            throw new IllegalArgumentException();
        } catch (InterruptedException e) {
            future = new AsyncResult("InterruptedException error");
        } catch (IllegalArgumentException e) {
            future = new AsyncResult("i am throw IllegalArgumentException error");
        }
        return future;
    }
}
