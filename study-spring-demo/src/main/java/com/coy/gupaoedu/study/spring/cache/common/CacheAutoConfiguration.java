package com.coy.gupaoedu.study.spring.cache.common;

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

import java.util.stream.Collectors;

/**
 * @author chenck
 * @date 2020/4/29 10:58
 */
@Configuration
@ConditionalOnClass(CacheManager.class)
public class CacheAutoConfiguration {

    private final Logger logger = LoggerFactory.getLogger(CacheAutoConfiguration.class);

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