package com.coy.gupaoedu.study.guava;

import com.coy.gupaoedu.study.guava.cache.User;
import com.google.common.util.concurrent.FutureCallback;
import com.google.common.util.concurrent.Futures;
import com.google.common.util.concurrent.ListenableFuture;
import com.google.common.util.concurrent.ListeningExecutorService;
import com.google.common.util.concurrent.MoreExecutors;

import java.util.concurrent.Callable;
import java.util.concurrent.Executors;

/**
 * 并发编程：
 * ListenableFuture接口并继承了JDK concurrent包下的Future 接口。
 * JDK Future通过异步的方式计算返回结果:
 * ListenableFuture可以允许你注册回调方法(callbacks)，在运算（多线程执行）完成的时候进行调用,  或者在运算（多线程执行）完成后立即执行。
 * 这样简单的改进，使得可以明显的支持更多的操作，这样的功能在JDK concurrent中的Future是不支持的。
 *
 * @author chenck
 * @date 2020/3/19 14:57
 */
public class ListenableFutureTest {

    public static void main(String[] args) {
        ListeningExecutorService service = MoreExecutors.listeningDecorator(Executors.newFixedThreadPool(10));
        ListenableFuture userFuture = service.submit(new Callable() {
            public User call() {
                //return User.init();
                throw new IllegalArgumentException("error test");
            }
        });

        Futures.addCallback(userFuture, new FutureCallback<User>() {

            /**
             * 在Future成功的时候执行，根据Future结果来判断。
             */
            public void onSuccess(User user) {
                System.out.println(user);
            }

            /**
             * 在Future失败的时候执行，根据Future结果来判断。
             */
            public void onFailure(Throwable thrown) {
                ; // escaped the explosion!
                System.out.println(thrown.getMessage());
            }
        });

    }
}
