package com.coy.gupaoedu.study.question;

/**
 * @author chenck
 * @date 2019/11/26 13:47
 */
public class Loopy {

    public static void main(String[] args) {
        final int start = Integer.MAX_VALUE - 100;
        final int end = Integer.MAX_VALUE;

        // 结果：死循环
        // 分析：
        // 当 i = 2147483647时（也就是Ingeger的最大值），i++ 执行后，i的值变为了i=-2147483648
        int count = 0;
        for (int i = start; i <= end; i++) {
            System.out.println("i=" + i + " count=" + count);
            count++;
            if (count > 120) {
                break;
            }
        }
        System.out.println(count);
    }
}
