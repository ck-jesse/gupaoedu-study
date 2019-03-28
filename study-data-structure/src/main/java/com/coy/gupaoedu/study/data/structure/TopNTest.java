package com.coy.gupaoedu.study.data.structure;

import java.util.Random;

/**
 * @author chenck
 * @date 2019/3/28 10:05
 */
public class TopNTest {

    public static void main(String[] args) {

        TopN topN = new TopN();

        // 第一组测试
        int[] arr1 = new int[]{56, 30, 71, 18, 29, 93, 44, 75, 20, 65, 68, 34};

        System.out.println("原数组：");
        topN.print(arr1);
        topN.findTopN(5, arr1);
        System.out.println("调整后数组：");
        topN.print(arr1);

        // 第二组测试
        int[] arr2 = new int[1000];
        for (int i = 0; i < arr2.length; i++) {
            arr2[i] = i + 1;
        }

        System.out.println("原数组：");
        topN.print(arr2);
        topN.findTopN(50, arr2);
        System.out.println("调整后数组：");
        topN.print(arr2);

        // 第三组测试
        Random random = new Random();
        int[] arr3 = new int[1000];
        for (int i = 0; i < arr3.length; i++) {
            arr3[i] = random.nextInt();
        }

        System.out.println("原数组：");
        topN.print(arr3);
        topN.findTopN(50, arr3);
        System.out.println("调整后数组：");
        topN.print(arr3);
    }
}
