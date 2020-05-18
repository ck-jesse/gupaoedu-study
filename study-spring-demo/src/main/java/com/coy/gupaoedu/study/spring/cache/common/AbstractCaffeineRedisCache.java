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
import java.util.concurrent.TimeUnit;

/**
 * @author chenck
 * @date 2020/4/28 19:55
 */
public abstract class AbstractCaffeineRedisCache extends AbstractValueAdaptingCache implements ExtendCache {

    private final Logger logger = LoggerFactory.getLogger(AbstractCaffeineRedisCache.class);

    /**
     * 缓存实例id
     */
    private final String instanceId;

    /**
     * 缓存名字
     */
    private final String name;

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
     * 过期时间(ms)
     */
    private long expireTime = 0L;

    /**
     * Create a {@link AbstractCaffeineRedisCache} instance with the specified name and the
     * given internal {@link Cache} to use.
     *
     * @param name                         the name of the cache
     * @param redisTemplate                whether to accept and convert {@code null}values for this cache
     * @param caffeineRedisCacheProperties the properties for this cache
     */
    public AbstractCaffeineRedisCache(String name, RedisTemplate<Object, Object> redisTemplate,
                                      CaffeineRedisCacheProperties caffeineRedisCacheProperties, long expireTime) {
        super(caffeineRedisCacheProperties.isAllowNullValues());
        Assert.notNull(caffeineRedisCacheProperties.getInstanceId(), "Instance Id must not be null");
        Assert.notNull(name, "Name must not be null");
        Assert.notNull(redisTemplate, "RedisTemplate must not be null");
        this.instanceId = caffeineRedisCacheProperties.getInstanceId();
        this.name = name;
        this.redisTemplate = redisTemplate;
        this.caffeine = caffeineRedisCacheProperties.getCaffeine();
        this.redis = caffeineRedisCacheProperties.getRedis();
        this.expireTime = expireTime;
    }

    @Override
    public final String getName() {
        return this.name;
    }

    @Override
    @Nullable
    protected Object lookup(Object key) {
        Object value = lookup0(key);
        if (value != null) {
            logger.debug("lookup get cache from caffeine, cacheName={}, key={}", this.getName(), key);
            return value;
        }

        value = getRedisValue(key);

        if (value != null) {
            logger.debug("lookup get cache from redis and put in caffeine, cacheName={}, key={}", this.getName(), key);
            put0(key, value);
        }
        return value;
    }

    @Override
    public void put(Object key, @Nullable Object value) {
        logger.debug("put cache, cacheName={}, key={}, value={}", this.getName(), key, value);
        Object userValue = toStoreValue(value);

        setRedisValue(key, userValue);

        cacheChangePush(key, CacheConsts.CACHE_REFRESH);

        put0(key, value);
    }

    @Override
    @Nullable
    public ValueWrapper putIfAbsent(Object key, @Nullable final Object value) {
        logger.debug("putIfAbsent cache, cacheName={}, key={}, value={}", this.getName(), key, value);
        Object userValue = toStoreValue(value);
        // 如果不存在，则设置
        boolean flag = this.redisTemplate.opsForValue().setIfAbsent(getRedisKey(key), userValue, getExpireTime(), TimeUnit.MILLISECONDS);

        if (!flag) {
            // key存在，则取原值并返回
            return toValueWrapper(getRedisValue(key));
        }

        cacheChangePush(key, CacheConsts.CACHE_REFRESH);

        put0(key, value);

        return toValueWrapper(userValue);
    }

    @Override
    public void evict(Object key) {
        logger.debug("evict cache, cacheName={}, key={}", this.getName(), key);
        // 先清除redis中缓存数据，然后清除caffeine中的缓存，避免短时间内如果先清除caffeine缓存后其他请求会再从redis里加载到caffeine中
        this.redisTemplate.delete(getRedisKey(key));

        cacheChangePush(key, CacheConsts.CACHE_CLEAR);

        evict0(key);
    }

    @Override
    public void clear() {
        logger.debug("clear all cache, cacheName={}", this.getName());
        // 先清除redis中缓存数据，然后清除caffeine中的缓存，避免短时间内如果先清除caffeine缓存后其他请求会再从redis里加载到caffeine中
        Set<Object> keys = redisTemplate.keys(this.name.concat(":"));
        for (Object key : keys) {
            redisTemplate.delete(key);
        }

        cacheChangePush(null, CacheConsts.CACHE_CLEAR);

        clear0();
    }

    @Override
    public long getExpireTime() {
        return expireTime;
    }

    @Override
    public Object getRedisKey(Object key) {
        StringBuilder sb = new StringBuilder();
        sb.append(name).append(":");
        if (redis.isUseKeyPrefix() && !StringUtils.isEmpty(redis.getKeyPrefix())) {
            sb.append(redis.getKeyPrefix()).append(":");
        }
        sb.append(key.toString());
        return sb.toString();
    }

    @Override
    public Object getRedisValue(Object key) {
        return redisTemplate.opsForValue().get(getRedisKey(key));
    }

    @Override
    public void setRedisValue(Object key, Object value) {
        redisTemplate.opsForValue().set(getRedisKey(key), value, getExpireTime(), TimeUnit.MILLISECONDS);
    }

    @Override
    public Object toStoreValueWrap(@Nullable Object userValue) {
        return toStoreValue(userValue);
    }

    @Override
    public void cacheChangePush(Object key, String optType) {
        redisTemplate.convertAndSend(redis.getTopic(), new CacheMessage(this.instanceId, this.name, key, optType));
    }

    // the abstract method of operate native cache

    public abstract Object lookup0(Object key);

    public abstract void put0(Object key, Object value);

    public abstract void evict0(Object key);

    public abstract void clear0();
}
