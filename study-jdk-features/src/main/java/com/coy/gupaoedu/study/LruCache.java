package com.coy.gupaoedu.study;

import java.util.LinkedHashMap;

/**
 * @author chenck
 * @date 2020/6/12 10:21
 */
public class LruCache<K, V> {

    int maxSize;
    int hitCount;
    int missCount;

    LinkedHashMap<K, V> map;

    public LruCache(int maxSize) {
        this.maxSize = maxSize;
        hitCount = 0;
        missCount = 0;
        /*
         * 初始化LinkedHashMap
         * 第一个参数：initialCapacity，初始大小
         * 第二个参数：loadFactor，负载因子=0.75f
         * 第三个参数：accessOrder=true，基于访问顺序；accessOrder=false，基于插入顺序
         */
        this.map = new LinkedHashMap<K, V>(0, 0.75f, true);
    }

    public final V get(K key) {
        V mapValue;
        synchronized (this) {
            // 关键点：LinkedHashMap每次get都会基于访问顺序来重整数据顺序
            mapValue = map.get(key);
            // 计算 命中次数
            if (mapValue != null) {
                hitCount++;
                return mapValue;
            }
            // 计算 丢失次数
            missCount++;
        }
        return null;
    }
}
