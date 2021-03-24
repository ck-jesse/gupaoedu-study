package com.coy.gupaoedu.study.jvm.oom;

import org.springframework.util.ConcurrentReferenceHashMap;

import java.util.Map;

/**
 * @author chenck
 * @date 2020/10/5 19:20
 */
public class ConcurrentReferenceHashMapTest {

    /**
     * -Xmx5M -Xms5M -verbose:gc -XX:+PrintGCDetails -XX:+PrintGCDateStamps
     * 频繁的GC
     */
    public static void main(String[] args) {
        Map<String, String> valueLoaderCache = new ConcurrentReferenceHashMap<>();
        for (int i = 0; i < 1000000; i++) {
            valueLoaderCache.put("key" + i, "valuevvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvv" + i);
        }
        System.out.println(valueLoaderCache.size());
    }
}
