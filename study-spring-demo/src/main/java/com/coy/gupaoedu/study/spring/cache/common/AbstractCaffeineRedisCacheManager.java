package com.coy.gupaoedu.study.spring.cache.common;

import com.github.benmanes.caffeine.cache.Caffeine;
import com.github.benmanes.caffeine.cache.RemovalListener;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.cache.Cache;
import org.springframework.data.redis.core.RedisTemplate;

import java.util.Collection;
import java.util.Collections;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.ScheduledThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

/**
 * 抽象缓存管理器
 *
 * @author chenck
 * @date 2020/5/13 19:18
 */
public abstract class AbstractCaffeineRedisCacheManager implements ExtendCacheManager {

    private final Logger logger = LoggerFactory.getLogger(AbstractCaffeineRedisCacheManager.class);

    // 缓存Map<cacheName, Cache>
    private final ConcurrentMap<String, Cache> cacheMap = new ConcurrentHashMap<>(16);

    private boolean dynamic = true;

    private Caffeine<Object, Object> defaultCacheBuilder = Caffeine.newBuilder();

    private RedisTemplate<Object, Object> redisTemplate;

    private CaffeineRedisCacheProperties caffeineRedisCacheProperties;

    private RemovalListener<Object, Object> removalListener;

    public AbstractCaffeineRedisCacheManager(RedisTemplate<Object, Object> redisTemplate,
                                             CaffeineRedisCacheProperties caffeineRedisCacheProperties) {
        this.redisTemplate = redisTemplate;
        this.dynamic = caffeineRedisCacheProperties.isDynamic();
        this.caffeineRedisCacheProperties = caffeineRedisCacheProperties;

        // 定期刷新过期的缓存
        ScheduledExecutorService scheduler = new ScheduledThreadPoolExecutor(caffeineRedisCacheProperties.getCaffeine().getRefreshPoolSize(),
                new DaemonThreadFactory("cache-refresh-"));
        scheduler.scheduleWithFixedDelay(() -> {
            for (Map.Entry<String, Cache> entry : cacheMap.entrySet()) {
                if (entry.getValue() instanceof ExtendCache) {
                    ExtendCache extendCache = (ExtendCache) entry.getValue();
                    // refresh() 只要加载成功，均会替换缓存中的前一个值
                    // 因为目的是刷新过期的缓存，所以refresh()不适用
                    // 思路：通过LoadingCache.get(key)来刷新过期缓存
                    extendCache.refreshAllExpireCache();
                }
            }
        }, 3, caffeineRedisCacheProperties.getCaffeine().getRefreshPeriod(), TimeUnit.SECONDS);

    }

    @Override
    public Cache getCache(String name) {
        Cache cache = this.cacheMap.get(name);
        if (cache == null && this.dynamic) {
            synchronized (this.cacheMap) {
                cache = this.cacheMap.get(name);
                if (cache == null) {
                    cache = createCaffeineRedisCache(name);
                    this.cacheMap.put(name, cache);
                }
            }
        }
        return cache;
    }

    @Override
    public Collection<String> getCacheNames() {
        return Collections.unmodifiableSet(this.cacheMap.keySet());
    }

    @Override
    public void setRemovalListener(RemovalListener<Object, Object> removalListener) {
        this.removalListener = removalListener;
    }

    @Override
    public void clearLocalCache(String cacheName, Object key) {
        Cache cache = cacheMap.get(cacheName);
        if (cache == null) {
            return;
        }

        AsyncCaffeineRedisCache redisCaffeineCache = (AsyncCaffeineRedisCache) cache;
        redisCaffeineCache.clearLocalCache(key);
    }

    @Override
    public boolean currentCacheInstance(String instanceId) {
        return caffeineRedisCacheProperties.getInstanceId().equals(instanceId);
    }

    /**
     * Create a new CaffeineRedisCache instance for the specified cache name.
     *
     * @param name the name of the cache
     * @return the Spring CaffeineRedisCache adapter (or a decorator thereof)
     */
    protected abstract Cache createCaffeineRedisCache(String name);


    @Override
    public ConcurrentMap<String, Cache> getCacheMap() {
        return cacheMap;
    }

    // getter ... ...

    public boolean isDynamic() {
        return dynamic;
    }

    public Caffeine<Object, Object> getDefaultCacheBuilder() {
        return defaultCacheBuilder;
    }

    public RedisTemplate<Object, Object> getRedisTemplate() {
        return redisTemplate;
    }

    public CaffeineRedisCacheProperties getCaffeineRedisCacheProperties() {
        return caffeineRedisCacheProperties;
    }

    public RemovalListener<Object, Object> getRemovalListener() {
        return removalListener;
    }
}
