package com.coy.gupaoedu.study.spring.cache.common;

import com.github.benmanes.caffeine.cache.Cache;
import com.github.benmanes.caffeine.cache.CacheLoader;
import com.github.benmanes.caffeine.cache.LoadingCache;
import org.checkerframework.checker.nullness.qual.NonNull;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.lang.Nullable;
import org.springframework.util.Assert;

import java.util.concurrent.Callable;

/**
 * @author chenck
 * @date 2020/4/28 19:55
 */
public class CaffeineRedisCache extends AbstractCaffeineRedisCache {

    private final Logger logger = LoggerFactory.getLogger(CaffeineRedisCache.class);

    /**
     * Caffeine Cache
     */
    private final Cache<Object, Object> caffeineCache;

    @Nullable
    private final CacheLoader<Object, Object> cacheLoader;

    public CaffeineRedisCache(String name, RedisTemplate<Object, Object> redisTemplate,
                              CaffeineRedisCacheProperties caffeineRedisCacheProperties, long expireTime,
                              Cache<Object, Object> caffeineCache, CacheLoader<Object, Object> cacheLoader) {
        super(name, redisTemplate, caffeineRedisCacheProperties, expireTime);
        Assert.notNull(caffeineCache, "Cache must not be null");
        this.caffeineCache = caffeineCache;
        this.cacheLoader = cacheLoader;
    }

    @Override
    public Cache<Object, Object> getNativeCache() {
        return this.caffeineCache;
    }

    /**
     * @Cacheable(sync=false) 进入此方法
     * 并发场景：未做同步控制，所以存在多个线程同时加载数据的情况，即可能存在缓存击穿的情况
     */
    @Override
    @Nullable
    public ValueWrapper get(Object key) {
        if (this.caffeineCache instanceof LoadingCache) {
            Object value = ((LoadingCache<Object, Object>) this.caffeineCache).get(key);
            logger.debug("LoadingCache.get cache, cacheName={}, key={}, value={}", this.getName(), key, value);
            return toValueWrapper(value);
        }

        ValueWrapper value = super.get(key);
        logger.debug("Cache.get cache, cacheName={}, key={}, value={}", this.getName(), key, value);
        return value;
    }

    /**
     * @Cacheable(sync=true) 进入此方法
     * 并发场景：仅一个线程加载数据，其他线程均阻塞
     * 注：借助Callable入参，可以实现不同缓存调用不同的加载数据逻辑的目的。
     */
    @Override
    @Nullable
    public <T> T get(Object key, final Callable<T> valueLoader) {
        if (this.caffeineCache instanceof LoadingCache) {
            if (null != this.cacheLoader && this.cacheLoader instanceof CustomCacheLoader) {
                // 将Callable设置到自定义CacheLoader中，以便在load()中执行具体的业务方法来加载数据
                CustomCacheLoader customCacheLoader = ((CustomCacheLoader) this.cacheLoader);
                customCacheLoader.setExtendCache(this);
                customCacheLoader.addValueLoader(key, valueLoader);

                // 如果是refreshAfterWrite策略，则只会阻塞加载数据的线程，其他线程返回旧值（如果是异步加载，则所有线程都返回旧值）
                Object value = ((LoadingCache<Object, Object>) this.caffeineCache).get(key);
                logger.debug("LoadingCache.get(key, callable) cache, cacheName={}, key={}, value={}", this.getName(), key, value);
                return (T) fromStoreValue(value);
            }
        }

        // 同步加载数据，仅一个线程加载数据，其他线程均阻塞
        Object value = this.caffeineCache.get(key, new LoadFunction(this, valueLoader));
        logger.debug("Cache.get(key, callable) cache, cacheName={}, key={}, value={}", this.getName(), key, value);
        return (T) fromStoreValue(value);
    }

    @Override
    public Object lookup0(Object key) {
        return caffeineCache.getIfPresent(key);
    }

    @Override
    public void put0(Object key, Object value) {
        caffeineCache.put(key, value);
    }

    @Override
    public void evict0(Object key) {
        caffeineCache.invalidate(key);
    }

    @Override
    public void clear0() {
        caffeineCache.invalidateAll();
    }

    @Override
    public void clearLocalCache(Object key) {
        logger.info("clear local cache, name={}, key={}", this.getName(), key);
        if (key == null) {
            caffeineCache.invalidateAll();
        } else {
            caffeineCache.invalidate(key);
        }
    }

    @Override
    public void refresh(@NonNull Object key) {
        if (this.caffeineCache instanceof LoadingCache) {
            logger.debug("refresh cache, name={}, key={}", this.getName(), key);
            ((LoadingCache) caffeineCache).refresh(key);
        }
    }

    @Override
    public void refreshAll() {
        if (this.caffeineCache instanceof LoadingCache) {
            LoadingCache loadingCache = (LoadingCache) caffeineCache;
            for (Object key : loadingCache.asMap().keySet()) {
                logger.debug("refreshAll cache, name={}, key={}", this.getName(), key);
                loadingCache.refresh(key);
            }
        }
    }

    @Override
    public void refreshExpireCache(@NonNull Object key) {
        if (this.caffeineCache instanceof LoadingCache) {
            logger.debug("refreshExpireCache cache, name={}, key={}", this.getName(), key);
            // 通过LoadingCache.get(key)来刷新过期缓存
            ((LoadingCache) caffeineCache).get(key);
        }
    }

    @Override
    public void refreshAllExpireCache() {
        if (this.caffeineCache instanceof LoadingCache) {
            LoadingCache loadingCache = (LoadingCache) caffeineCache;
            for (Object key : loadingCache.asMap().keySet()) {
                logger.debug("refreshAllExpireCache cache, name={}, key={}", this.getName(), key);
                // 通过LoadingCache.get(key)来刷新过期缓存
                loadingCache.get(key);
            }
        }
    }

}
