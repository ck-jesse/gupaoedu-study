package com.coy.gupaoedu.study.spring.cache.common;

import lombok.Getter;
import lombok.Setter;
import org.springframework.boot.context.properties.ConfigurationProperties;

import java.time.Duration;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * 一二级缓存属性配置
 *
 * @author chenck
 * @date 2020/4/26 20:44
 */
@ConfigurationProperties(prefix = "spring.cache.multi")
@Getter
public class RedisCaffeineCacheProperties {

    /**
     * 要创建的缓存名字
     */
    private List<String> cacheNames = new ArrayList<>();

    /**
     * 是否存储空值，默认true，防止缓存穿透
     */
    private boolean allowNullValues = true;
    /**
     * 是否动态根据cacheName创建Cache的实现，默认true
     */
    private boolean dynamic = true;
    /**
     * 缓存key的前缀
     */
    private String cachePrefix;

    private final Caffeine caffeine = new Caffeine();

    private final Redis redis = new Redis();

    /**
     * Caffeine specific cache properties.
     */
    public static class Caffeine {

    }

    /**
     * Redis-specific cache properties.
     */
    @Getter
    @Setter
    public static class Redis {

        /**
         * 全局过期时间，单位毫秒，默认不过期
         * Entry expiration. By default the entries never expire.
         */
        private Duration defaultTimeToLive;

        /**
         * Allow caching null values.
         */
        private boolean cacheNullValues = true;

        /**
         * Key prefix.
         */
        private String keyPrefix;

        /**
         * Whether to use the key prefix when writing to Redis.
         */
        private boolean useKeyPrefix = true;
        /**
         * 每个cacheName的过期时间，单位毫秒，优先级比defaultTimeToLive高
         */
        private Map<String, Long> expires = new HashMap<>();
        /**
         * 缓存更新时通知其他节点的topic名称
         */
        private String topic = "cache:redis:caffeine:topic";
    }
}
