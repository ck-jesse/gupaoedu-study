package com.coy.gupaoedu.study.spring.easyexcel.weeget;

import com.alibaba.excel.context.AnalysisContext;
import com.alibaba.excel.event.AnalysisEventListener;
import com.alibaba.fastjson.JSON;
import com.coy.gupaoedu.study.spring.easyexcel.DemoDAO;
import com.coy.gupaoedu.study.spring.easyexcel.DemoData;
import org.junit.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.client.RestTemplate;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.atomic.AtomicLong;

/**
 * 有个很重要的点 ReloadStockCacheListener 不能被spring管理，要每次读取excel都要new,然后里面用到spring可以构造方法传进去
 *
 * @author chenck
 * @date 2020/10/13 14:36
 */
public class ReloadStockCacheListener extends AnalysisEventListener<WarehouseSpecData> {

    private static final Logger LOGGER = LoggerFactory.getLogger(ReloadStockCacheListener.class);

    private AtomicLong totalCount = new AtomicLong();
    private AtomicLong refreshCount = new AtomicLong();

    /**
     * 每一条数据解析都会来调用
     *
     * @param context
     */
    @Override
    public void invoke(WarehouseSpecData data, AnalysisContext context) {
        totalCount.incrementAndGet();
        getStock(data);
    }

    /**
     * 所有数据解析完成了 都会来调用
     *
     * @param context
     */
    @Override
    public void doAfterAllAnalysed(AnalysisContext context) {
        LOGGER.info("所有数据解析完成！");
    }

    private static final String HOST = "http://inspire-gateway-pro.yunhuotong.net:80/weeget-bullet-goods-rest";

    RestTemplate restTemplate = new RestTemplate();

    private void getStock(WarehouseSpecData data) {
        String url = HOST + "/stock/cache/getStock?goodsSpecId=" + data.getGoods_spec_id();
        int dbAvailableStock = data.getStock() - data.getLocking_stock();
        int availableStock = data.getAvailableStock();// 盘点库存
        try {
            LOGGER.info("total={} refesh={} url={}, data={}", totalCount.get(), refreshCount.get(), url, JSON.toJSONString(data));
            JsonResult result = restTemplate.getForObject(url, JsonResult.class);
            int redisStock = (Integer) result.getResultData();
            // 盘点库存数量和redis中库存数量比较
            if (redisStock == availableStock) {
                LOGGER.info("total={} refesh={} 库存一致 goodsSpecId={}, availableStock={}, redisStock={}, url={}, result={}", totalCount.get(), refreshCount.get(), data.getGoods_spec_id(), availableStock, redisStock, url, JSON.toJSONString(result));
            } else {
                if (redisStock > availableStock) {
                    LOGGER.info("total={} refesh={} 库存不一致 超卖 goodsSpecId={}, availableStock={}, redisStock={}, url={}", totalCount.get(), refreshCount.get(), data.getGoods_spec_id(), availableStock, redisStock, url);
//                    refreshStock(data, availableStock, redisStock, "超卖");
                } else {
                    LOGGER.info("total={} refesh={} 库存不一致 少卖 goodsSpecId={}, availableStock={}, redisStock={}, url={}", totalCount.get(), refreshCount.get(), data.getGoods_spec_id(), availableStock, redisStock, url);
//                    refreshStock(data, availableStock, redisStock, "少卖");
                }
            }
        } catch (Exception e) {
            LOGGER.error("total={} refesh={} url={}, error={}", totalCount.get(), refreshCount.get(), url, e.getMessage());
        }
    }

    private void refreshStock(WarehouseSpecData data, int availableStock, int redisStockBefore, String remark) {
        String url = HOST + "/stock/cache/refreshStock?goodsSpecId=" + data.getGoods_spec_id();
        try {
            refreshCount.incrementAndGet();
            LOGGER.info("total={} refesh={} refreshStock {} goodsSpecId={}, availableStock={}, url={}", totalCount.get(), refreshCount.get(), remark, data.getGoods_spec_id(), availableStock, url);
            JsonResult result = restTemplate.getForObject(url, JsonResult.class);
            LOGGER.info("total={} refesh={} refreshStockSucc {} goodsSpecId={}, availableStock={}, redisStockBefore={}, redisStockAfter={}, url={}, result={}", totalCount.get(), refreshCount.get(), remark, data.getGoods_spec_id(), availableStock, redisStockBefore, result.getResultData(), url, JSON.toJSONString(result));
        } catch (Exception e) {
            LOGGER.error("total={} refesh={} refreshStockError url={}, error={}", totalCount.get(), refreshCount.get(), url, e.getMessage());
        }
    }

}
