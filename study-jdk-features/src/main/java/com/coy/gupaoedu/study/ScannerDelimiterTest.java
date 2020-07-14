package com.coy.gupaoedu.study;

import java.util.Scanner;

/**
 * Scanner 分隔符
 *
 * @author chenck
 * @date 2020/7/14 16:30
 */
public class ScannerDelimiterTest {

    public static void main(String[] args) {
        // 默认情况下，Scanner 根据空白字符对输入进行分词，但是你可以用正则表达式指定自己所需的分隔符：
        Scanner scanner = new Scanner("12, 42, 78, 99, 42");
        scanner.useDelimiter("\\s*,\\s*");
        while (scanner.hasNextInt()) {
            System.out.println(scanner.nextInt());
        }
    }
}
