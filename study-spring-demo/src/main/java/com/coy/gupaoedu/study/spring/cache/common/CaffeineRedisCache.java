package com.coy.gupaoedu.study.spring.cache.common;

import com.github.benmanes.caffeine.cache.Cache;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.cache.support.AbstractValueAdaptingCache;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.lang.Nullable;
import org.springframework.util.Assert;
import org.springframework.util.StringUtils;

import java.util.Set;
import java.util.concurrent.Callable;
import java.util.concurrent.TimeUnit;
import java.util.function.Function;

/**
 * @author chenck
 * @date 2020/4/28 19:55
 */
public class CaffeineRedisCache extends AbstractValueAdaptingCache {

    private final Logger logger = LoggerFactory.getLogger(CaffeineRedisCache.class);

    /**
     * 缓存名字
     */
    private final String name;

    /**
     * Caffeine Cache
     */
    private final Cache<Object, Object> caffeineCache;

    /**
     * RedisTemplate
     */
    private final RedisTemplate<Object, Object> redisTemplate;

    /**
     * Caffeine 属性配置
     */
    private final CaffeineRedisCacheProperties.Caffeine caffeine;

    /**
     * Redis 属性配置
     */
    private final CaffeineRedisCacheProperties.Redis redis;

    /**
     * Create a {@link CaffeineRedisCache} instance with the specified name and the
     * given internal {@link com.github.benmanes.caffeine.cache.Cache} to use.
     *
     * @param name                         the name of the cache
     * @param caffeineCache                the backing Caffeine Cache instance
     * @param redisTemplate                whether to accept and convert {@code null}values for this cache
     * @param caffeineRedisCacheProperties the properties for this cache
     */
    public CaffeineRedisCache(String name, Cache<Object, Object> caffeineCache, RedisTemplate<Object, Object> redisTemplate,
                              CaffeineRedisCacheProperties caffeineRedisCacheProperties) {
        super(caffeineRedisCacheProperties.isAllowNullValues());
        Assert.notNull(name, "Name must not be null");
        Assert.notNull(caffeineCache, "Cache must not be null");
        Assert.notNull(redisTemplate, "RedisTemplate must not be null");
        Assert.notNull(caffeineRedisCacheProperties, "CaffeineRedisCacheProperties must not be null");
        this.name = name;
        this.caffeineCache = caffeineCache;
        this.redisTemplate = redisTemplate;
        this.caffeine = caffeineRedisCacheProperties.getCaffeine();
        this.redis = caffeineRedisCacheProperties.getRedis();
    }

    @Override
    public final String getName() {
        return this.name;
    }

    @Override
    public final Cache<Object, Object> getNativeCache() {
        return this.caffeineCache;
    }

    @Override
    @Nullable
    public ValueWrapper get(Object key) {
        /*
        // 暂不支持LoadingCache，因为没法自定义load()方法中加载数据的逻辑
        if (this.caffeineCache instanceof LoadingCache) {
            Object value = ((LoadingCache<Object, Object>) this.caffeineCache).get(key);
            return toValueWrapper(value);
        }*/
        return super.get(key);
    }

    @Override
    @Nullable
    public <T> T get(Object key, final Callable<T> valueLoader) {
        // get(key, Callable) 方式更加灵活，可扩展加载数据的逻辑
        // 并发场景下，利用Caffeine的本身机制来对数据加载进行并发控制
        return (T) fromStoreValue(this.caffeineCache.get(key, new CaffeineRedisCache.LoadFunction(name, key, valueLoader)));
    }

    @Override
    @Nullable
    protected Object lookup(Object key) {
        Object value = caffeineCache.getIfPresent(key);
        if (value != null) {
            logger.debug("get cache from caffeine, the key is : {}", key);
            return value;
        }

        Object redisKey = getRedisKey(key);
        value = redisTemplate.opsForValue().get(redisKey);

        if (value != null) {
            logger.debug("get cache from redis and put in caffeine, the key is : {}", redisKey);
            caffeineCache.put(key, value);
        }
        return value;
    }

    @Override
    public void put(Object key, @Nullable Object value) {
        Object userValue = toStoreValue(value);
        redisTemplate.opsForValue().set(getRedisKey(key), userValue, getRedisExpire(), TimeUnit.MILLISECONDS);

        cacheChangePush(new CacheMessage(this.name, key));

        this.caffeineCache.put(key, userValue);
    }

    @Override
    @Nullable
    public ValueWrapper putIfAbsent(Object key, @Nullable final Object value) {
        Object userValue = toStoreValue(value);
        Object redisKey = getRedisKey(key);
        // 如果不存在，则设置
        boolean flag = this.redisTemplate.opsForValue().setIfAbsent(redisKey, userValue, getRedisExpire(), TimeUnit.MILLISECONDS);

        if (!flag) {
            // key存在，则取原值并返回
            Object oldValue = redisTemplate.opsForValue().get(redisKey);
            return toValueWrapper(oldValue);
        }

        cacheChangePush(new CacheMessage(this.name, key));

        this.caffeineCache.put(key, userValue);

        return toValueWrapper(userValue);
    }

    @Override
    public void evict(Object key) {
        // 先清除redis中缓存数据，然后清除caffeine中的缓存，避免短时间内如果先清除caffeine缓存后其他请求会再从redis里加载到caffeine中
        this.redisTemplate.delete(getRedisKey(key));

        cacheChangePush(new CacheMessage(this.name, key));

        this.caffeineCache.invalidate(key);
    }

    @Override
    public void clear() {
        // 先清除redis中缓存数据，然后清除caffeine中的缓存，避免短时间内如果先清除caffeine缓存后其他请求会再从redis里加载到caffeine中
        Set<Object> keys = redisTemplate.keys(this.name.concat(":"));
        for (Object key : keys) {
            redisTemplate.delete(key);
        }

        cacheChangePush(new CacheMessage(this.name, null));

        this.caffeineCache.invalidateAll();
    }

    /**
     * 获取redis key
     */
    private Object getRedisKey(Object key) {
        StringBuilder sb = new StringBuilder();
        sb.append(this.name).append(":");
        if (this.redis.isUseKeyPrefix() && !StringUtils.isEmpty(this.redis.getKeyPrefix())) {
            sb.append(this.redis.getKeyPrefix()).append(":");
        }
        sb.append(key.toString());
        return sb.toString();
    }

    /**
     * 获取过期时间
     */
    private long getRedisExpire() {
        Long cacheNameExpire = redis.getExpires().get(this.name);
        if (null != cacheNameExpire) {
            return cacheNameExpire.longValue();
        }
        return redis.getDefaultTimeToLive();
    }

    /**
     * 缓存变更时通知其他节点清理本地缓存
     */
    private void cacheChangePush(CacheMessage message) {
        redisTemplate.convertAndSend(redis.getTopic(), message);
    }

    /**
     * 清理本地缓存
     */
    public void clearLocalCache(Object key) {
        logger.info("clear local cache, the key is : {}", key);
        if (key == null) {
            caffeineCache.invalidateAll();
        } else {
            caffeineCache.invalidate(key);
        }
    }

    /**
     * 加载数据 Function
     */
    private class LoadFunction implements Function<Object, Object> {

        private final String name;
        private final Object key;
        private final Callable<?> valueLoader;

        public LoadFunction(String name, Object key, Callable<?> valueLoader) {
            this.name = name;
            this.key = key;
            this.valueLoader = valueLoader;
        }

        @Override
        public Object apply(Object o) {
            try {
                // 走到此处，表明已经从本地缓存中没有获取到数据，所以先从redis中获取数据
                Object redisKey = getRedisKey(key);
                Object value = redisTemplate.opsForValue().get(redisKey);

                if (value != null) {
                    logger.info("[LoadFunction]get cache from redis, the key is : {}", redisKey);
                    // 从redis中获取到数据后不需要显示设置到本地缓存，利用Caffeine本身的机制进行设置
                    return value;
                }
                // 执行业务方法获取数据
                Object userValue = toStoreValue(this.valueLoader.call());

                redisTemplate.opsForValue().set(getRedisKey(key), userValue, getRedisExpire(), TimeUnit.MILLISECONDS);

                cacheChangePush(new CacheMessage(this.name, key));

                return userValue;
            } catch (Exception ex) {
                throw new ValueRetrievalException(o, this.valueLoader, ex);
            }
        }
    }

}
