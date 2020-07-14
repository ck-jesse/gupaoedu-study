package com.coy.gupaoedu.study.jdk8;

import java.util.Optional;

/**
 * @author chenck
 * @date 2020/7/14 9:50
 */
public class OptionalTest2 {

    static void test(String testName, Optional<String> opt) {
        System.out.println("=== " + testName + " === ");
        System.out.println(opt.orElse("Null"));
        System.out.println();
    }

    public static void main(String[] args) {
        // 生成一个空 Optional。
        test("empty", Optional.empty());

        // 将一个非空值包装到 Optional 里。
        test("of", Optional.of("Howdy"));
        try {
            // 不能通过传递 null 到 of() 来创建 Optional 对象。最安全的方法是， 使用 ofNullable() 来优雅地处理 null。
            test("of", Optional.of(null));
        } catch (Exception e) {
            System.out.println(e);
        }

        // 针对一个可能为空的值，为空时自动生成 Optional.empty，否则将值包装在 Optional 中。
        test("ofNullable", Optional.ofNullable("Hi"));
        test("ofNullable", Optional.ofNullable(null));
    }
}
