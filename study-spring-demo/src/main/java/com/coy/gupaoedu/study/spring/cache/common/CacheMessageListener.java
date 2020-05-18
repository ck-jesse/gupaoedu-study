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
            logger.info("[RedisCacheTopicMessage] the same instanceId not refresh local cache, instanceId={}, cacheName={}, key={}",
                    cacheMessage.getInstanceId(), cacheMessage.getCacheName(), cacheMessage.getKey());
            return;
        }
        logger.info("[RedisCacheTopicMessage] refresh local cache, instanceId={}, cacheName={}, key={}",
                cacheMessage.getInstanceId(), cacheMessage.getCacheName(), cacheMessage.getKey());
        extendCacheManager.refreshLocalCache(cacheMessage.getCacheName(), cacheMessage.getKey());
    }
}
