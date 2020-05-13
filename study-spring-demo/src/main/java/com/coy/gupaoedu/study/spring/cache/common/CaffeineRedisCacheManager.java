package com.coy.gupaoedu.study.spring.cache.common;

import com.github.benmanes.caffeine.cache.CacheLoader;
import com.github.benmanes.caffeine.cache.Caffeine;
import com.github.benmanes.caffeine.cache.RemovalListener;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.cache.Cache;
import org.springframework.cache.CacheManager;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.lang.Nullable;
import org.springframework.util.StringUtils;

import java.util.Collection;
import java.util.Collections;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;

/**
 * 缓存管理器
 *
 * @author chenck
 * @date 2020/4/28 19:54
 */
public class CaffeineRedisCacheManager implements CacheManager {

    private final Logger logger = LoggerFactory.getLogger(CaffeineRedisCacheManager.class);

    // 缓存Map<cacheName, Cache>
    private final ConcurrentMap<String, Cache> cacheMap = new ConcurrentHashMap<>(16);

    private boolean dynamic = true;

    private Caffeine<Object, Object> defaultCacheBuilder = Caffeine.newBuilder();

    private RedisTemplate<Object, Object> redisTemplate;

    private CaffeineRedisCacheProperties caffeineRedisCacheProperties;

    private CaffeineRedisCacheProperties.Caffeine caffeine;

    private CaffeineRedisCacheProperties.Redis redis;

    private RemovalListener<Object, Object> removalListener;

    public CaffeineRedisCacheManager(RedisTemplate<Object, Object> redisTemplate, CaffeineRedisCacheProperties caffeineRedisCacheProperties) {
        this.redisTemplate = redisTemplate;
        this.dynamic = caffeineRedisCacheProperties.isDynamic();
        this.caffeineRedisCacheProperties = caffeineRedisCacheProperties;
        this.caffeine = caffeineRedisCacheProperties.getCaffeine();
        this.redis = caffeineRedisCacheProperties.getRedis();
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

    public void setRemovalListener(RemovalListener<Object, Object> removalListener) {
        this.removalListener = removalListener;
    }

    /**
     * Create a new CaffeineRedisCache instance for the specified cache name.
     *
     * @param name the name of the cache
     * @return the Spring CaffeineCache adapter (or a decorator thereof)
     */
    protected Cache createCaffeineRedisCache(String name) {
        // 解析spec
        CustomCaffeineSpec customCaffeineSpec = this.getCaffeineSpec(name);
        Tuple2<com.github.benmanes.caffeine.cache.Cache<Object, Object>, CacheLoader> tuple2 = createNativeCaffeineCache(name, customCaffeineSpec);
        if (null == customCaffeineSpec) {
            return new CaffeineRedisCache(name, tuple2.getT1(), redisTemplate, caffeineRedisCacheProperties, tuple2.getT2(), 0);
        }

        return new CaffeineRedisCache(name, tuple2.getT1(), redisTemplate, caffeineRedisCacheProperties, tuple2.getT2(),
                customCaffeineSpec.getExpireTime());
    }

    /**
     * Create a native Caffeine Cache instance for the specified cache name.
     *
     * @param name the name of the cache
     * @return the native Caffeine Cache instance
     */
    protected Tuple2<com.github.benmanes.caffeine.cache.Cache<Object, Object>, CacheLoader> createNativeCaffeineCache(String name,
                                                                                                                      CustomCaffeineSpec customCaffeineSpec) {
        if (null == customCaffeineSpec) {
            if (null != this.removalListener) {
                defaultCacheBuilder.removalListener(this.removalListener);
            }
            return Tuple2.of(defaultCacheBuilder.build(), null);
        }

        Caffeine<Object, Object> cacheBuilder = customCaffeineSpec.toBuilder();

        if (null != this.removalListener) {
            cacheBuilder.removalListener(this.removalListener);
        }
        if ("refreshAfterWrite".equals(customCaffeineSpec.getExpireStrategy())) {
            // TODO 验证下来发现，如果使用CustomExpiry，获取过期缓存项时会阻塞所有线程，未使用到refreshAfterWrite的特性
            //cacheBuilder.expireAfter(new CustomExpiry(redisTemplate, caffeineRedisCacheProperties, name, customCaffeineSpec.getExpireTime()));

            CustomCacheLoader cacheLoader = new CustomCacheLoader();

            return Tuple2.of(cacheBuilder.build(cacheLoader), cacheLoader);
        }
        return Tuple2.of(cacheBuilder.build(), null);
    }

    private CustomCaffeineSpec getCaffeineSpec(String name) {
        String spec = caffeine.getSpec(name);
        logger.info("create native caffiene cache, name={}, spec={}", name, spec);
        if (!StringUtils.hasText(spec)) {
            return null;
        }
        return CustomCaffeineSpec.parse(spec);
    }

    /**
     * 清理本地缓存
     */
    public void clearLocalCache(String cacheName, Object key) {
        Cache cache = cacheMap.get(cacheName);
        if (cache == null) {
            return;
        }

        CaffeineRedisCache redisCaffeineCache = (CaffeineRedisCache) cache;
        redisCaffeineCache.clearLocalCache(key);
    }

    /**
     * 判断是否为当前缓存实例
     */
    public boolean currentCacheInstance(String instanceId) {
        return caffeineRedisCacheProperties.getInstanceId().equals(instanceId);
    }
}
