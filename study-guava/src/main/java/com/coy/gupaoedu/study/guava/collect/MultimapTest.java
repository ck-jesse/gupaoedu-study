package com.coy.gupaoedu.study.guava.collect;

import com.google.common.collect.ArrayListMultimap;
import com.google.common.collect.Multimap;

import java.util.ArrayList;

/**
 * @author chenck
 * @date 2020/3/18 19:29
 */
public class MultimapTest {

    public static void main(String[] args) {
        Multimap<String, String> multimap = ArrayListMultimap.create();
        multimap.put("zhangsanfeng", "man");
        multimap.put("zhangsanfeng", "yes");
        multimap.put("lucy", "woman");

        ArrayList<String> list = (ArrayList<String>) multimap.get("zhangsanfeng");
        System.out.println(list);
    }
}
