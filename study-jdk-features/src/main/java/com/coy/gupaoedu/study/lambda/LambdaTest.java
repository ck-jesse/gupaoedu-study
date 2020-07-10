package com.coy.gupaoedu.study.lambda;

import org.junit.Test;

import java.util.Arrays;
import java.util.List;
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
public class LambdaTest extends BaseExample {

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

    // 双冒号(::)运算符在Java 8中被用作方法引用（method reference）。它提供了一种不执行方法的方法。为此，方法引用需要由兼容的函数接口组成的目标类型上下文。
    // 静态方法引用语法：classname::methodname 例如：Person::getAge
    // 对象的实例方法引用语法：instancename::methodname 例如：System.out::println
    // 对象的超类方法引用语法： super::methodname
    // 类构造器引用语法： classname::new 例如：ArrayList::new
    // 数组构造器引用语法： typename[]::new 例如： String[]:new

    // 静态方法引用语法:ClassName::methodName 例如：Person::getAge
    @Test
    public void test11() {
        List<String> list = Arrays.asList("aaaa", "bbbb", "cccc");
        list.forEach(LambdaTest::print11);
    }

    // 对象的实例方法引用语法：instancename::methodName 例如：System.out::println
    @Test
    public void test12() {
        List<String> list = Arrays.asList("aaaa", "bbbb", "cccc");
        list.forEach(new LambdaTest()::print12);
    }

    // 对象的超类方法引用语法： super::methodname
    @Test
    public void test13() {
        List<String> list = Arrays.asList("aaaa", "bbbb", "cccc");
        list.forEach(super::print);
    }


    public static void print11(String content) {
        System.out.println(content);
    }

    public void print12(String content) {
        System.out.println(content);
    }

}

class BaseExample {
    public void print(String content) {
        System.out.println(content);
    }
}
