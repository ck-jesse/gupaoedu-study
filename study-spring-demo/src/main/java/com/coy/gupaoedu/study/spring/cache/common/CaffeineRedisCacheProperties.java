package com.coy.gupaoedu.study.spring.cache.common;

import lombok.Getter;
import lombok.Setter;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.util.StringUtils;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;

/**
 * 一二级缓存属性配置
 * 注：Caffeine 一级缓存，Redis 二级缓存
 *
 * @author chenck
 * @date 2020/4/26 20:44
 */
@ConfigurationProperties(prefix = "spring.cache.multi")
@Getter
@Setter
public class CaffeineRedisCacheProperties {

    /**
     * 缓存实例id（默认为UUID）
     */
    private String instanceId = UUID.randomUUID().toString().replaceAll("-", "");

    /**
     * 启用refreshAfterWrite，用于加载定义CustomCacheLoader或Expiry
     */
    private Boolean enableRefreshAfterWrite = false;

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

    private final Caffeine caffeine = new Caffeine();

    private final Redis redis = new Redis();

    /**
     * Caffeine specific cache properties.
     */
    @Getter
    @Setter
    public static class Caffeine {
        /**
         * The spec to use to create caches. See CaffeineSpec for more details on the spec format.
         */
        private String defaultSpec;

        /**
         * The spec to use to create caches. See CaffeineSpec for more details on the spec format.
         * <key,value>=<cacheName, spec>
         */
        private Map<String, String> specMap = new HashMap<>();

        /**
         * 获取 spec
         */
        public String getSpec(String cacheName) {
            if (!StringUtils.hasText(cacheName)) {
                return defaultSpec;
            }
            String spec = specMap.get(cacheName);
            if (!StringUtils.hasText(spec)) {
                return defaultSpec;
            }
            return spec;
        }
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
        private long defaultTimeToLive = 0L;

        /**
         * 缓存Key prefix.
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
        private String topic = "cache:caffeine:redis:topic";

        /**
         * 获取redis key
         */
        public Object getRedisKey(String name, Object key) {
            StringBuilder sb = new StringBuilder();
            sb.append(name).append(":");
            if (this.isUseKeyPrefix() && !StringUtils.isEmpty(this.getKeyPrefix())) {
                sb.append(this.getKeyPrefix()).append(":");
            }
            sb.append(key.toString());
            return sb.toString();
        }

    }
}
