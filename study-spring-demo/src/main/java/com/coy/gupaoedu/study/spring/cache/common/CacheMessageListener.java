package com.coy.gupaoedu.study.spring.cache.common;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.redis.connection.Message;
import org.springframework.data.redis.connection.MessageListener;
import org.springframework.data.redis.core.RedisTemplate;

/**
 * 缓存消息监听
 *
 * @author chenck
 * @date 2020/4/29 10:56
 */
public class CacheMessageListener implements MessageListener {
    private final Logger logger = LoggerFactory.getLogger(CacheMessageListener.class);

    private RedisTemplate<Object, Object> redisTemplate;

    private ExtendCacheManager extendCacheManager;

    public CacheMessageListener(RedisTemplate<Object, Object> redisTemplate, ExtendCacheManager extendCacheManager) {
        super();
        this.redisTemplate = redisTemplate;
        this.extendCacheManager = extendCacheManager;
    }

    @Override
    public void onMessage(Message message, byte[] pattern) {
        CacheMessage cacheMessage = (CacheMessage) redisTemplate.getValueSerializer().deserialize(message.getBody());
        if (extendCacheManager.currentCacheInstance(cacheMessage.getInstanceId())) {
            logger.info("[RedisCacheTopicMessage] the same instanceId not deal local cache, instanceId={}, cacheName={}, key={}, optType={}",
                    cacheMessage.getInstanceId(), cacheMessage.getCacheName(), cacheMessage.getKey(), cacheMessage.getOptType());
            return;
        }
        logger.info("[RedisCacheTopicMessage] deal local cache, instanceId={}, cacheName={}, key={}, optType={}",
                cacheMessage.getInstanceId(), cacheMessage.getCacheName(), cacheMessage.getKey(), cacheMessage.getOptType());
        if (CacheConsts.CACHE_REFRESH.equals(cacheMessage.getOptType())) {
            extendCacheManager.refreshLocalCache(cacheMessage.getCacheName(), cacheMessage.getKey());
        } else {
            extendCacheManager.clearLocalCache(cacheMessage.getCacheName(), cacheMessage.getKey());
        }
    }
}
