package com.coy.gupaoedu.study.jdk8.stream;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

/**
 * @author chenck
 * @date 2019/10/26 14:41
 */
public class ListToStringTest {

    /**
     * 将集合转换为指定分隔符的字符串
     */
    public static void main(String[] args) {
        List<String> list = new ArrayList<>(Arrays.asList("Lily", "Kity", "Ben", "Hello"));

        String listStr = String.join(",", list);
        System.out.println(listStr);

        String listStr1 = list.stream().collect(Collectors.joining("/"));
        System.out.println(listStr1);

        List<String> list1 = Arrays.asList(listStr.split(","));
        System.out.println(list1);

        // 选择一个子域
        Arrays.stream(new int[]{1, 3, 5, 7, 15, 28, 37}, 3, 6)
                .forEach(value -> System.out.println(value));
    }
}
