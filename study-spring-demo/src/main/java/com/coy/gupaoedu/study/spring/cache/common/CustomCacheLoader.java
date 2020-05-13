package com.coy.gupaoedu.study.spring.cache.common;

import com.github.benmanes.caffeine.cache.CacheLoader;
import org.checkerframework.checker.nullness.qual.NonNull;
import org.checkerframework.checker.nullness.qual.Nullable;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.cache.Cache;
import org.springframework.util.ConcurrentReferenceHashMap;

import java.util.Map;
import java.util.concurrent.Callable;

/**
 * 自定义CacheLoader
 * <p>
 * 目的：主要是为了在使用refreshAfterWrite策略的特性：仅加载数据的线程阻塞，其他线程返回旧值
 * 结合sync=true，在高并发场景下
 *
 * @author chenck
 * @date 2020/5/9 14:28
 */
public class CustomCacheLoader implements CacheLoader<Object, Object> {

    private final Logger logger = LoggerFactory.getLogger(CustomCacheLoader.class);

    /**
     * <key, Callable>
     * 用户保证并发场景下对于不同的key找到对应的Callable进行数据加载
     */
    private static final Map<Object, Callable> VALUE_LOADER_CACHE = new ConcurrentReferenceHashMap<>();

    private CaffeineRedisCache caffeineRedisCache;

    @Nullable
    @Override
    public Object load(@NonNull Object key) throws Exception {

        // 直接返回null，目的是使后续逻辑去执行具体的加载数据方法，然后put到缓存
        Callable<?> valueLoader = VALUE_LOADER_CACHE.get(key);
        if (null == valueLoader) {
            logger.info("[CustomCacheLoader] valueLoader is null direct return, key={}, value=null", key);
            return null;
        }

        if (null == caffeineRedisCache) {
            logger.info("[CustomCacheLoader] caffeineRedisCache is null direct return, key={}, value=null", key);
            return null;
        }

        // 在获取缓存时动态设置valueLoader，来达到实现不同缓存调用不同的加载数据逻辑的目的。
        try {
            logger.debug("[CustomCacheLoader] load cache, key={}", key);
            // 走到此处，表明已经从本地缓存中没有获取到数据，所以先从redis中获取数据
            Object value = caffeineRedisCache.getRedisValue(key);

            if (value != null) {
                logger.info("[CustomCacheLoader] get cache from redis, key={}, value={}", key, value);
                // 从redis中获取到数据后不需要显示设置到本地缓存，利用Caffeine本身的机制进行设置
                return value;
            }

            // 执行业务方法获取数据
            value = valueLoader.call();
            logger.info("[CustomCacheLoader] load data from method, key={}, value={}", key, value);

            caffeineRedisCache.setRedisValue(key, value);

            caffeineRedisCache.cacheChangePush(new CacheMessage(caffeineRedisCache.getInstanceId(), caffeineRedisCache.getName(), key));

            return value;
        } catch (Exception ex) {
            throw new Cache.ValueRetrievalException(key, valueLoader, ex);
        }
    }

    /**
     * 注意：reload() 默认调用 load() 来同步执行
     * <p>
     * 此处扩展 reload()，将 load()中的逻辑 交给线程池来异步执行，也就是不会走到 load() 中了
     * 结论：所有的用户请求线程均返回旧的缓存值，这样就不会有用户线程被阻塞了
     * public ListenableFuture<String> reload(String key, String oldValue) throws Exception {
     * // 默认实现：reload() 直接调用了 load(key)
     * // return Futures.immediateFuture(load(key));
     * // 自定义扩展实现：将 load()中的逻辑抽取出来 交给线程池来异步执行
     * }
     */

    /**
     * 设置加载数据的处理器
     * 注：在获取缓存时动态设置valueLoader，来达到实现不同缓存调用不同的加载数据逻辑的目的。
     */
    public void addValueLoader(Object key, Callable valueLoader) {
        Callable oldCallable = VALUE_LOADER_CACHE.get(key);
        if (null == oldCallable) {
            VALUE_LOADER_CACHE.put(key, valueLoader);
        }
    }

    public void setCaffeineRedisCache(CaffeineRedisCache caffeineRedisCache) {
        this.caffeineRedisCache = caffeineRedisCache;
    }
}
