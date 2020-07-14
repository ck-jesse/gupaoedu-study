package com.coy.gupaoedu.study.jdk8;

import java.util.Optional;
import java.util.stream.IntStream;
import java.util.stream.Stream;

/**
 * @author chenck
 * @date 2020/7/14 9:22
 */
public class OptionalTest {

    public static void main(String[] args) {
        System.out.println(Stream.<String>empty().findFirst());
        System.out.println(Stream.<String>empty().findAny());
        System.out.println(Stream.<String>empty().max(String.CASE_INSENSITIVE_ORDER));
        System.out.println(Stream.<String>empty().min(String.CASE_INSENSITIVE_ORDER));
        System.out.println(Stream.<String>empty().reduce((s1, s2) -> s1 + s2));
        System.out.println(IntStream.empty().average());
        System.out.println();

        test(Stream.of("Epithets").findFirst());
        test(Stream.<String>empty().findFirst());
    }

    public static void test(Optional<String> optString) {
        // 当你接收到 Optional 对象时，应首先调用 isPresent() 检查其中是否包含元素。如果存在，可使用 get() 获取。
        if (optString.isPresent()) {
            System.out.println(optString.get());
        } else {
            System.out.println("Nothing inside!");
        }
    }

}
