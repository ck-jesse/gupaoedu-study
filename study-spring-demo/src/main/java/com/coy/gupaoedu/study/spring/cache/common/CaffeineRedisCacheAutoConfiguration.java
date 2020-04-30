package com.coy.gupaoedu.study.spring.cache.common;

import com.github.benmanes.caffeine.cache.Caffeine;
import com.github.benmanes.caffeine.cache.CaffeineSpec;
import com.github.benmanes.caffeine.cache.Expiry;
import org.checkerframework.checker.index.qual.NonNegative;
import org.checkerframework.checker.nullness.qual.NonNull;
import org.springframework.beans.factory.ObjectProvider;
import org.springframework.boot.autoconfigure.AutoConfigureAfter;
import org.springframework.boot.autoconfigure.cache.CacheManagerCustomizers;
import org.springframework.boot.autoconfigure.condition.ConditionalOnBean;
import org.springframework.boot.autoconfigure.data.redis.RedisAutoConfiguration;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.listener.ChannelTopic;
import org.springframework.data.redis.listener.RedisMessageListenerContainer;
import org.springframework.util.CollectionUtils;
import org.springframework.util.StringUtils;

import java.util.List;

/**
 * @author chenck
 * @date 2020/4/29 10:58
 */
@Configuration
@AutoConfigureAfter(RedisAutoConfiguration.class)
@EnableConfigurationProperties(CaffeineRedisCacheProperties.class)
public class CaffeineRedisCacheAutoConfiguration {

    private final RedisTemplate<Object, Object> redisTemplate;

    private final CaffeineRedisCacheProperties caffeineRedisCacheProperties;

    private final CacheManagerCustomizers customizers;

    private final Caffeine<Object, Object> caffeine;

    private final CaffeineSpec caffeineSpec;

    // 构造器注入
    CaffeineRedisCacheAutoConfiguration(RedisTemplate<Object, Object> redisTemplate,
                                        CaffeineRedisCacheProperties caffeineRedisCacheProperties,
                                        CacheManagerCustomizers customizers,
                                        ObjectProvider<Caffeine<Object, Object>> caffeine,
                                        ObjectProvider<CaffeineSpec> caffeineSpec) {
        this.redisTemplate = redisTemplate;
        this.caffeineRedisCacheProperties = caffeineRedisCacheProperties;
        this.customizers = customizers;
        this.caffeine = caffeine.getIfAvailable();
        this.caffeineSpec = caffeineSpec.getIfAvailable();
    }

    @Bean
    @ConditionalOnBean(RedisTemplate.class)
    public CaffeineRedisCacheManager cacheManager() {
        CaffeineRedisCacheManager cacheManager = new CaffeineRedisCacheManager(redisTemplate, caffeineRedisCacheProperties);
        // cache 构建
        String specification = this.caffeineRedisCacheProperties.getCaffeine().getSpec();
        if (StringUtils.hasText(specification)) {
            cacheManager.setCacheSpecification(specification);
        } else if (this.caffeineSpec != null) {
            cacheManager.setCaffeineSpec(this.caffeineSpec);
        } else if (this.caffeine != null) {
            cacheManager.setCaffeine(this.caffeine);
        }

        List<String> cacheNames = this.caffeineRedisCacheProperties.getCacheNames();
        if (!CollectionUtils.isEmpty(cacheNames)) {
            cacheManager.setCacheNames(cacheNames);
        }

        cacheManager.setExpiry(expiry());
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

    @Bean
    public Expiry<Object, Object> expiry() {
        // 自定义过期策略 — 到期时间由 Expiry 实现独自计算，计算延长或缩短条目的生存时间
        // 这个API和expireAfterWrite/expireAfterAccess两个API是互斥的
        // 利用时间轮，来进行过期处理。时间轮一个高效的处理定时任务的结构，可以简单的将其看做是一个多维数组。
        // 参考 https://www.cnblogs.com/liujinhua306/p/9808500.html
        return new Expiry<Object, Object>() {
            // 返回创建后的过期时间
            @Override
            public long expireAfterCreate(@NonNull Object key, @NonNull Object value, long currentTime) {
                return 0;
            }

            // 返回更新后的过期时间
            @Override
            public long expireAfterUpdate(@NonNull Object key, @NonNull Object value, long currentTime, @NonNegative long currentDuration) {
                return 0;
            }

            // 返回读取后的过期时间
            @Override
            public long expireAfterRead(@NonNull Object key, @NonNull Object value, long currentTime, @NonNegative long currentDuration) {
                return 0;
            }
        };
    }

    // 在resources/META-INF/spring.factories文件中增加spring boot配置扫描
    // # Auto Configure
    //org.springframework.boot.autoconfigure.EnableAutoConfiguration=\
    //com.coy.gupaoedu.study.spring.cache.common.CaffeineRedisCacheAutoConfiguration
}
