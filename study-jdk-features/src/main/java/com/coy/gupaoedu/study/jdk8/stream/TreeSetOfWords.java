package com.coy.gupaoedu.study.jdk8.stream;

import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Arrays;
import java.util.Set;
import java.util.TreeSet;
import java.util.stream.Collectors;

/**
 * @author chenck
 * @date 2020/7/14 10:29
 */
public class TreeSetOfWords {

    public static void main(String[] args) throws Exception {

        Path path = Paths.get("TreeSetOfWords.java");
        // Files.lines(Path) 打开 Path 并将其转换成为行流。
        Set<String> words2 =
                Files.lines(path)
                        .flatMap(s -> Arrays.stream(s.split("\\W+")))// 下一行代码将匹配一个或多个非单词字符（\\w+）行进行分割
                        .filter(s -> !s.matches("\\d+")) // No numbers 使用 matches(\\d+) 查找并移除全数字字符串（注意,words2 是通过的）。
                        .map(String::trim)// 使用 String.trim() 去除单词两边的空白
                        .filter(s -> s.length() > 2)// 过滤所有长度小于3的单词
                        .limit(100)// 只获取100个单词
                        .collect(Collectors.toCollection(TreeSet::new));// 保存到 TreeSet 中
        System.out.println(words2);
    }
}
