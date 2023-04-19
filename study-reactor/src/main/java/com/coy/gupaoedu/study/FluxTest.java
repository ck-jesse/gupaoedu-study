package com.coy.gupaoedu.study;

import org.junit.jupiter.api.Test;
import reactor.core.publisher.Flux;

import java.time.Duration;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Stream;

/**
 * Hello world!
 */
public class FluxTest {

    // --------------- （1）基于各种工厂模式的静态创建方法

    /**
     * 指定序列中包含的全部元素，创建出来的 Flux 序列在发布这些元素之后会自动结束
     */
    @Test
    public void just() {
        // 要想控制台有输出，必须要调用 subscribe 方法，Flux 要是没有订阅者，数据就不会流动。
        // subscribe() 中的 lambda 表达式实际上是 java.util.Consumer，用于创建响应式流的 Subscriber。
        // 由于调用了 subscribe() 方法，数据开始流动了。
        Flux.just("Hello World!").subscribe(System.out::println);
    }

    /**
     * 如果我们已经有了一个数组、一个 Iterable 对象或 Stream 对象，那么就可以通过 Flux 提供的 fromXXX() 方法组来从这些对象中自动创建 Flux，包括 fromArray()、fromIterable() 和 fromStream() 方法。
     */
    @Test
    public void from() {
        // fromArray()
        String[] fruits = new String[]{"Apple", "Orange", "Grape", "Banana", "Strawberry"};
        Flux.fromArray(fruits).subscribe(System.out::println);

        // fromIterable()
        List<String> fruitList = new ArrayList<>();
        fruitList.add("Apple");
        fruitList.add("Orange");
        fruitList.add("Grape");
        fruitList.add("Banana");
        fruitList.add("Strawberry");
        Flux.fromIterable(fruitList).subscribe(System.out::println);

        // fromStream()
        Stream<String> fruitStream = Stream.of("Apple", "Orange", "Grape", "Banana", "Strawberry");
        Flux.fromStream(fruitStream).subscribe(System.out::println);
    }

    /**
     * 使用 Flux 作为计数器，发出一个随每个新值递增的数字。
     */
    @Test
    public void range() {
        Flux.range(1, 5).subscribe(System.out::println);
    }

    /**
     * interval() 方法可以用来生成从 0 开始递增的 Long 对象的数据序列
     */
    @Test
    public void interval() throws InterruptedException {
        // 这段代码的执行效果相当于在等待 2 秒钟之后，生成一个从 0 开始逐一递增的无界数据序列，每 200 毫秒推送一次数据。
        Flux.interval(Duration.ofSeconds(2), Duration.ofMillis(200)).subscribe(System.out::println);

        Thread.sleep(10000);
    }

    /**
     * 使用 empty()、error() 和 never() 这三个方法类创建一些特殊的数据序列
     * <p>
     * 这几个方法通常只用于调试和测试
     */
    @Test
    public void empty() {
        // empty() 创建一个只包含结束消息的空序列
        Flux.empty().subscribe(System.out::println);

        // never() 不希望所创建的序列不发出任何类似的消息通知
        Flux.never().subscribe(System.out::println);

        // error() 创建一个只包含错误消息的序列
        Flux.error(new RuntimeException("test exception")).subscribe(System.out::println);
    }


    // --------------- （2）采用编程的方式动态创建 Flux

    /**
     * generate() 方法生成 Flux 序列依赖于 Reactor 所提供的 SynchronousSink 组件
     * SynchronousSink 组件包括 next()、complete() 和 error() 这三个核心方法
     * 从 SynchronousSink 组件的命名上就能知道它是一个同步的 Sink 组件，也就是说元素的生成过程是同步执行的。这里要注意的是 next() 方法只能最多被调用一次。
     */
    @Test
    public void generate() {
        // 这里调用了一次 next() 方法，并通过 complete() 方法结束了这个数据流，如果不调用 complete() 方法，那么就会生成一个所有元素均为"生活的理想是为了理想的生活"的无界数据流。
        Flux.generate(sink -> {
            sink.next("生活的理想是为了理想的生活");
            sink.complete();
        }).subscribe(System.out::println);
    }

    /**
     * 通过 create() 方法创建 Flux 对象的方式非常灵活
     * FluxSink 除了 next()、complete() 和 error() 这三个核心方法外，还定义了背压策略，并且可以在一次调用中产生多个元素
     */
    @Test
    public void create() {
        Flux.create(sink -> {
            for (int i = 0; i < 5; i++) {
                sink.next("生活的理想是为了理想的生活" + i);
            }
            sink.complete();
        }).subscribe(System.out::println);
    }

}
