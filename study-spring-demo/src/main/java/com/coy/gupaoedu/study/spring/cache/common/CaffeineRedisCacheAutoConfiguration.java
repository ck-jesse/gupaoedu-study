package com.coy.gupaoedu.study.spring.cache.common;

import com.github.benmanes.caffeine.cache.Caffeine;
import com.github.benmanes.caffeine.cache.RemovalListener;
import org.springframework.boot.autoconfigure.AutoConfigureAfter;
import org.springframework.boot.autoconfigure.cache.CacheManagerCustomizers;
import org.springframework.boot.autoconfigure.condition.ConditionalOnClass;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.cache.CacheManager;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.listener.ChannelTopic;
import org.springframework.data.redis.listener.RedisMessageListenerContainer;

/**
 * @author chenck
 * @date 2020/4/29 10:58
 */
@Configuration
@ConditionalOnClass({Caffeine.class, CaffeineRedisCacheManager.class})
@ConditionalOnMissingBean(CacheManager.class)
@AutoConfigureAfter(CacheAutoConfiguration.class)
@EnableConfigurationProperties(CaffeineRedisCacheProperties.class)
public class CaffeineRedisCacheAutoConfiguration {

    private final RedisTemplate<Object, Object> redisTemplate;

    private final CaffeineRedisCacheProperties caffeineRedisCacheProperties;

    private final CacheManagerCustomizers customizers;

    private final RemovalListener<Object, Object> removalListener;

    // 构造器注入
    CaffeineRedisCacheAutoConfiguration(RedisTemplate<Object, Object> redisTemplate,
                                        CaffeineRedisCacheProperties caffeineRedisCacheProperties,
                                        CacheManagerCustomizers customizers,
                                        RemovalListener<Object, Object> removalListener) {
        this.redisTemplate = redisTemplate;
        this.caffeineRedisCacheProperties = caffeineRedisCacheProperties;
        this.customizers = customizers;
        this.removalListener = removalListener;
    }

    /**
     * 定义同步Caffeine
     */
    @Bean
    @ConditionalOnMissingBean
    public ExtendCacheManager cacheManager() {
        ExtendCacheManager cacheManager = new CaffeineRedisCacheManager(redisTemplate, caffeineRedisCacheProperties);

        if (null != this.removalListener) {
            cacheManager.setRemovalListener(this.removalListener);
        }

        // 扩展点，源码中有很多可以借鉴的点
        return this.customizers.customize(cacheManager);
    }

    @Bean
    public RedisMessageListenerContainer redisMessageListenerContainer(RedisTemplate<Object, Object> redisTemplate,
                                                                       ExtendCacheManager extendCacheManager) {
        RedisMessageListenerContainer redisMessageListenerContainer = new RedisMessageListenerContainer();
        redisMessageListenerContainer.setConnectionFactory(redisTemplate.getConnectionFactory());
        CacheMessageListener cacheMessageListener = new CacheMessageListener(redisTemplate, extendCacheManager);
        redisMessageListenerContainer.addMessageListener(cacheMessageListener, new ChannelTopic(caffeineRedisCacheProperties.getRedis().getTopic()));
        return redisMessageListenerContainer;
    }

    // 在resources/META-INF/spring.factories文件中增加spring boot配置扫描
    //org.springframework.boot.autoconfigure.EnableAutoConfiguration=\
    //com.coy.gupaoedu.study.spring.cache.common.CaffeineRedisCacheAutoConfiguration

    // 注意：自定义spring-boot-starter时，需要让 CacheAutoConfiguration 被spring扫描到，否则CaffeineRedisCacheAutoConfiguration会注入失败。
    // 为了减少启动时扫描的类，也可以将CacheAutoConfiguration中的内容在CaffeineRedisCacheAutoConfiguration中定义。
}
