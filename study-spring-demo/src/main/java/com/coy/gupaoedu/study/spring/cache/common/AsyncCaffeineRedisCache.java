package com.coy.gupaoedu.study.spring.cache.common;

import com.github.benmanes.caffeine.cache.AsyncCache;
import com.github.benmanes.caffeine.cache.AsyncCacheLoader;
import com.github.benmanes.caffeine.cache.AsyncLoadingCache;
import com.github.benmanes.caffeine.cache.Cache;
import org.checkerframework.checker.nullness.qual.NonNull;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.lang.Nullable;
import org.springframework.util.Assert;

import java.util.concurrent.Callable;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutionException;

/**
 * 异步 CaffeineRedisCache
 *
 * @author chenck
 * @date 2020/4/28 19:55
 */
public class AsyncCaffeineRedisCache extends AbstractCaffeineRedisCache {

    private final Logger logger = LoggerFactory.getLogger(AsyncCaffeineRedisCache.class);

    /**
     * Caffeine Cache
     */
    private final AsyncCache<Object, Object> caffeineCache;

    @Nullable
    private final AsyncCacheLoader<Object, Object> cacheLoader;

    /**
     * Create a {@link AsyncCaffeineRedisCache} instance with the specified name and the
     * given internal {@link Cache} to use.
     *
     * @param name                         the name of the cache
     * @param redisTemplate                whether to accept and convert {@code null}values for this cache
     * @param caffeineRedisCacheProperties the properties for this cache
     * @param expireTime                   the expire time
     * @param caffeineCache                the backing Caffeine Cache instance
     * @param cacheLoader                  the backing Caffeine cacheLoader instance
     */
    public AsyncCaffeineRedisCache(String name, RedisTemplate<Object, Object> redisTemplate,
                                   CaffeineRedisCacheProperties caffeineRedisCacheProperties, long expireTime,
                                   AsyncCache<Object, Object> caffeineCache, AsyncCacheLoader<Object, Object> cacheLoader) {
        super(name, redisTemplate, caffeineRedisCacheProperties, expireTime);
        Assert.notNull(caffeineCache, "AsyncCache must not be null");
        this.caffeineCache = caffeineCache;
        this.cacheLoader = cacheLoader;
    }

    @Override
    public AsyncCache<Object, Object> getNativeCache() {
        return this.caffeineCache;
    }

    /**
     * @Cacheable(sync=false) 进入此方法
     * 并发场景：未做同步控制，所以存在多个线程同时加载数据的情况，即可能存在缓存击穿的情况
     */
    @Override
    @Nullable
    public ValueWrapper get(Object key) {
        if (this.caffeineCache instanceof AsyncLoadingCache) {
            Object value = null;
            try {
                value = ((AsyncLoadingCache<Object, Object>) this.caffeineCache).get(key).get();
            } catch (InterruptedException | ExecutionException e) {
                logger.error("LoadingCache.get(key, callable) error, key=" + key, e);
            }
            logger.debug("LoadingCache.get cache, key={}, value={}", key, value);
            return toValueWrapper(value);
        }

        ValueWrapper value = super.get(key);
        logger.debug("Cache.get cache, key={}, value={}", key, value);
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
        if (this.caffeineCache instanceof AsyncLoadingCache) {
            if (null != this.cacheLoader && this.cacheLoader instanceof AsyncCustomCacheLoader) {
                // 将Callable设置到自定义CacheLoader中，以便在load()中执行具体的业务方法来加载数据
                AsyncCustomCacheLoader customCacheLoader = ((AsyncCustomCacheLoader) this.cacheLoader);
                customCacheLoader.setCaffeineRedisCache(this);
                customCacheLoader.addValueLoader(key, valueLoader);

                // 如果是refreshAfterWrite策略，则只会阻塞加载数据的线程，其他线程返回旧值（如果是异步加载，则所有线程都返回旧值）
                Object value = null;
                try {
                    value = ((AsyncLoadingCache<Object, Object>) this.caffeineCache).get(key).get();
                } catch (InterruptedException | ExecutionException e) {
                    logger.error("AsyncLoadingCache.get(key, callable) error, key=" + key, e);
                }
                logger.debug("AsyncLoadingCache.get(key, callable) cache, key={}, value={}", key, value);
                return (T) fromStoreValue(value);
            }
        }

        // 同步加载数据，仅一个线程加载数据，其他线程均阻塞
        Object value = null;
        try {
            value = this.caffeineCache.get(key, new LoadFunction(this, valueLoader)).get();
        } catch (InterruptedException | ExecutionException e) {
            logger.error("get(key, callable) error, key=" + key, e);
        }
        logger.debug("get(key, callable) cache, key={}, value={}", key, value);
        return (T) fromStoreValue(value);
    }

    @Override
    public Object lookup0(Object key) {
        try {
            return caffeineCache.getIfPresent(key).get();
        } catch (InterruptedException | ExecutionException e) {
            logger.error("lookup0 error, key=" + key, e);
        }
        return null;
    }

    @Override
    public void put0(Object key, Object value) {
        this.caffeineCache.put(key, CompletableFuture.completedFuture(value));
    }

    @Override
    public void evict0(Object key) {
        this.caffeineCache.synchronous().invalidate(key);
    }

    @Override
    public void clear0() {
        this.caffeineCache.synchronous().invalidateAll();
    }

    @Override
    public void clearLocalCache(Object key) {
        logger.info("clear local cache, name={}, key={}", this.getName(), key);
        if (key == null) {
            caffeineCache.synchronous().invalidateAll();
        } else {
            caffeineCache.synchronous().invalidate(key);
        }
    }

    @Override
    public void refresh(@NonNull Object key) {
        if (this.caffeineCache instanceof AsyncLoadingCache) {
            logger.info("refresh cache, name={}, key={}", this.getName(), key);
            AsyncLoadingCache loadingCache = (AsyncLoadingCache) caffeineCache;
            loadingCache.synchronous().refresh(key);
        }
    }

    @Override
    public void refreshAll() {
        if (this.caffeineCache instanceof AsyncLoadingCache) {
            AsyncLoadingCache loadingCache = (AsyncLoadingCache) caffeineCache;
            for (Object key : loadingCache.asMap().keySet()) {
                logger.info("refreshAll cache, name={}, key={}", this.getName(), key);
                loadingCache.synchronous().refresh(key);
            }
        }
    }

    @Override
    public void refreshAllExpireCache() {
        if (this.caffeineCache instanceof AsyncLoadingCache) {
            AsyncLoadingCache loadingCache = (AsyncLoadingCache) caffeineCache;
            for (Object key : loadingCache.asMap().keySet()) {
                logger.info("refreshAllExpireCache cache, name={}, key={}", this.getName(), key);
                // 通过LoadingCache.get(key)来刷新过期缓存
                loadingCache.synchronous().get(key);
            }
        }
    }
}
