package com.coy.gupaoedu.study.spring.clazz;

import com.coy.gupaoedu.study.spring.cache.common.AbstractCaffeineRedisCache;
import com.coy.gupaoedu.study.spring.cache.common.CaffeineRedisCacheProperties;
import com.github.benmanes.caffeine.cache.CacheLoader;
import org.checkerframework.checker.nullness.qual.NonNull;
import org.springframework.data.redis.core.RedisTemplate;

import java.util.concurrent.Callable;

/**
 * @author chenck
 * @date 2021/5/24 19:44
 */
public class RedisCache extends AbstractCaffeineRedisCache {
    /**
     * Create a {@link AbstractCaffeineRedisCache} instance with the specified name and the
     * given internal {@link Cache} to use.
     *
     * @param name                         the name of the cache
     * @param redisTemplate                whether to accept and convert {@code null}values for this cache
     * @param caffeineRedisCacheProperties the properties for this cache
     * @param expireTime
     * @param cacheLoader
     */
    public RedisCache(String name, RedisTemplate<Object, Object> redisTemplate, CaffeineRedisCacheProperties caffeineRedisCacheProperties, long expireTime, CacheLoader<Object, Object> cacheLoader) {
        super(name, redisTemplate, caffeineRedisCacheProperties, expireTime, cacheLoader);
    }

    @Override
    public Object get0(Object key) {
        return null;
    }

    @Override
    public Object get0(Object key, Callable<?> valueLoader) {
        return null;
    }

    @Override
    public Object lookup0(Object key) {
        return null;
    }

    @Override
    public void put0(Object key, Object value) {

    }

    @Override
    public void evict0(Object key) {

    }

    @Override
    public void clear0() {

    }

    @Override
    public boolean isLoadingCache() {
        return false;
    }

    @Override
    public void clearLocalCache(Object key) {

    }

    @Override
    public void refresh(@NonNull Object key) {

    }

    @Override
    public void refreshAll() {

    }

    @Override
    public void refreshExpireCache(@NonNull Object key) {

    }

    @Override
    public void refreshAllExpireCache() {

    }

    @Override
    public Object getNativeCache() {
        return null;
    }
}
