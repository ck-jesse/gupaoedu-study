package com.coy.gupaoedu.study.spring.easyexcel.weeget.data;

import com.alibaba.excel.EasyExcel;
import org.junit.Test;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.List;

/**
 * @author chenck
 * @date 2021/3/25 20:01
 */
public class Part3DataDealTest {

    /**
     * 驳回导致商品价格为0的历史数据处理
     * 等同于是订单未关联快照的历史数据
     */
    @Test
    public void orderNotGoodsOrderData1() throws IOException {

        // 解析原数据（含原始的供货价、最低销售价）
        String fileName = "F:\\03weeget\\07问题排查记录\\20210322 快照bug\\20210325\\订单商品成本价和最新规格成本价不一致（这部分确定）.xlsx";
        EasyExcel.read(fileName, OrderData.class, new OrderDataListener()).sheet().doRead();
        // OrderDataListener.compareSpecSnapshotPrice();// 原数据校验
        // System.out.println(OrderDataListener.getOrderDataMap().keySet());

        // 解析规格信息（作为新增规格的基本数据）
        fileName = "F:\\03weeget\\07问题排查记录\\20210322 快照bug\\20210325\\订单商品成本价和最新规格成本价不一致（这部分确定）规格信息.xlsx";
        EasyExcel.read(fileName, GoodsSpec.class, new GoodsSpecListener()).sheet().doRead();

        OrderDataListener.buildRepairSql();

        List<String> sqlList = OrderDataListener.getSqlList();
        Files.write(Paths.get("F:\\03weeget\\07问题排查记录\\20210322 快照bug\\20210325\\订单商品成本价和最新规格成本价不一致（这部分确定）sqlList.txt"), sqlList);

        List<String> rollbackSqlList = OrderDataListener.getRollbackSqlList();
        Files.write(Paths.get("F:\\03weeget\\07问题排查记录\\20210322 快照bug\\20210325\\订单商品成本价和最新规格成本价不一致（这部分确定）rollbackSqlList.txt"), rollbackSqlList);

    }

    @Test
    public void orderNotGoodsOrderData2() throws IOException {

        // 解析原数据（含原始的供货价、最低销售价）
        String fileName = "F:\\03weeget\\07问题排查记录\\20210322 快照bug\\20210325\\订单商品成本价和最新规格成本价不一致（含有多条历史快照取最新一条）.xlsx";
        EasyExcel.read(fileName, OrderData.class, new OrderDataListener()).sheet().doRead();
        // OrderDataListener.compareSpecSnapshotPrice();// 原数据校验
        // System.out.println(OrderDataListener.getOrderDataMap().keySet());

        // 解析规格信息（作为新增规格的基本数据）
        fileName = "F:\\03weeget\\07问题排查记录\\20210322 快照bug\\20210325\\订单商品成本价和最新规格成本价不一致（含有多条历史快照取最新一条）规格信息.xlsx";
        EasyExcel.read(fileName, GoodsSpec.class, new GoodsSpecListener()).sheet().doRead();

        OrderDataListener.buildRepairSql();

        List<String> sqlList = OrderDataListener.getSqlList();
        Files.write(Paths.get("F:\\03weeget\\07问题排查记录\\20210322 快照bug\\20210325\\订单商品成本价和最新规格成本价不一致（含有多条历史快照取最新一条）sqlList.txt"), sqlList);

        List<String> rollbackSqlList = OrderDataListener.getRollbackSqlList();
        Files.write(Paths.get("F:\\03weeget\\07问题排查记录\\20210322 快照bug\\20210325\\订单商品成本价和最新规格成本价不一致（含有多条历史快照取最新一条）rollbackSqlList.txt"), rollbackSqlList);
    }

    @Test
    public void orderNotGoodsOrderData3() throws IOException {

        // 解析原数据（含原始的供货价、最低销售价）
        String fileName = "F:\\03weeget\\07问题排查记录\\20210322 快照bug\\20210325\\订单商品成本价和最新规格成本价不一致（聚合部分）.xlsx";
        EasyExcel.read(fileName, OrderData.class, new OrderDataListener()).sheet().doRead();
        // OrderDataListener.compareSpecSnapshotPrice();// 原数据校验
        // System.out.println("规格数量=" + OrderDataListener.getOrderDataMap().size());
        // System.out.println(OrderDataListener.getOrderDataMap().keySet());

        // 解析规格信息（作为新增规格的基本数据）
        fileName = "F:\\03weeget\\07问题排查记录\\20210322 快照bug\\20210325\\订单商品成本价和最新规格成本价不一致（聚合部分）规格信息.xlsx";
        EasyExcel.read(fileName, GoodsSpec.class, new GoodsSpecListener()).sheet().doRead();

        OrderDataListener.buildRepairSql();

        List<String> sqlList = OrderDataListener.getSqlList();
        Files.write(Paths.get("F:\\03weeget\\07问题排查记录\\20210322 快照bug\\20210325\\订单商品成本价和最新规格成本价不一致（聚合部分）sqlList.txt"), sqlList);

        List<String> rollbackSqlList = OrderDataListener.getRollbackSqlList();
        Files.write(Paths.get("F:\\03weeget\\07问题排查记录\\20210322 快照bug\\20210325\\订单商品成本价和最新规格成本价不一致（聚合部分）rollbackSqlList.txt"), rollbackSqlList);
    }
}
