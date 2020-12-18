package com.coy.gupaoedu.study.spring.easyexcel.weeget.coupon;

import com.alibaba.fastjson.JSON;
import com.coy.gupaoedu.study.spring.easyexcel.weeget.JsonResult;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.util.CollectionUtils;
import org.springframework.web.client.RestTemplate;

import java.util.Collection;
import java.util.concurrent.atomic.AtomicLong;

/**
 * @author chenck
 * @date 2020/12/18 19:33
 */
public class UserCouponService {

    private static final Logger LOGGER = LoggerFactory.getLogger(UserCouponService.class);

    private static final String HOST = "https://inspire-gateway-pro.yunhuotong.net/weeget-bullet-coupon-rest";

    private static final RestTemplate restTemplate = new RestTemplate();
    private AtomicLong totalCount;
    private AtomicLong dealCount;
    private AtomicLong refreshCount;

    public UserCouponService(AtomicLong totalCount, AtomicLong dealCount, AtomicLong refreshCount) {
        this.totalCount = totalCount;
        this.dealCount = dealCount;
        this.refreshCount = refreshCount;
    }

    public void reloadUserCouponCache(UserCouponData data) {
        try {
            dealCount.incrementAndGet();
            boolean check = checkUserCouponCache(data);
            if (!check) {
                return;
            }
            refreshStock(data);
        } catch (Exception e) {
            LOGGER.error("total={} deal={} refesh={} userId={}, error={}", totalCount.get(), dealCount.get(), refreshCount.get(), data.getWeixin_user_id(), e.getMessage());
        }
    }

    private boolean checkUserCouponCache(UserCouponData data) {
        String url = HOST + "/couponCache/getUserCouponCache?userId=" + data.getWeixin_user_id();
        LOGGER.info("total={} deal={} refesh={} url={}, data={}", totalCount.get(), dealCount.get(), refreshCount.get(), url, JSON.toJSONString(data));
        JsonResult result = restTemplate.getForObject(url, JsonResult.class);
        if (null == result.getResultData()) {
            LOGGER.info("total={} deal={} refesh={} 无需刷新 缓存数据为空 userId={}, url={}, result={}", totalCount.get(), dealCount.get(), refreshCount.get(), data.getWeixin_user_id(), url, JSON.toJSONString(result));
            return false;
        }
        if (result.getResultData() instanceof Collection) {
            if (CollectionUtils.isEmpty((Collection<?>) result.getResultData())) {
                LOGGER.info("total={} deal={} refesh={} 无需刷新 缓存数据为空 userId={}, url={}, result={}", totalCount.get(), dealCount.get(), refreshCount.get(), data.getWeixin_user_id(), url, JSON.toJSONString(result));
                return false;
            }
        }
        return true;
    }

    private void refreshStock(UserCouponData data) {
        String url = HOST + "/couponCache/reloadUserCouponCache?userId=" + data.getWeixin_user_id();
        try {
            refreshCount.incrementAndGet();
            LOGGER.info("total={} deal={} refesh={} refreshStock userId={}, url={}", totalCount.get(), dealCount.get(), refreshCount.get(), data.getWeixin_user_id(), url);
            JsonResult result = restTemplate.getForObject(url, JsonResult.class);
            LOGGER.info("total={} deal={} refesh={} refreshStockSucc userId={}, url={}, result={}", totalCount.get(), dealCount.get(), refreshCount.get(), data.getWeixin_user_id(), url, JSON.toJSONString(result));
        } catch (Exception e) {
            LOGGER.error("total={} deal={} refesh={} refreshStockError userId={}, url={}, error={}", totalCount.get(), dealCount.get(), refreshCount.get(), data.getWeixin_user_id(), url, e.getMessage());
        }
    }
}
