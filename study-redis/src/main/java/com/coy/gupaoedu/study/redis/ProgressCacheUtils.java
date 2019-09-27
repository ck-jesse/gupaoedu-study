package com.coy.gupaoedu.study.redis;

import java.util.Date;

import static com.coy.gupaoedu.study.redis.ProgressConstants.INTEGRAL_RECORD_REDIS_KEY_PREFIX;
import static com.coy.gupaoedu.study.redis.ProgressConstants.INTEGRAL_REDIS_KEY_FORMAT;
import static com.coy.gupaoedu.study.redis.ProgressConstants.INTEGRAL_REDIS_FIELD_KEY_FORMAT;

/**
 * 成长中心缓存计算工具类
 *
 * @author wuxie.qj
 * @date 2019/6/25
 */
public class ProgressCacheUtils {

    private static final int DEFAULT_MALL_PV_QUEUE_SIZE = 2048;     // 分配的hash桶数量

    /**
     * 获取商城下单积分缓存hash桶的key
     *
     * @author wuxie
     * @date 2019年6月25日
     * @param cardCode - 会员卡号
     * @return hash桶的key
     */
    public static String getMallPvCacheHashKey(String cardCode){
        // 得到Fibonacci散列后的hash取模值
        long hashVal = getHashKey(cardCode, DEFAULT_MALL_PV_QUEUE_SIZE);

        return String.format(INTEGRAL_REDIS_KEY_FORMAT, INTEGRAL_RECORD_REDIS_KEY_PREFIX, hashVal);

    }

    /**
     * 获取商城下单积分缓存hash桶的field字段key
     *
     * @author wuxie
     * @date 2019年6月25日
     * @param cardCode - 会员卡号
     * @param month - 月份
     * @return hash桶的field字段key
     */
    public static String getMallPvCacheFieldKey(String cardCode, int month){
        return String.format(INTEGRAL_REDIS_FIELD_KEY_FORMAT, cardCode, month);
    }


    private static long getHashKey(Object splitKeyVal, Integer subTableCount) {
        if(splitKeyVal == null){
            throw new RuntimeException("splitKeyVal is null:");
        }
        long hashVal = splitKeyVal.toString().hashCode();

        hashVal = ( hashVal * 2654435769L ) >> 28;
        return Math.abs(hashVal % subTableCount);
    }

    public static void main(String[] args) {
        System.out.println(getMallPvCacheHashKey("14560363"));
        System.out.println(getMallPvCacheFieldKey("14560363", 201907));
//        int monthLast = DateTime.getMonth(DateTime.getLastMonth(new Date()));
//        int monthPre = DateTime.getMonth(DateTime.getLast2Month(new Date()));
//        System.out.println(monthPre);

    }

}
