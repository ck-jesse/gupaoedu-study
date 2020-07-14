package com.coy.gupaoedu.study.jdk8;

import java.util.Arrays;
import java.util.function.Predicate;
import java.util.stream.Stream;

/**
 * filter(Predicate)：将 Predicate 应用于 Optional 中的内容并返回结果。当 Optional 不满足 Predicate 时返回空。如果 Optional 为空，则直接返回。
 *
 * @author chenck
 * @date 2020/7/14 10:09
 */
public class OptionalFilterTest {
    static String[] elements = {
            "Foo", "", "Bar", "Baz", "Bingo"
    };

    static Stream<String> testStream() {
        return Arrays.stream(elements);
    }

    static void test(String descr, Predicate<String> pred) {
        System.out.println(" ---( " + descr + " )---");
        for (int i = 0; i <= elements.length; i++) {
            // 一般来说，流的 filter() 会在 Predicate 返回 false 时移除流元素。
            // 而 Optional.filter() 在失败时不会删除 Optional，而是将其保留下来，并转化为空。
            System.out.println(testStream().skip(i).findFirst().filter(pred));
        }
    }

    public static void main(String[] args) {
        test("true", str -> true);
        test("false", str -> false);
        test("str != \"\"", str -> str != "");
        test("str.length() == 3", str -> str.length() == 3);
        test("startsWith(\"B\")", str -> str.startsWith("B"));
    }
}
