package com.coy.gupaoedu.study.spring.cache.common;

import com.alibaba.excel.util.DateUtils;
import com.github.benmanes.caffeine.cache.Expiry;
import org.checkerframework.checker.index.qual.NonNegative;
import org.checkerframework.checker.nullness.qual.NonNull;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.redis.core.RedisTemplate;

import java.util.Date;
import java.util.concurrent.TimeUnit;

/**
 * 自定义过期策略 — 到期时间由 Expiry 实现独自计算，计算延长或缩短条目的生存时间
 * 这个API和expireAfterWrite/expireAfterAccess两个API是互斥的，也就是只支持refreshAfterWrite
 * 利用时间轮，来进行过期处理。时间轮一个高效的处理定时任务的结构，可以简单的将其看做是一个多维数组。
 * 参考 https://www.cnblogs.com/liujinhua306/p/9808500.html
 * <p>
 * 注：不建议使用该方式来控制分布式缓存的一致性
 * 1、只能控制refreshAfterWrite策略下的一致性
 * 2、Expiry有一个缺陷，验证下来发现，如果使用CustomExpiry，获取过期缓存项时会阻塞所有线程，未使用到refreshAfterWrite的特性
 * <p>
 * 采用定时刷新的方式
 *
 * @author chenck
 * @date 2020/5/11 17:17
 */
@Deprecated
public class CustomExpiry implements Expiry<Object, Object> {

    private final Logger logger = LoggerFactory.getLogger(CustomExpiry.class);
    private RedisTemplate<Object, Object> redisTemplate;
    private CaffeineRedisCacheProperties caffeineRedisCacheProperties;

    private String name;
    /**
     * 过期时间(ms)
     */
    private long expireTime = 0L;

    public CustomExpiry(RedisTemplate<Object, Object> redisTemplate, CaffeineRedisCacheProperties caffeineRedisCacheProperties,
                        String name, long expireTime) {
        this.redisTemplate = redisTemplate;
        this.caffeineRedisCacheProperties = caffeineRedisCacheProperties;
        this.name = name;
        this.expireTime = expireTime;
    }

    private String format(long nanoSeconds) {
        return DateUtils.format(new Date(TimeUnit.NANOSECONDS.toMillis(nanoSeconds)), "yyyy-MM-dd HH:mm:ss.SSS");
    }

    // 返回创建后的过期时间
    @Override
    public long expireAfterCreate(@NonNull Object key, @NonNull Object value, long currentTime) {
        Object redisKey = caffeineRedisCacheProperties.getRedis().getRedisKey(name, key);
        Long milliseconds = redisTemplate.opsForValue().getOperations().getExpire(redisKey, TimeUnit.MILLISECONDS);
        // 当nanoSeconds==null时 会在事务或管道时
        // 返回值为-1时 此键值没有设置过期日期
        // 返回值为-2时 不存在此键
        if (null == milliseconds || -1 == milliseconds || -2 == milliseconds) {
            return expireTime;
        }
        logger.debug("[CustomExpiry] expireAfterCreate key={}, currentTime={}, milliseconds={}", key, format(currentTime), milliseconds);

        return TimeUnit.MILLISECONDS.toNanos(milliseconds);
    }

    // 返回更新后的过期时间
    @Override
    public long expireAfterUpdate(@NonNull Object key, @NonNull Object value, long currentTime, @NonNegative long currentDuration) {
        logger.debug("[CustomExpiry] expireAfterUpdate key={}, currentTime={}, currentDuration={}", key,
                format(currentTime), format(currentDuration));
        return currentDuration;
    }

    // 返回读取后的过期时间
    @Override
    public long expireAfterRead(@NonNull Object key, @NonNull Object value, long currentTime, @NonNegative long currentDuration) {
        // 返回currentDuration，则不修改缓存的过期时间
        // The {@code currentDuration} may be returned to not modify the expiration time
        return currentDuration;
    }
}
