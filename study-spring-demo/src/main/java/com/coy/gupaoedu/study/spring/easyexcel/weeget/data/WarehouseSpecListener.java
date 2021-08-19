package com.coy.gupaoedu.study.spring.easyexcel.weeget.data;

import com.alibaba.excel.context.AnalysisContext;
import com.alibaba.excel.event.AnalysisEventListener;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.concurrent.atomic.AtomicLong;
import java.util.stream.Collectors;

/**
 * @author chenck
 * @date 2021/3/22 12:34
 */
public class WarehouseSpecListener extends AnalysisEventListener<WarehouseSpec> {

    /**
     * 改价规格id列表，同时有下单的规格
     */
    private static final List<WarehouseSpec> warehouseSpecList = new ArrayList<>();

    private static final Logger LOGGER = LoggerFactory.getLogger(WarehouseSpecListener.class);
    private AtomicLong totalCount = new AtomicLong();// 总数
    private AtomicLong dealCount = new AtomicLong();// 处理数量

    @Override
    public void invoke(WarehouseSpec warehouseSpec, AnalysisContext analysisContext) {
        totalCount.incrementAndGet();
        warehouseSpecList.add(warehouseSpec);
    }

    @Override
    public void doAfterAllAnalysed(AnalysisContext analysisContext) {
        LOGGER.info("所有数据解析完成！ totalCount={}", totalCount.get());
    }

    public static List<WarehouseSpec> getWarehouseSpecList() {
        return warehouseSpecList;
    }

    public static Map<Integer, WarehouseSpec> getWarehouseSpecMap() {
        // 等于是去重了
        Map<Integer, WarehouseSpec> map = warehouseSpecList.stream().collect(Collectors.toMap(warehouseSpec -> warehouseSpec.getGoods_spec_id(), warehouseSpec -> warehouseSpec));
        return map;
    }

}
