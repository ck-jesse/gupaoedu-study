package com.coy.gupaoedu.study.spring.cache.common;

import com.github.benmanes.caffeine.cache.Caffeine;
import org.springframework.boot.autoconfigure.AutoConfigureAfter;
import org.springframework.boot.autoconfigure.cache.CacheManagerCustomizers;
import org.springframework.boot.autoconfigure.condition.ConditionalOnClass;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.boot.autoconfigure.data.redis.RedisAutoConfiguration;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.cache.CacheManager;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.listener.ChannelTopic;
import org.springframework.data.redis.listener.RedisMessageListenerContainer;
import org.springframework.util.CollectionUtils;

import java.util.List;

/**
 * @author chenck
 * @date 2020/4/29 10:58
 */
@Configuration
@ConditionalOnClass({Caffeine.class, CaffeineRedisCacheManager.class})
@ConditionalOnMissingBean(CacheManager.class)
@AutoConfigureAfter(RedisAutoConfiguration.class)
@EnableConfigurationProperties(CaffeineRedisCacheProperties.class)
public class CaffeineRedisCacheAutoConfiguration {

    private final RedisTemplate<Object, Object> redisTemplate;

    private final CaffeineRedisCacheProperties caffeineRedisCacheProperties;

    private final CacheManagerCustomizers customizers;

    // 构造器注入
    CaffeineRedisCacheAutoConfiguration(RedisTemplate<Object, Object> redisTemplate,
                                        CaffeineRedisCacheProperties caffeineRedisCacheProperties,
                                        CacheManagerCustomizers customizers) {
        this.redisTemplate = redisTemplate;
        this.caffeineRedisCacheProperties = caffeineRedisCacheProperties;
        this.customizers = customizers;
    }

    @Bean
    public CaffeineRedisCacheManager cacheManager() {
        CaffeineRedisCacheManager cacheManager = new CaffeineRedisCacheManager(redisTemplate, caffeineRedisCacheProperties);

        List<String> cacheNames = this.caffeineRedisCacheProperties.getCacheNames();
        if (!CollectionUtils.isEmpty(cacheNames)) {
            cacheManager.setCacheNames(cacheNames);
        }

        // 扩展点，源码中有很多可以借鉴的点
        return this.customizers.customize(cacheManager);
    }

    @Bean
    public RedisMessageListenerContainer redisMessageListenerContainer(RedisTemplate<Object, Object> redisTemplate,
                                                                       CaffeineRedisCacheManager caffeineRedisCacheManager) {
        RedisMessageListenerContainer redisMessageListenerContainer = new RedisMessageListenerContainer();
        redisMessageListenerContainer.setConnectionFactory(redisTemplate.getConnectionFactory());
        CacheMessageListener cacheMessageListener = new CacheMessageListener(redisTemplate, caffeineRedisCacheManager);
        redisMessageListenerContainer.addMessageListener(cacheMessageListener, new ChannelTopic(caffeineRedisCacheProperties.getRedis().getTopic()));
        return redisMessageListenerContainer;
    }

    // 在resources/META-INF/spring.factories文件中增加spring boot配置扫描
    // # Auto Configure
    //org.springframework.boot.autoconfigure.EnableAutoConfiguration=\
    //com.coy.gupaoedu.study.spring.cache.common.CaffeineRedisCacheAutoConfiguration
}
