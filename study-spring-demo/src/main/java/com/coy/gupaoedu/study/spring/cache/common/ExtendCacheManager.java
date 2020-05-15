package com.coy.gupaoedu.study.spring.cache.common;

import com.github.benmanes.caffeine.cache.RemovalListener;
import org.springframework.cache.Cache;
import org.springframework.cache.CacheManager;

import java.util.concurrent.ConcurrentMap;

/**
 * @author chenck
 * @date 2020/5/14 20:58
 */
public interface ExtendCacheManager extends CacheManager {

    /**
     * 设置移除监听器
     */
    void setRemovalListener(RemovalListener<Object, Object> removalListener);

    /**
     * 清理本地缓存
     */
    void clearLocalCache(String cacheName, Object key);

    /**
     * 判断是否为当前缓存实例
     */
    boolean currentCacheInstance(String instanceId);

    /**
     * 获取缓存集合
     */
    ConcurrentMap<String, Cache> getCacheMap();
}
