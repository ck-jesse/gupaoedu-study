package com.coy.gupaoedu.study.jdk8;

import java.util.Arrays;
import java.util.function.Function;
import java.util.stream.Stream;

/**
 * @author chenck
 * @date 2020/7/14 10:15
 */
public class OptionalMapTest {
    static String[] elements = {"12", "", "23", "45"};

    static Stream<String> testStream() {
        return Arrays.stream(elements);
    }

    static void test(String descr, Function<String, String> func) {
        System.out.println(" ---( " + descr + " )---");
        for (int i = 0; i <= elements.length; i++) {
            // 在 Optional 不为空时才应用映射函数，并将 Optional 的内容提取到映射函数。
            // 映射函数的返回结果会自动包装成为 Optional。Optional.empty 会被直接跳过。
            System.out.println(testStream().skip(i)
                    .findFirst() // Produces an Optional
                    .map(func));
        }
    }

    public static void main(String[] args) {
        // If Optional is not empty, map() first extracts
        // the contents which it then passes
        // to the function:
        test("Add brackets", s -> "[" + s + "]");
        test("Increment", s -> {
            try {
                return Integer.parseInt(s) + 1 + "";
            } catch (NumberFormatException e) {
                return s;
            }
        });
        test("Replace", s -> s.replace("2", "9"));
        test("Take last digit", s -> s.length() > 0 ?
                s.charAt(s.length() - 1) + "" : s);
    }
    // After the function is finished, map() wraps the
    // result in an Optional before returning it:
}
