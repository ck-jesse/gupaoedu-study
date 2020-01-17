package com.coy.gupaoedu.study.demo;

import cn.hutool.cache.CacheUtil;
import cn.hutool.cache.impl.LRUCache;
import cn.hutool.core.date.DateUnit;

/**
 * @author chenck
 * @date 2020/1/17 13:59
 */
public class LRUCacheTest {

    public static void main(String[] args) {
        LRUCache<String, String> lruCache = CacheUtil.newLRUCache(3);
        lruCache.put("key1", "value1", DateUnit.SECOND.getMillis() * 3);
        lruCache.put("key2", "value2", DateUnit.SECOND.getMillis() * 3);
        lruCache.put("key3", "value3", DateUnit.SECOND.getMillis() * 3);
        lruCache.get("key1");//使用时间推近
        lruCache.put("key4", "value4", DateUnit.SECOND.getMillis() * 3);
        //由于缓存容量只有3，当加入第四个元素的时候，根据LRU规则，最少使用的将被移除（2被移除）
        String value2 = lruCache.get("key");//null
        System.out.println(value2);


    }
}
