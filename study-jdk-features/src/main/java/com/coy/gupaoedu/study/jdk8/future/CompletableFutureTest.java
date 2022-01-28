package com.coy.gupaoedu.study.jdk8.future;

import org.junit.Test;

import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.TimeUnit;

/**
 * 参考文章：https://mp.weixin.qq.com/s/wKzEW2SSiRkemftCgN6ckg
 * <p>
 * CompletionStage
 *
 * @author chenck
 * @date 2022/1/28 17:44
 */
public class CompletableFutureTest {

    // 串行关系（下一步）
    // thenRun
    // thenApply
    // thenAccept

    // 聚合 And 关系（And）
    // combine...with...
    // both...and...
    // thenCombine
    // thenAcceptBoth
    // runAfterBoth

    // 聚合 Or 关系（Or）
    // Either...or...
    // applyToEither
    // acceptEither
    // runAfterEither

    // 异常处理
    // exceptionally
    // whenComplete
    // handle

    // 带有 Sync 的方法是单独起一个线程来执行，但是我们并没有创建线程，这是怎么实现的呢？
    // 细心的朋友如果仔细看每个变种函数的第三个方法也许会发现里面都有一个 Executor 类型的参数，用于指定线程池，
    // 因为实际业务中我们是严谨手动创建线程的，这在 我会手动创建线程，
    // 为什么要使用线程池?文章中明确说明过；如果没有指定线程池，那自然就会有一个默认的线程池，也就是 ForkJoinPool

    @Test
    public void demo1() throws ExecutionException, InterruptedException {
        CompletableFuture<String> future = new CompletableFuture();
        String result = future.get();// 这里调用 get() 将会永久性的堵塞

        // 1）调用complete()方法手动返回指定结果
        // 2）所有等待这个 Future 的 client 都会返回手动结束的指定结果
        future.complete("success");
    }

    // 使用 runAsync 进行异步计算(无返回结果)
    @Test
    public void demo2() throws ExecutionException, InterruptedException {
        CompletableFuture<Void> future = CompletableFuture.runAsync(() -> {
            try {
                TimeUnit.SECONDS.sleep(3);
            } catch (InterruptedException e) {
                throw new IllegalStateException(e);
            }
            System.out.println("运行在一个单独的线程当中");
        });

        // get() 方法在Future 计算完成之前会一直处在 blocking 状态下，
        // 对于真正的异步处理，我们希望的是可以通过传入回调函数，在Future 结束时自动调用该回调函数，这样，我们就不用等待结果
        future.get();
    }

    // 使用 supplyAsync 进行异步计算(有返回结果)
    @Test
    public void demo3() throws ExecutionException, InterruptedException {
        CompletableFuture<String> future = CompletableFuture.supplyAsync(() -> {
            try {
                TimeUnit.SECONDS.sleep(3);
            } catch (InterruptedException e) {
                throw new IllegalStateException(e);
            }
            System.out.println("运行在一个单独的线程当中");
            return "我有返回值";
        });

        System.out.println(future.get());

    }

    // 使用 supplyAsync 进行异步计算(有返回结果)
    @Test
    public void demo4() throws ExecutionException, InterruptedException {
        CompletableFuture<String> comboFuture = CompletableFuture.supplyAsync(() -> {
            // 前序操作
            try {
                TimeUnit.SECONDS.sleep(3);
            } catch (InterruptedException e) {
                throw new IllegalStateException(e);
            }
            System.out.println("赞");
            return "赞";
        }).thenApply(first -> {
            // 后续操作
            System.out.println("在看");
            return first + ", 在看";
        }).thenApply(second -> second + ", 转发");
        // 串行的后续操作并不一定会和前序操作使用同一个线程

        System.out.println("三连有没有？");
        System.out.println(comboFuture.get());
    }


    // 使用 supplyAsync 进行异步计算(有返回结果)
    @Test
    public void demo5() throws ExecutionException, InterruptedException {
        // 模拟远端API调用，这里只返回了一个构造的对象
        final CompletableFuture<Void> voidCompletableFuture = CompletableFuture.supplyAsync(
                () -> "颈椎/腰椎治疗仪"
        ).thenAccept(product -> {
            System.out.println("获取到远程API产品名称 " + product);
        });
        voidCompletableFuture.get();

    }

    @Test
    public void demo6() throws ExecutionException, InterruptedException {
        CompletableFuture<String> stringCompletableFuture = CompletableFuture.supplyAsync(() -> {
            System.out.println("前序操作");
            return "前需操作结果";
        }).thenApplyAsync(result -> {
            System.out.println("后续操作");
            return "后续操作结果";
        });
    }


    @Test
    public void demo7() throws ExecutionException, InterruptedException {
        CompletableFuture<String> getUsersDetail = CompletableFuture.supplyAsync(() -> "7");
        CompletableFuture<Integer> getCreditRating = CompletableFuture.supplyAsync(() -> 7);

        // 想“拍平” 返回结果，thenCompose 方法就派上用场
        CompletableFuture<Integer> result = getUsersDetail.thenCompose(s -> getCreditRating);
        System.out.println(result.get());
    }


    @Test
    public void demo8() throws ExecutionException, InterruptedException {
        CompletableFuture<Double> weightFuture = CompletableFuture.supplyAsync(() -> 65.0);
        CompletableFuture<Double> heightFuture = CompletableFuture.supplyAsync(() -> 183.8);

        // 如果要聚合两个独立 Future 的结果
        CompletableFuture<Double> combinedFuture = weightFuture
                .thenCombine(heightFuture, (weight, height) -> {
                    Double heightInMeter = height / 100;
                    return weight / (heightInMeter * heightInMeter);
                });

        System.out.println("身体BMI指标 - " + combinedFuture.get());
    }


    // exceptionally 异常处理
    @Test
    public void demo9() throws ExecutionException, InterruptedException {
        Integer age = -1;

        // exceptionally 就相当于 catch，出现异常，将会跳过 thenApply 的后续操作，直接捕获异常，进行异常处理
        CompletableFuture<String> maturityFuture = CompletableFuture.supplyAsync(() -> {
            if (age < 0) {
                throw new IllegalArgumentException("何方神圣？");
            }
            if (age > 18) {
                return "大家都是成年人";
            } else {
                return "未成年禁止入内";
            }
        }).thenApply((str) -> {
            System.out.println("游戏开始");
            return str;
        }).exceptionally(ex -> {
            System.out.println("必有蹊跷，来者" + ex.getMessage());
            return "Unknown!";
        });

        System.out.println(maturityFuture.get());
    }


    // handle 异常处理
    @Test
    public void demo10() throws ExecutionException, InterruptedException {
        Integer age = -1;

        // 良好的习惯是使用 try/finally 范式，handle 就可以起到 finally 的作用，对上述程序做一个小小的更改， handle 接受两个参数，一个是正常返回值，一个是异常
        CompletableFuture<String> maturityFuture = CompletableFuture.supplyAsync(() -> {
            if (age < 0) {
                throw new IllegalArgumentException("何方神圣？");
            }
            if (age > 18) {
                return "大家都是成年人";
            } else {
                return "未成年禁止入内";
            }
        }).thenApply((str) -> {
            System.out.println("游戏开始");
            return str;
        }).handle((res, ex) -> {
            if (ex != null) {
                System.out.println("必有蹊跷，来者" + ex.getMessage());
                return "Unknown!";
            }
            return res;
        });

        System.out.println(maturityFuture.get());
    }
}
