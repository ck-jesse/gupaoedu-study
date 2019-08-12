package com.coy.gupaoedu.study.io.nio;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

/**
 * @author chenck
 * @date 2019/8/12 16:07
 */
public class ComparatorTest {

    public static void main(String[] args) {
        List<String> list = new ArrayList<>();
        list.add("8M");
        list.add("12M");
        list.add("24M");

        System.out.println(list);
        // 降序
        Collections.sort(list, new Comparator<String>() {
            @Override
            public int compare(String o1, String o2) {
                Integer code1 = Integer.valueOf(o1.replaceAll("M", ""));
                Integer code2 = Integer.valueOf(o2.replaceAll("M", ""));
                System.out.println(code1 + "  " + code2 + " " + code2.compareTo(code1));
                // return -1 表示降序
                return code2.compareTo(code1);
            }
        });
        System.out.println(list);

        System.out.println("12M".compareTo("24M"));
        System.out.println("12M".compareTo("12M"));
        System.out.println("24M".compareTo("12M"));
    }
}
