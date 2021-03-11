package com.coy.gupaoedu.study.spring.easyexcel.weeget.goods;

import com.alibaba.excel.EasyExcel;
import org.junit.Test;

/**
 * @author chenck
 * @date 2020/10/13 14:33
 */
public class GoodsCacheTest {

    /**
     * 批量刷新库存缓存
     * 已完成
     */
    @Test
    public void getStock01() throws InterruptedException {
        String fileName = "E:\\temp\\goods\\【20210311】H2000更新库存数据.xlsx";
        // 这里 需要指定读用哪个class去读，然后读取第一个sheet 文件流会自动关闭
        EasyExcel.read(fileName, WarehouseSpecData.class, new ReloadStockCacheListener()).sheet().doRead();

        while (true) {
            Thread.sleep(1000);
        }
    }

}
