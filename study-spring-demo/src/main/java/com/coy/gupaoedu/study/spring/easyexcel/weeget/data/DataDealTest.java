package com.coy.gupaoedu.study.spring.easyexcel.weeget.data;

import com.alibaba.excel.EasyExcel;
import org.junit.Test;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.List;

/**
 * @author chenck
 * @date 2021/3/22 11:42
 */
public class DataDealTest {

    /**
     * 未关联快照 的订单的历史数据处理
     */
    @Test
    public void orderNotGoodsSnap() throws InterruptedException {

        // 解析有订单的改价规格数据，用于后续复制生成新的规格
        String fileName = "F:\\03weeget\\07问题排查记录\\20210322 快照bug\\商品改价有订单的规格信息(未关联快照).xlsx";
        EasyExcel.read(fileName, GoodsSpec.class, new GoodsSpecListener()).sheet().doRead();

        fileName = "F:\\03weeget\\07问题排查记录\\20210322 快照bug\\商品快照信息(未关联订单).xlsx";
        EasyExcel.read(fileName, GoodsSpecSnapshot.class, new GoodsSpecSnapshotListener()).sheet().doRead();

        // 解析导入的所有的改价规格信息
        fileName = "F:\\03weeget\\07问题排查记录\\20210322 快照bug\\商品改价导入记录0319.xlsx";
        // 这里 需要指定读用哪个class去读，然后读取第一个sheet 文件流会自动关闭
        EasyExcel.read(fileName, ImportDataItem.class, new OrderNotGoodsSnapDataDealListener()).sheet().doRead();

        while (true) {
            Thread.sleep(2000);
        }
    }


    /**
     * 驳回导致商品价格为0的历史数据处理
     * 等同于是订单未关联快照的历史数据
     */
    @Test
    public void orderNotGoodsSnap0Price() throws InterruptedException, IOException {

        // 拿到规格信息
        String fileName = "F:\\03weeget\\07问题排查记录\\20210322 快照bug\\驳回0元\\驳回商品规格信息.xlsx";
        EasyExcel.read(fileName, GoodsSpec.class, new GoodsSpecListener()).sheet().doRead();

        // 大数据提供的原始数据，取其中的供货价和最低销售价
        fileName = "F:\\03weeget\\07问题排查记录\\20210322 快照bug\\驳回0元\\驳回商品规格价格元数据.xlsx";
        EasyExcel.read(fileName, Price0Data.class, new OrderNotGoodsSnapPrice0DataDealListener()).sheet().doRead();

        List<String> sqlList = OrderNotGoodsSnapPrice0DataDealListener.getSqlList();
        Files.write(Paths.get("F:\\03weeget\\07问题排查记录\\20210322 快照bug\\驳回0元\\sqlListTemp.txt"), sqlList);

        List<String> rollbackSqlList = OrderNotGoodsSnapPrice0DataDealListener.getRollbackSqlList();
        Files.write(Paths.get("F:\\03weeget\\07问题排查记录\\20210322 快照bug\\驳回0元\\rollbackSqlList.txt"), rollbackSqlList);

        while (true) {
            Thread.sleep(2000);
        }
    }

    /**
     * 驳回导致商品价格为0的历史数据处理
     * 等同于是订单未关联快照的历史数据
     */
    @Test
    public void orderNotGoodsSnap0Price1() throws InterruptedException, IOException {

        // 拿到规格信息
        String fileName = "F:\\03weeget\\07问题排查记录\\20210322 快照bug\\20210325\\20210325异常商品规格价格0元-规格信息.xlsx";
        EasyExcel.read(fileName, GoodsSpec.class, new GoodsSpecListener()).sheet().doRead();

        // 大数据提供的原始数据，取其中的供货价和最低销售价
        fileName = "F:\\03weeget\\07问题排查记录\\20210322 快照bug\\20210325\\20210325异常商品规格价格0元.xlsx";
        EasyExcel.read(fileName, Price0Data.class, new OrderNotGoodsSnapPrice0DataDealListener()).sheet().doRead();

        List<String> sqlList = OrderNotGoodsSnapPrice0DataDealListener.getSqlList();
        Files.write(Paths.get("F:\\03weeget\\07问题排查记录\\20210322 快照bug\\20210325\\20210325异常商品规格价格0元-sqlList.txt"), sqlList);

        List<String> rollbackSqlList = OrderNotGoodsSnapPrice0DataDealListener.getRollbackSqlList();
        Files.write(Paths.get("F:\\03weeget\\07问题排查记录\\20210322 快照bug\\20210325\\20210325异常商品规格价格0元-rollbackSqlList.txt"), rollbackSqlList);

    }

    /**
     * 驳回导致商品价格为0的历史数据处理
     * 等同于是订单未关联快照的历史数据
     */
    @Test
    public void orderNotGoodsSnap0Price2() throws InterruptedException, IOException {

        // 拿到规格信息
        String fileName = "F:\\03weeget\\07问题排查记录\\20210322 快照bug\\20210330\\最后一次商品数据修复0元0330规格信息.xlsx";
        EasyExcel.read(fileName, GoodsSpec.class, new GoodsSpecListener()).sheet().doRead();

        // 大数据提供的原始数据，取其中的供货价和最低销售价
        fileName = "F:\\03weeget\\07问题排查记录\\20210322 快照bug\\20210330\\最后一次商品数据修复0元0330.xlsx";
        EasyExcel.read(fileName, Price0Data.class, new OrderNotGoodsSnapPrice0DataDealListener()).sheet().doRead();

        List<String> sqlList = OrderNotGoodsSnapPrice0DataDealListener.getSqlList();
        Files.write(Paths.get("F:\\03weeget\\07问题排查记录\\20210322 快照bug\\20210330\\最后一次商品数据修复0元0330-sqlList.txt"), sqlList);

        List<String> rollbackSqlList = OrderNotGoodsSnapPrice0DataDealListener.getRollbackSqlList();
        Files.write(Paths.get("F:\\03weeget\\07问题排查记录\\20210322 快照bug\\20210330\\最后一次商品数据修复0元0330-rollbackSqlList.txt"), rollbackSqlList);

    }


    /**
     * 已关联快照 的订单的历史数据处理
     * 第一步：
     */
    public void test() {

        String fileName = "F:\\03weeget\\07问题排查记录\\20210322 快照bug\\商品快照信息(已关联订单).xlsx";
        EasyExcel.read(fileName, GoodsSpecSnapshot.class, new GoodsSpecSnapshotListener()).sheet().doRead();

        // 过滤和区分哪些快照需要处理，哪些不需要处理
        // 不需要处理：
        // 1）规格只有一条快照[无需处理]
        // 2）[快照比较][无需处理]供货价和最低销售价一致
        // 3）[快照比较][无需处理]最新的快照无需处理（因为当前已经按照原来的逻辑复制一条新的规格，旧规格状态修改为0，所以无需修改）
        // 需要处理：
        // 1）[快照比较][需要处理]供货价和最低销售价不一致

        // 刷选出来需要处理的规格快照后，在通过SQL统计一把，判断快照id是否有对应的订单，若有，则需要处理，若无，则不需要处理
        GoodsSpecSnapshotListener.dealSnapshot();

    }

    /**
     * 已关联快照 的订单的历史数据处理
     * 第二步：
     */
    @Test
    public void orderHaveGoodsSnap() throws InterruptedException {

        // 解析有订单的改价规格数据，用于后续复制生成新的规格
        String fileName = "F:\\03weeget\\07问题排查记录\\20210322 快照bug\\商品改价有订单的规格信息(已关联快照).xlsx";
        EasyExcel.read(fileName, GoodsSpec.class, new GoodsSpecListener()).sheet().doRead();

        // 规格对应的库存
        fileName = "F:\\03weeget\\07问题排查记录\\20210322 快照bug\\商品改价有订单的规格库存信息(已关联快照).xlsx";
        EasyExcel.read(fileName, WarehouseSpec.class, new WarehouseSpecListener()).sheet().doRead();

        // 这份数据时已经过滤后，需要处理的规格快照信息（有订单的）
        fileName = "F:\\03weeget\\07问题排查记录\\20210322 快照bug\\商品改价有订单的规格快照基本信息(已关联快照).xlsx";
        EasyExcel.read(fileName, GoodsSpecSnapshot.class, new OrderHaveGoodsSnapDataDealListener()).sheet().doRead();

        while (true) {
            Thread.sleep(2000);
        }
    }


}
