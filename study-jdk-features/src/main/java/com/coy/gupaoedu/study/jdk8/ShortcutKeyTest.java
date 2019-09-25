package com.coy.gupaoedu.study.jdk8;

import java.util.Arrays;
import java.util.List;

/**
 * idea 快捷键 测试
 *
 * @author chenck
 * @date 2019/9/25 9:39
 */
public class ShortcutKeyTest {

    public static void main(String[] args) {
        // 30.var
        int age = 30;

        // new Object().var
        Object obj = new Object();

        // 快捷键： obj.null
        if (obj == null) {

        }
        // 快捷键： obj.notnull
        if (obj != null) {

        }
        // 快捷键： obj.nn
        if (obj != null) {

        }

        List<String> list = Arrays.asList("a", "b", "c");
        // list.for
        for (String s : list) {
            System.out.println(s);
        }
        // list.fori
        for (int i = 0; i < list.size(); i++) {
            System.out.println(list.get(i));
        }

    }
}
