package com.coy.gupaoedu.study.jdk8.functional;

/**
 * 函数式接口（Functional Interface） ：是只包含一个抽象方法声明的接口。
 * 如：java.lang.Runnable
 * 标记接口（Marker Interface） ：是一种没有方法或属性声明的接口，简单地说，marker 接口是空接口。
 * 如：java.io.Serializable
 *
 * 1、函数式接口用途：
 * 主要用在Lambda表达式和方法引用上
 * 2、关于注解@FunctionalInterface：
 * Java 8为函数式接口引入了一个新注解@FunctionalInterface，主要用于编译级错误检查，加上该注解，当你写的接口不符合函数式接口定义的时候，编译器会报错。
 *
 * @author chenck
 * @date 2019/9/24 10:18
 */
@FunctionalInterface
public interface GreetingService {

    /**
     * 函数式接口里只能有一个抽象方法
     */
    void sayMessage(String message);

    /**
     * 函数式接口里允许定义java.lang.Object里的public方法
     * 这些方法对于函数式接口来说，不被当成是抽象方法（虽然它们是抽象方法）；
     * 因为任何一个函数式接口的实现，默认都继承了Object类，包含了来自java.lang.Object里对这些抽象方法的实现；
     */
    @Override
    boolean equals(Object obj);

    /**
     * 函数式接口里允许定义默认方法
     */
    default void doSomeMoreWork()
    {
        // Method body
    }

    /**
     * 函数式接口里允许定义静态方法
     * 因为静态方法不能是抽象方法，是一个已经实现了的方法，所以是符合函数式接口的定义的
     */
    static void printHello(){
        System.out.println("Hello");
    }

}
