package com.coy.gupaoedu.study.spring.cache.common;

import com.github.benmanes.caffeine.cache.AsyncCache;
import com.github.benmanes.caffeine.cache.AsyncCacheLoader;
import com.github.benmanes.caffeine.cache.Caffeine;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.cache.Cache;
import org.springframework.data.redis.core.RedisTemplate;

/**
 * 异步缓存管理器
 *
 * @author chenck
 * @date 2020/5/13 19:18
 */
public class AsyncCaffeineRedisCacheManager extends AbstractCaffeineRedisCacheManager {

    private final Logger logger = LoggerFactory.getLogger(AsyncCaffeineRedisCacheManager.class);

    public AsyncCaffeineRedisCacheManager(RedisTemplate<Object, Object> redisTemplate, CaffeineRedisCacheProperties caffeineRedisCacheProperties) {
        super(redisTemplate, caffeineRedisCacheProperties);
    }

    /**
     * Create a new CaffeineRedisCache instance for the specified cache name.
     *
     * @param name the name of the cache
     * @return the Spring CaffeineCache adapter (or a decorator thereof)
     */
    @Override
    protected Cache createCaffeineRedisCache(String name) {
        // 解析spec
        CustomCaffeineSpec customCaffeineSpec = getCaffeineRedisCacheProperties().getCaffeine().getCaffeineSpec(name);
        Tuple2<AsyncCache<Object, Object>, AsyncCacheLoader> tuple2 = createNativeCaffeineCache(name, customCaffeineSpec);
        long expireTime = 0L;
        if (null != customCaffeineSpec) {
            expireTime = customCaffeineSpec.getExpireTime();
        }
        return new AsyncCaffeineRedisCache(name, getRedisTemplate(), getCaffeineRedisCacheProperties(), expireTime, tuple2.getT1(), tuple2.getT2());
    }

    /**
     * Create a native Caffeine Cache instance for the specified cache name.
     *
     * @param name the name of the cache
     * @return the native Caffeine Cache instance
     */
    private Tuple2<AsyncCache<Object, Object>, AsyncCacheLoader> createNativeCaffeineCache(String name, CustomCaffeineSpec customCaffeineSpec) {
        logger.info("create async cache, name={}", name);
        if (null == customCaffeineSpec) {
            if (null != this.getRemovalListener()) {
                getDefaultCacheBuilder().removalListener(this.getRemovalListener());
            }
            return Tuple2.of(getDefaultCacheBuilder().buildAsync(), null);
        }

        Caffeine<Object, Object> cacheBuilder = customCaffeineSpec.toBuilder();

        if (null != this.getRemovalListener()) {
            cacheBuilder.removalListener(this.getRemovalListener());
        }
        if ("refreshAfterWrite".equals(customCaffeineSpec.getExpireStrategy())) {
            // TODO 验证下来发现，如果使用CustomExpiry，获取过期缓存项时会阻塞所有线程，未使用到refreshAfterWrite的特性
            //cacheBuilder.expireAfter(new CustomExpiry(getRedisTemplate(), getCaffeineRedisCacheProperties(), name,
            //customCaffeineSpec.getExpireTime()));

            AsyncCustomCacheLoader cacheLoader = new AsyncCustomCacheLoader();

            return Tuple2.of(cacheBuilder.buildAsync(cacheLoader), cacheLoader);
        }
        return Tuple2.of(cacheBuilder.buildAsync(), null);
    }

}
