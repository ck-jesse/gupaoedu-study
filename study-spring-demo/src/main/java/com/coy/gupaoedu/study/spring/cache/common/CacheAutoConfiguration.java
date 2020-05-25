package com.coy.gupaoedu.study.spring.cache.common;

import com.fasterxml.jackson.annotation.JsonAutoDetect;
import com.fasterxml.jackson.annotation.PropertyAccessor;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.github.benmanes.caffeine.cache.RemovalCause;
import com.github.benmanes.caffeine.cache.RemovalListener;
import org.checkerframework.checker.nullness.qual.NonNull;
import org.checkerframework.checker.nullness.qual.Nullable;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.ObjectProvider;
import org.springframework.boot.autoconfigure.cache.CacheManagerCustomizer;
import org.springframework.boot.autoconfigure.cache.CacheManagerCustomizers;
import org.springframework.boot.autoconfigure.condition.ConditionalOnClass;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.cache.CacheManager;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.redis.connection.RedisConnectionFactory;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.serializer.Jackson2JsonRedisSerializer;
import org.springframework.data.redis.serializer.StringRedisSerializer;

import java.util.stream.Collectors;

/**
 * @author chenck
 * @date 2020/4/29 10:58
 */
@Configuration
@ConditionalOnClass(CacheManager.class)
public class CacheAutoConfiguration {

    private final Logger logger = LoggerFactory.getLogger(CacheAutoConfiguration.class);

    /**
     * 替换RedisTemplate的默认jdk序列化为json序列化
     */
    @Bean
    @ConditionalOnMissingBean
    public RedisTemplate redisTemplate(RedisConnectionFactory redisConnectionFactory) {
        RedisTemplate redisTemplate = new RedisTemplate();
        redisTemplate.setConnectionFactory(redisConnectionFactory);

        // 使用Jackson2JsonRedisSerialize 替换默认序列化
        Jackson2JsonRedisSerializer jackson2JsonRedisSerializer = new Jackson2JsonRedisSerializer<>(Object.class);
        ObjectMapper objectMapper = new ObjectMapper();
        objectMapper.setVisibility(PropertyAccessor.ALL, JsonAutoDetect.Visibility.ANY);
        objectMapper.enableDefaultTyping(ObjectMapper.DefaultTyping.NON_FINAL);
        jackson2JsonRedisSerializer.setObjectMapper(objectMapper);

        StringRedisSerializer stringRedisSerializer = new StringRedisSerializer();

        // 设置value的序列化规则和 key的序列化规则
        redisTemplate.setKeySerializer(stringRedisSerializer);
        redisTemplate.setHashKeySerializer(stringRedisSerializer);
        redisTemplate.setValueSerializer(jackson2JsonRedisSerializer);
        redisTemplate.setHashValueSerializer(jackson2JsonRedisSerializer);
        redisTemplate.afterPropertiesSet();
        return redisTemplate;
    }

    @Bean
    @ConditionalOnMissingBean
    public CacheManagerCustomizers cacheManagerCustomizers(ObjectProvider<CacheManagerCustomizer<?>> customizers) {
        return new CacheManagerCustomizers(customizers.orderedStream().collect(Collectors.toList()));
    }

    /**
     * 移除监听器
     */
    @Bean
    public RemovalListener<Object, Object> removalListener() {
        return new RemovalListener<Object, Object>() {
            private final Logger logger = LoggerFactory.getLogger(RemovalListener.class);

            @Override
            public void onRemoval(@Nullable Object key, @Nullable Object value, @NonNull RemovalCause cause) {
                logger.debug("[RemovalListener] key={}, value={}", key, value);
            }
        };
    }

    /**
     * 自定义CacheLoader
     * 描述：因refreshAfterWrite必须使用LoadingCache,所以定义该默认的CacheLoader。
     */
    /*@Bean
    @ConditionalOnProperty(value = "spring.cache.multi.enableRefreshAfterWrite", havingValue = "true")
    @ConditionalOnMissingBean
    public CustomCacheLoader cacheLoader() {
        return new CustomCacheLoader();
    }*/

    /**
     * 自定义Expiry
     */
    /*@Bean
    @ConditionalOnProperty(value = "spring.cache.multi.enableRefreshAfterWrite", havingValue = "true")
    @ConditionalOnMissingBean
    public CustomExpiry expiry(RedisTemplate<Object, Object> redisTemplate, CaffeineRedisCacheProperties caffeineRedisCacheProperties) {
        return new CustomExpiry(redisTemplate, caffeineRedisCacheProperties);
    }*/
}
