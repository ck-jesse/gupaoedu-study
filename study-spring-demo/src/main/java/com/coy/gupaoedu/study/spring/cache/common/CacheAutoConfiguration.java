package com.coy.gupaoedu.study.spring.cache.common;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.ObjectProvider;
import org.springframework.boot.autoconfigure.cache.CacheManagerCustomizer;
import org.springframework.boot.autoconfigure.cache.CacheManagerCustomizers;
import org.springframework.boot.autoconfigure.condition.ConditionalOnClass;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
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
     * 定义一个默认的 CacheLoader
     * 描述：因refreshAfterWrite必须使用LoadingCache,所以定义该默认的CacheLoader。
     */
    @Bean
    @ConditionalOnProperty(value = "spring.cache.multi.enableRefreshAfterWrite", havingValue = "true")
    @ConditionalOnMissingBean
    public CustomCacheLoader cacheLoader() {
        return new CustomCacheLoader();
    }

    /*@Bean
    public Expiry<Object, Object> expiry() {
        // 自定义过期策略 — 到期时间由 Expiry 实现独自计算，计算延长或缩短条目的生存时间
        // 这个API和expireAfterWrite/expireAfterAccess两个API是互斥的，也就是只支持refreshAfterWrite
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
    }*/

}
