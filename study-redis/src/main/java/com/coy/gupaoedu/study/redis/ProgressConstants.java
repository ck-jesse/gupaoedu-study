package com.coy.gupaoedu.study.redis;

/**
 * @author Yuan.Donghao
 *
 */
public interface ProgressConstants {

    /**
     * 积分同步到redis的时候使用的key
     */
    String INTEGRAL_RECORD_REDIS_KEY_PREFIX = "perfect-center-progress:integral";

    /**
     * 记录积分同步的时间
     */
    String SYNTIME_REDIS_KEY_PREFIX = "perfect-center-progress:syntime";

    /**
     * 两个月之后过期，无法界定两个月的时间，设置为62天
     */
    int REDIS_EXPIRE_SECONDS =  60 * 60 * 24 * 62;

    /**
     * 积分记录的redis hash key的格式， 格式为：前缀:卡号hash
     */
    String INTEGRAL_REDIS_KEY_FORMAT = "%s:%s";

    /**
     * 积分记录的redis field key的格式， 格式为：卡号_年月
     */
    String INTEGRAL_REDIS_FIELD_KEY_FORMAT = "%s_%s";

}
