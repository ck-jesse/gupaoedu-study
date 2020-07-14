package com.coy.gupaoedu.study.jdk8;

import java.util.Optional;
import java.util.function.Consumer;
import java.util.stream.Stream;

/**
 * @author chenck
 * @date 2020/7/14 9:22
 */
public class OptionalTest1 {

    static void basics(Optional<String> optString) {
        if (optString.isPresent()) {
            System.out.println(optString.get());
        } else {
            System.out.println("Nothing inside!");
        }
    }

    // 当值存在时调用 Consumer，否则什么也不做。
    static void ifPresent(Optional<String> optString) {
        optString.ifPresent(System.out::println);
    }

    // 如果值存在则直接返回，否则生成 otherObject。
    static void orElse(Optional<String> optString) {
        System.out.println(optString.orElse("Nada"));
    }

    // 如果值存在则直接返回，否则使用 Supplier 函数生成一个可替代对象。
    static void orElseGet(Optional<String> optString) {
        System.out.println(
                optString.orElseGet(() -> "Generated"));
    }

    // 如果值存在直接返回，否则使用 Supplier 函数生成一个异常。
    static void orElseThrow(Optional<String> optString) {
        try {
            System.out.println(optString.orElseThrow(
                    () -> new Exception("Supplied")));
        } catch (Exception e) {
            System.out.println("Caught " + e);
        }
    }

    static void test(String testName, Consumer<Optional<String>> cos) {
        System.out.println("=== " + testName + " === ");
        cos.accept(Stream.of("Epithets").findFirst());
        cos.accept(Stream.<String>empty().findFirst());
        System.out.println();
    }

    public static void main(String[] args) {
        test("basics", OptionalTest1::basics);
        test("ifPresent", OptionalTest1::ifPresent);
        test("orElse", OptionalTest1::orElse);
        test("orElseGet", OptionalTest1::orElseGet);
        test("orElseThrow", OptionalTest1::orElseThrow);
    }

}
