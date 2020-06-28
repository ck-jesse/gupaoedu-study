package com.coy.gupaoedu.study.juc;

import java.util.concurrent.ConcurrentHashMap;

/**
 * @author chenck
 * @date 2020/6/28 11:39
 */
public class ConcurrentHashMapTest {

    public static void main(String[] args) {
        ConcurrentHashMap chm = new ConcurrentHashMap();
        System.out.println("size=" + chm.size());
        chm.put("copy", "balance");
        System.out.println("size=" + chm.size());
        System.out.println(chm.get("copy"));

        System.out.println(Integer.numberOfLeadingZeros(10));
        System.out.println(Integer.numberOfLeadingZeros(16) | (1 << (16 - 1)));
    }
}
