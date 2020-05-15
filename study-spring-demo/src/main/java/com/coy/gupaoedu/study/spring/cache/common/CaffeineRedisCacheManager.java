package com.coy.gupaoedu.study.spring.cache.common;

import com.github.benmanes.caffeine.cache.CacheLoader;
import com.github.benmanes.caffeine.cache.Caffeine;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.cache.Cache;
import org.springframework.data.redis.core.RedisTemplate;

/**
 * 缓存管理器
 *
 * @author chenck
 * @date 2020/4/28 19:54
 */
public class CaffeineRedisCacheManager extends AbstractCaffeineRedisCacheManager {

    private final Logger logger = LoggerFactory.getLogger(CaffeineRedisCacheManager.class);

    public CaffeineRedisCacheManager(RedisTemplate<Object, Object> redisTemplate, CaffeineRedisCacheProperties caffeineRedisCacheProperties) {
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
        Tuple2<com.github.benmanes.caffeine.cache.Cache<Object, Object>, CacheLoader> tuple2 = createNativeCaffeineCache(name, customCaffeineSpec);
        long expireTime = 0L;
        if (null != customCaffeineSpec) {
            expireTime = customCaffeineSpec.getExpireTime();
        }
        return new CaffeineRedisCache(name, getRedisTemplate(), getCaffeineRedisCacheProperties(), expireTime, tuple2.getT1(), tuple2.getT2());
    }

    /**
     * Create a native Caffeine Cache instance for the specified cache name.
     *
     * @param name the name of the cache
     * @return the native Caffeine Cache instance
     */
    private Tuple2<com.github.benmanes.caffeine.cache.Cache<Object, Object>, CacheLoader> createNativeCaffeineCache(String name,
                                                                                                                    CustomCaffeineSpec customCaffeineSpec) {
        if (null == customCaffeineSpec) {
            if (null != this.getRemovalListener()) {
                getDefaultCacheBuilder().removalListener(this.getRemovalListener());
            }
            return Tuple2.of(getDefaultCacheBuilder().build(), null);
        }

        Caffeine<Object, Object> cacheBuilder = customCaffeineSpec.toBuilder();

        if (null != this.getRemovalListener()) {
            cacheBuilder.removalListener(this.getRemovalListener());
        }
        if ("refreshAfterWrite".equals(customCaffeineSpec.getExpireStrategy())) {
            // TODO 验证下来发现，如果使用CustomExpiry，获取过期缓存项时会阻塞所有线程，未使用到refreshAfterWrite的特性
            //cacheBuilder.expireAfter(new CustomExpiry(getRedisTemplate(), getCaffeineRedisCacheProperties(), name,
            //        customCaffeineSpec.getExpireTime()));

            CustomCacheLoader cacheLoader = new CustomCacheLoader();

            return Tuple2.of(cacheBuilder.build(cacheLoader), cacheLoader);
        }
        return Tuple2.of(cacheBuilder.build(), null);
    }

}
