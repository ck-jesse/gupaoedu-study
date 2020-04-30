package com.coy.gupaoedu.study.spring.cache.common;

import com.github.benmanes.caffeine.cache.Caffeine;
import com.github.benmanes.caffeine.cache.CaffeineSpec;
import com.github.benmanes.caffeine.cache.Expiry;
import org.springframework.cache.Cache;
import org.springframework.cache.CacheManager;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.lang.Nullable;
import org.springframework.util.Assert;
import org.springframework.util.ObjectUtils;

import java.util.Collection;
import java.util.Collections;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;

/**
 * 缓存管理器
 *
 * @author chenck
 * @date 2020/4/28 19:54
 */
public class CaffeineRedisCacheManager implements CacheManager {
    // 缓存Map<cacheName, Cache>
    private final ConcurrentMap<String, Cache> cacheMap = new ConcurrentHashMap<>(16);

    private boolean dynamic = true;

    //
    private Caffeine<Object, Object> cacheBuilder = Caffeine.newBuilder();

    // 自定义过期策略，用于计算缓存项的过期时间
    private Expiry<Object, Object> expiry;

    private RedisTemplate<Object, Object> redisTemplate;

    private CaffeineRedisCacheProperties caffeineRedisCacheProperties;

    public CaffeineRedisCacheManager(RedisTemplate<Object, Object> redisTemplate, CaffeineRedisCacheProperties caffeineRedisCacheProperties) {
        this.redisTemplate = redisTemplate;
        this.dynamic = caffeineRedisCacheProperties.isDynamic();
        this.caffeineRedisCacheProperties = caffeineRedisCacheProperties;
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

    /**
     * Specify the set of cache names for this CacheManager's 'static' mode.
     * <p>The number of caches and their names will be fixed after a call to this method,
     * with no creation of further cache regions at runtime.
     * <p>Calling this with a {@code null} collection argument resets the
     * mode to 'dynamic', allowing for further creation of caches again.
     */
    public void setCacheNames(@Nullable Collection<String> cacheNames) {
        if (cacheNames != null) {
            for (String name : cacheNames) {
                this.cacheMap.put(name, createCaffeineRedisCache(name));
            }
            this.dynamic = false;
        } else {
            this.dynamic = true;
        }
    }

    /**
     * Set the Caffeine to use for building each individual
     * {@link CaffeineRedisCache} instance.
     *
     * @see #createNativeCaffeineCache
     * @see com.github.benmanes.caffeine.cache.Caffeine#build()
     */
    public void setCaffeine(Caffeine<Object, Object> caffeine) {
        Assert.notNull(caffeine, "Caffeine must not be null");
        doSetCaffeine(caffeine);
    }

    /**
     * Set the {@link CaffeineSpec} to use for building each individual
     * {@link CaffeineRedisCache} instance.
     *
     * @see #createNativeCaffeineCache
     * @see com.github.benmanes.caffeine.cache.Caffeine#from(CaffeineSpec)
     */
    public void setCaffeineSpec(CaffeineSpec caffeineSpec) {
        doSetCaffeine(Caffeine.from(caffeineSpec));
    }

    /**
     * Set the Caffeine cache specification String to use for building each
     * individual {@link CaffeineRedisCache} instance. The given value needs to
     * comply with Caffeine's {@link CaffeineSpec} (see its javadoc).
     *
     * @see #createNativeCaffeineCache
     * @see com.github.benmanes.caffeine.cache.Caffeine#from(String)
     */
    public void setCacheSpecification(String cacheSpecification) {
        doSetCaffeine(Caffeine.from(cacheSpecification));
    }

    /**
     * Create a new CaffeineRedisCache instance for the specified cache name.
     *
     * @param name the name of the cache
     * @return the Spring CaffeineCache adapter (or a decorator thereof)
     */
    protected Cache createCaffeineRedisCache(String name) {
        return new CaffeineRedisCache(name, createNativeCaffeineCache(name), redisTemplate, caffeineRedisCacheProperties);
    }

    /**
     * Set the
     * @param expiry the expiry to use in calculating the expiration time of cache entries
     * @return void
     */
    public void setExpiry(Expiry<Object, Object> expiry) {
        this.expiry = expiry;
    }

    /**
     * Create a native Caffeine Cache instance for the specified cache name.
     *
     * @param name the name of the cache
     * @return the native Caffeine Cache instance
     */
    protected com.github.benmanes.caffeine.cache.Cache<Object, Object> createNativeCaffeineCache(String name) {
        if (this.expiry != null) {
            this.cacheBuilder.expireAfter(this.expiry);
        }
        return this.cacheBuilder.build();
    }

    private void doSetCaffeine(Caffeine<Object, Object> cacheBuilder) {
        if (!ObjectUtils.nullSafeEquals(this.cacheBuilder, cacheBuilder)) {
            this.cacheBuilder = cacheBuilder;
            refreshKnownCaches();
        }
    }

    /**
     * Create the known caches again with the current state of this manager.
     */
    private void refreshKnownCaches() {
        for (Map.Entry<String, Cache> entry : this.cacheMap.entrySet()) {
            entry.setValue(createCaffeineRedisCache(entry.getKey()));
        }
    }

    /**
     *
     */
    public void clearLocalCache(String cacheName, Object key) {
        Cache cache = cacheMap.get(cacheName);
        if (cache == null) {
            return;
        }

        CaffeineRedisCache redisCaffeineCache = (CaffeineRedisCache) cache;
        redisCaffeineCache.clearLocalCache(key);
    }
}
