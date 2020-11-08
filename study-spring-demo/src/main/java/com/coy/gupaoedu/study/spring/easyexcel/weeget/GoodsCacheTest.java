package com.coy.gupaoedu.study.spring.easyexcel.weeget;

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
    public void getStock01() {
        String fileName = "F:/temp/库存检查.xlsx";
        // 这里 需要指定读用哪个class去读，然后读取第一个sheet 文件流会自动关闭
        EasyExcel.read(fileName, WarehouseSpecData.class, new ReloadStockCacheListener()).sheet().doRead();
    }

    @Test
    public void getStock02() {
        String fileName = "F:/temp/ck10131445-022.xls";
        // 这里 需要指定读用哪个class去读，然后读取第一个sheet 文件流会自动关闭
        EasyExcel.read(fileName, WarehouseSpecData.class, new ReloadStockCacheListener()).sheet().doRead();
    }

    @Test
    public void getStock03() {
        String fileName = "F:/temp/ck10131445-033.xls";
        // 这里 需要指定读用哪个class去读，然后读取第一个sheet 文件流会自动关闭
        EasyExcel.read(fileName, WarehouseSpecData.class, new ReloadStockCacheListener()).sheet().doRead();
    }

    @Test
    public void getStock04() {
        String fileName = "F:/temp/ck10131445-044.xls";
        // 这里 需要指定读用哪个class去读，然后读取第一个sheet 文件流会自动关闭
        EasyExcel.read(fileName, WarehouseSpecData.class, new ReloadStockCacheListener()).sheet().doRead();
    }

    @Test
    public void getStock05() {
        String fileName = "F:/temp/ck10131445-055.xls";
        // 这里 需要指定读用哪个class去读，然后读取第一个sheet 文件流会自动关闭
        EasyExcel.read(fileName, WarehouseSpecData.class, new ReloadStockCacheListener()).sheet().doRead();
    }

    @Test
    public void getStock06() {
        String fileName = "F:/temp/ck10131445-06.xls";
        // 这里 需要指定读用哪个class去读，然后读取第一个sheet 文件流会自动关闭
        EasyExcel.read(fileName, WarehouseSpecData.class, new ReloadStockCacheListener()).sheet().doRead();
    }
}
