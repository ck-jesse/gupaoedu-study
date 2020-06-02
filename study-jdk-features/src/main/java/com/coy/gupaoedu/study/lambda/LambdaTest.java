package com.coy.gupaoedu.study.lambda;

import org.junit.Test;

import java.util.function.BinaryOperator;
import java.util.function.Consumer;

/**
 * Lambda表达式在Java中引入了一个新的语法元素和操作符->，它将Lambda分成两个部分：
 * <p>
 * 左侧：指定Lambda表达式需要的所有参数；
 * 右侧：指定了Lambda体，即Lambda表达式要执行的功能。
 *
 * @author chenck
 * @date 2020/6/2 16:32
 */
public class LambdaTest {

    // 语法格式一：无参，无返回值，Lambda体只需要一条语句
    @Test
    public void test1() {
        Runnable runnable = () -> System.out.println("Hello World");
        runnable.run();
    }

    // 语法格式二：Lambda有一个参数，且无返回值
    @Test
    public void test2() {
        Consumer<String> consumer = (x) -> System.out.println(x);
        consumer.accept("Hello World");
    }

    // 语法格式三：Lambda有一个参数，参数的小括号可以省略
    @Test
    public void test3() {
        Consumer<String> com = x -> System.out.println(x);
        com.accept("Hello World");
    }

    // 语法格式四：Lambda有2个参数，并且有返回值，多条语句时必须加上{}
    @Test
    public void test4() {
        BinaryOperator<Integer> bi = (x, y) -> {
            System.out.println("加法");
            return x + y;
        };
        System.out.println(bi.apply(23, 45));
    }

    // 语法格式五：当Lambda体只有一条语句时，return与大括号可以省略
    // 语法格式六：Lambda的参数列表的类型可以不写，JVM编辑器可以通过上下文进行”类型推断“
    @Test
    public void test5() {
        BinaryOperator<Integer> bi = (x, y) -> x + y;
        System.out.println(bi.apply(20, 30));
    }

}
