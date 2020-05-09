package com.coy.gupaoedu.study.spring.cache.common;

import com.github.benmanes.caffeine.cache.Cache;
import com.github.benmanes.caffeine.cache.CacheLoader;
import com.github.benmanes.caffeine.cache.LoadingCache;
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
     * 缓存实例id
     */
    private final String instanceId;

    /**
     * 缓存名字
     */
    private final String name;

    /**
     * Caffeine Cache
     */
    private final Cache<Object, Object> caffeineCache;

    //
    @Nullable
    private final CacheLoader<Object, Object> cacheLoader;

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
                              CaffeineRedisCacheProperties caffeineRedisCacheProperties, CacheLoader<Object, Object> cacheLoader) {
        super(caffeineRedisCacheProperties.isAllowNullValues());
        Assert.notNull(caffeineRedisCacheProperties.getInstanceId(), "Instance Id must not be null");
        Assert.notNull(name, "Name must not be null");
        Assert.notNull(caffeineCache, "Cache must not be null");
        Assert.notNull(redisTemplate, "RedisTemplate must not be null");
        this.instanceId = caffeineRedisCacheProperties.getInstanceId();
        this.name = name;
        this.caffeineCache = caffeineCache;
        this.redisTemplate = redisTemplate;
        this.caffeine = caffeineRedisCacheProperties.getCaffeine();
        this.redis = caffeineRedisCacheProperties.getRedis();
        this.cacheLoader = cacheLoader;
    }

    @Override
    public final String getName() {
        return this.name;
    }

    @Override
    public final Cache<Object, Object> getNativeCache() {
        return this.caffeineCache;
    }

    // @Cacheable(sync=false) 进入此方法
    // 并发场景：未做同步控制，所以存在多个线程同时加载数据的情况，即可能存在缓存击穿的情况
    @Override
    @Nullable
    public ValueWrapper get(Object key) {
        if (this.caffeineCache instanceof LoadingCache) {
            if (null != this.cacheLoader && this.cacheLoader instanceof CustomCacheLoader) {
                // 将valueLoader设置为null，保证load()直接返回null
                ((CustomCacheLoader) this.cacheLoader).setValueLoader(null);
            }
            Object value = ((LoadingCache<Object, Object>) this.caffeineCache).get(key);
            logger.debug("LoadingCache.get cache, key={}, value={}", key, value);
            return toValueWrapper(value);
        }

        ValueWrapper value = super.get(key);
        logger.debug("Cache.get cache, key={}, value={}", key, value);
        return value;
    }

    // @Cacheable(sync=true) 进入此方法
    // 并发场景：仅一个线程加载数据，其他线程均阻塞
    // 注：借助Callable入参，可以实现不同缓存调用不同的加载数据逻辑的目的。
    @Override
    @Nullable
    public <T> T get(Object key, final Callable<T> valueLoader) {
        // 通过 CustomCacheLoader + refreshAfterWrite 实现不同缓存调用不同的加载数据逻辑的目的。
        if (this.caffeineCache instanceof LoadingCache) {
            if (null != this.cacheLoader && this.cacheLoader instanceof CustomCacheLoader) {
                // 将Callable设置到自定义CacheLoader中，以便在load()中执行具体的业务方法来加载数据
                CustomCacheLoader customCacheLoader = ((CustomCacheLoader) this.cacheLoader);
                customCacheLoader.setInstanceId(this.instanceId);
                customCacheLoader.setName(this.name);
                customCacheLoader.setRedisTemplate(this.redisTemplate);
                customCacheLoader.setCaffeineRedisCache(this);
                customCacheLoader.setValueLoader(valueLoader);

                // 如果是refreshAfterWrite策略，则只会阻塞加载数据的线程，其他线程返回旧值（如果是异步加载，则所有线程都返回旧值）
                Object value = ((LoadingCache<Object, Object>) this.caffeineCache).get(key);
                logger.debug("LoadingCache.get(key, callable) cache, key={}, value={}", key, value);
                return (T) fromStoreValue(value);
            }
            logger.debug("get(key, callable) cache, key={}", key);
        }

        // 同步加载数据，仅一个线程加载数据，其他线程均阻塞
        Object value = this.caffeineCache.get(key, new CaffeineRedisCache.LoadFunction(this.instanceId, this.name, key, valueLoader));
        logger.debug("get(key, callable) cache, key={}, value={}", key, value);
        return (T) fromStoreValue(value);
    }

    @Override
    @Nullable
    protected Object lookup(Object key) {
        Object value = caffeineCache.getIfPresent(key);
        if (value != null) {
            logger.debug("lookup get cache from caffeine, key={}", key);
            return value;
        }

        Object redisKey = getRedisKey(key);
        value = redisTemplate.opsForValue().get(redisKey);

        if (value != null) {
            logger.debug("lookup get cache from redis and put in caffeine, the key is : {}", redisKey);
            caffeineCache.put(key, value);
        }
        return value;
    }

    @Override
    public void put(Object key, @Nullable Object value) {
        logger.debug("put cache, key={}, value={}", key, value);
        Object userValue = toStoreValue(value);
        redisTemplate.opsForValue().set(getRedisKey(key), userValue, getRedisExpire(), TimeUnit.MILLISECONDS);

        cacheChangePush(new CacheMessage(this.instanceId, this.name, key));

        this.caffeineCache.put(key, userValue);
    }

    @Override
    @Nullable
    public ValueWrapper putIfAbsent(Object key, @Nullable final Object value) {
        logger.debug("putIfAbsent cache, key={}, value={}", key, value);
        Object userValue = toStoreValue(value);
        Object redisKey = getRedisKey(key);
        // 如果不存在，则设置
        boolean flag = this.redisTemplate.opsForValue().setIfAbsent(redisKey, userValue, getRedisExpire(), TimeUnit.MILLISECONDS);

        if (!flag) {
            // key存在，则取原值并返回
            Object oldValue = redisTemplate.opsForValue().get(redisKey);
            return toValueWrapper(oldValue);
        }

        cacheChangePush(new CacheMessage(this.instanceId, this.name, key));

        this.caffeineCache.put(key, userValue);

        return toValueWrapper(userValue);
    }

    @Override
    public void evict(Object key) {
        logger.debug("evict cache, key={}", key);
        // 先清除redis中缓存数据，然后清除caffeine中的缓存，避免短时间内如果先清除caffeine缓存后其他请求会再从redis里加载到caffeine中
        this.redisTemplate.delete(getRedisKey(key));

        cacheChangePush(new CacheMessage(this.instanceId, this.name, key));

        this.caffeineCache.invalidate(key);
    }

    @Override
    public void clear() {
        logger.debug("clear all cache");
        // 先清除redis中缓存数据，然后清除caffeine中的缓存，避免短时间内如果先清除caffeine缓存后其他请求会再从redis里加载到caffeine中
        Set<Object> keys = redisTemplate.keys(this.name.concat(":"));
        for (Object key : keys) {
            redisTemplate.delete(key);
        }

        cacheChangePush(new CacheMessage(this.instanceId, this.name, null));

        this.caffeineCache.invalidateAll();
    }

    /**
     * 获取redis key
     */
    public Object getRedisKey(Object key) {
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
    public long getRedisExpire() {
        Long cacheNameExpire = redis.getExpires().get(this.name);
        if (null != cacheNameExpire) {
            return cacheNameExpire.longValue();
        }
        return redis.getDefaultTimeToLive();
    }

    /**
     * 缓存变更时通知其他节点清理本地缓存
     */
    public void cacheChangePush(CacheMessage message) {
        redisTemplate.convertAndSend(redis.getTopic(), message);
    }

    /**
     * 清理本地缓存
     */
    public void clearLocalCache(Object key) {
        logger.info("clear local cache, key={}", key);
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

        private final String instanceId;
        private final String name;
        private final Object key;
        private final Callable<?> valueLoader;

        public LoadFunction(String instanceId, String name, Object key, Callable<?> valueLoader) {
            this.instanceId = instanceId;
            this.name = name;
            this.key = key;
            this.valueLoader = valueLoader;
        }

        @Override
        public Object apply(Object o) {
            try {
                logger.debug("[LoadFunction] load cache, key={}", key);
                // 走到此处，表明已经从本地缓存中没有获取到数据，所以先从redis中获取数据
                Object redisKey = getRedisKey(key);
                Object value = redisTemplate.opsForValue().get(redisKey);

                if (value != null) {
                    logger.info("[LoadFunction] get cache from redis, key={}, value={}", redisKey, value);
                    // 从redis中获取到数据后不需要显示设置到本地缓存，利用Caffeine本身的机制进行设置
                    return value;
                }
                // 执行业务方法获取数据
                value = toStoreValue(this.valueLoader.call());
                logger.debug("[LoadFunction] load cache, key={}, value={}", key, value);

                redisTemplate.opsForValue().set(getRedisKey(key), value, getRedisExpire(), TimeUnit.MILLISECONDS);

                cacheChangePush(new CacheMessage(this.instanceId, this.name, key));

                return value;
            } catch (Exception ex) {
                throw new ValueRetrievalException(o, this.valueLoader, ex);
            }
        }
    }

}
