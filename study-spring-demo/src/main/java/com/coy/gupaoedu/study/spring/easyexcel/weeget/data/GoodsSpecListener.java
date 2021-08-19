package com.coy.gupaoedu.study.spring.easyexcel.weeget.data;

import com.alibaba.excel.context.AnalysisContext;
import com.alibaba.excel.event.AnalysisEventListener;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.atomic.AtomicLong;
import java.util.stream.Collectors;

/**
 * @author chenck
 * @date 2021/3/22 12:34
 */
public class GoodsSpecListener extends AnalysisEventListener<GoodsSpec> {

    /**
     * 改价规格id列表，同时有下单的规格
     */
    private static final List<GoodsSpec> goodsSpecIdList = new ArrayList<>();
    private static final Map<Integer, GoodsSpec> goodsSpecMap = new HashMap<>();

    private static final Logger LOGGER = LoggerFactory.getLogger(GoodsSpecListener.class);
    private AtomicLong totalCount = new AtomicLong();// 总数
    private AtomicLong dealCount = new AtomicLong();// 处理数量

    @Override
    public void invoke(GoodsSpec goodsSpec, AnalysisContext analysisContext) {
        totalCount.incrementAndGet();
        goodsSpecIdList.add(goodsSpec);
    }

    @Override
    public void doAfterAllAnalysed(AnalysisContext analysisContext) {
        LOGGER.info("所有数据解析完成！ totalCount={}", totalCount.get());
    }

    public static List<GoodsSpec> getGoodsSpecIdList() {
        return goodsSpecIdList;
    }

    public static Map<Integer, GoodsSpec> getGoodsSpecMap() {
        if (goodsSpecMap.size() > 0) {
            return goodsSpecMap;
        }
        // 等于是去重了
        Map<Integer, GoodsSpec> goodsSpecMap1 = goodsSpecIdList.stream().collect(Collectors.toMap(goodsSpec -> goodsSpec.getGoods_spec_id(), goodsSpecSnapshot -> goodsSpecSnapshot));
        goodsSpecMap.putAll(goodsSpecMap1);
        return goodsSpecMap;
    }
}
