package com.coy.gupaoedu.study.spring.cache.common;

import com.github.benmanes.caffeine.cache.AsyncCacheLoader;
import org.checkerframework.checker.nullness.qual.NonNull;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.util.ConcurrentReferenceHashMap;

import java.util.Map;
import java.util.concurrent.Callable;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.Executor;

/**
 * 自定义CacheLoader
 * <p>
 * 目的：主要是为了在使用refreshAfterWrite策略的特性：仅加载数据的线程阻塞，其他线程返回旧值
 * 结合sync=true，在高并发场景下
 *
 * @author chenck
 * @date 2020/5/9 14:28
 */
public class AsyncCustomCacheLoader implements AsyncCacheLoader<Object, Object> {

    private final Logger logger = LoggerFactory.getLogger(AsyncCustomCacheLoader.class);

    /**
     * <key, Callable>
     * 用户保证并发场景下对于不同的key找到对应的Callable进行数据加载
     */
    private static final Map<Object, Callable<?>> VALUE_LOADER_CACHE = new ConcurrentReferenceHashMap<>();

    private ExtendCache extendCache;

    /**
     * 设置加载数据的处理器
     * 注：在获取缓存时动态设置valueLoader，来达到实现不同缓存调用不同的加载数据逻辑的目的。
     */
    public void addValueLoader(Object key, Callable<?> valueLoader) {
        Callable<?> oldCallable = VALUE_LOADER_CACHE.get(key);
        if (null == oldCallable) {
            VALUE_LOADER_CACHE.put(key, valueLoader);
        }
    }

    public void setCaffeineRedisCache(ExtendCache extendCache) {
        this.extendCache = extendCache;
    }

    @Override
    public @NonNull CompletableFuture<Object> asyncLoad(@NonNull Object key, @NonNull Executor executor) {
        // 直接返回null，目的是使后续逻辑去执行具体的加载数据方法，然后put到缓存
        Callable<?> valueLoader = VALUE_LOADER_CACHE.get(key);
        if (null == valueLoader) {
            logger.info("[CustomCacheLoader] valueLoader is null direct return null, key={}", key);
            return CompletableFuture.completedFuture(null);
        }

        if (null == extendCache) {
            logger.info("[CustomCacheLoader] caffeineRedisCache is null direct return null, key={}", key);
            return CompletableFuture.completedFuture(null);
        }
        LoadFunction loadFunction = new LoadFunction(extendCache, valueLoader);
        return CompletableFuture.supplyAsync(() -> loadFunction.apply(key), executor);
    }
}
