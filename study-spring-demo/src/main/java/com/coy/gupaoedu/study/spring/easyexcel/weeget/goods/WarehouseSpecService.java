package com.coy.gupaoedu.study.spring.easyexcel.weeget.goods;

import com.alibaba.fastjson.JSON;
import com.coy.gupaoedu.study.spring.easyexcel.weeget.JsonResult;
import com.coy.gupaoedu.study.spring.easyexcel.weeget.coupon.UserCouponService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.client.RestTemplate;

import java.util.concurrent.atomic.AtomicLong;

/**
 * @author chenck
 * @date 2020/12/18 20:37
 */
public class WarehouseSpecService {

    private static final Logger LOGGER = LoggerFactory.getLogger(UserCouponService.class);

    private static final String HOST = "http://inspire-gateway-pro.yunhuotong.net:80/weeget-bullet-goods-rest";

    private static final RestTemplate restTemplate = new RestTemplate();
    private AtomicLong totalCount;
    private AtomicLong dealCount;
    private AtomicLong refreshCount;

    public WarehouseSpecService(AtomicLong totalCount, AtomicLong dealCount, AtomicLong refreshCount) {
        this.totalCount = totalCount;
        this.dealCount = dealCount;
        this.refreshCount = refreshCount;
    }

    public void refreshStock(WarehouseSpecData data) {
        dealCount.incrementAndGet();
        String url = HOST + "/stock/cache/getStock?goodsSpecId=" + data.getGoods_spec_id();
        int dbAvailableStock = data.getStock() - data.getLocking_stock();
        int availableStock = data.getAvailableStock();// 盘点库存
        try {
            int redisStock = getRedisStock(data);
            // 盘点库存数量和redis中库存数量比较
            if (redisStock == availableStock) {
                LOGGER.info("total={} deal={} refesh={} 库存一致 goodsSpecId={}, availableStock={}, redisStock={}, url={}", totalCount.get(), dealCount.get(), refreshCount.get(), data.getGoods_spec_id(), availableStock, redisStock, url);
                return;
            }
            if (redisStock > availableStock) {
                LOGGER.info("total={} deal={} refesh={} 库存不一致 超卖 goodsSpecId={}, availableStock={}, redisStock={}, url={}", totalCount.get(), dealCount.get(), refreshCount.get(), data.getGoods_spec_id(), availableStock, redisStock, url);
                refreshStock(data, availableStock, redisStock, "超卖");
            } else {
                LOGGER.info("total={} deal={} refesh={} 库存不一致 少卖 goodsSpecId={}, availableStock={}, redisStock={}, url={}", totalCount.get(), dealCount.get(), refreshCount.get(), data.getGoods_spec_id(), availableStock, redisStock, url);
                refreshStock(data, availableStock, redisStock, "少卖");
            }
        } catch (Exception e) {
            LOGGER.error("total={} deal={} refesh={} url={}, error={}", totalCount.get(), dealCount.get(), refreshCount.get(), url, e.getMessage());
        }
    }

    private int getRedisStock(WarehouseSpecData data) {
        String url = HOST + "/stock/cache/getStock?goodsSpecId=" + data.getGoods_spec_id();
        LOGGER.info("total={} deal={} refesh={} url={}, data={}", totalCount.get(), dealCount.get(), refreshCount.get(), url, JSON.toJSONString(data));
        JsonResult result = restTemplate.getForObject(url, JsonResult.class);
        int redisStock = (Integer) result.getResultData();
        return redisStock;
    }

    private void refreshStock(WarehouseSpecData data, int availableStock, int redisStockBefore, String remark) {
        String url = HOST + "/stock/cache/refreshStock?goodsSpecId=" + data.getGoods_spec_id();
        try {
            refreshCount.incrementAndGet();
            LOGGER.info("total={} deal={} refesh={} refreshStock {} goodsSpecId={}, availableStock={}, url={}", totalCount.get(), dealCount.get(), refreshCount.get(), remark, data.getGoods_spec_id(), availableStock, url);
            JsonResult result = restTemplate.getForObject(url, JsonResult.class);
            LOGGER.info("total={} deal={} refesh={} refreshStockSucc {} goodsSpecId={}, availableStock={}, redisStockBefore={}, redisStockAfter={}, url={}, result={}", totalCount.get(), dealCount.get(), refreshCount.get(), remark, data.getGoods_spec_id(), availableStock, redisStockBefore, result.getResultData(), url, JSON.toJSONString(result));
        } catch (Exception e) {
            LOGGER.error("total={} deal={} refesh={} refreshStockError url={}, error={}", totalCount.get(), dealCount.get(), refreshCount.get(), url, e.getMessage());
        }
    }

}
