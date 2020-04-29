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

    private CaffeineRedisCacheManager caffeineRedisCacheManager;

    public CacheMessageListener(RedisTemplate<Object, Object> redisTemplate, CaffeineRedisCacheManager redisCaffeineCacheManager) {
        super();
        this.redisTemplate = redisTemplate;
        this.caffeineRedisCacheManager = redisCaffeineCacheManager;
    }

    @Override
    public void onMessage(Message message, byte[] pattern) {
        CacheMessage cacheMessage = (CacheMessage) redisTemplate.getValueSerializer().deserialize(message.getBody());

        logger.debug("recevice a redis topic message, clear local cache, the cacheName is {}, the key is {}", cacheMessage.getCacheName(),
                cacheMessage.getKey());
        caffeineRedisCacheManager.clearLocalCache(cacheMessage.getCacheName(), cacheMessage.getKey());
    }
}
