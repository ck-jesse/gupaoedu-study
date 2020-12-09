package com.coy.gupaoedu.study.spring.easyexcel.weeget;

import com.alibaba.excel.annotation.ExcelIgnore;
import com.alibaba.excel.annotation.ExcelProperty;
import com.alibaba.excel.annotation.write.style.ColumnWidth;
import lombok.Data;

import java.util.Date;

/**
 * 仓库规格信息
 *
 * @author chenck
 * @date 2020/10/13 14:39
 */
@Data
public class WarehouseSpecData {

    @ColumnWidth(10)
    @ExcelProperty("warehouse_spec_id")
    private Integer warehouse_spec_id;

    @ColumnWidth(10)
    @ExcelProperty("goods_spec_id")
    private Integer goods_spec_id;

    // 库存
    @ColumnWidth(10)
    @ExcelProperty("stock")
    private Integer stock;

    // 锁定库存
    @ColumnWidth(10)
    @ExcelProperty("locking_stock")
    private Integer locking_stock;

    // 盘点库存
    @ColumnWidth(10)
    @ExcelProperty("available_stock")
    private Integer availableStock;

    @ColumnWidth(10)
    @ExcelProperty("warehouse_id")
    private Integer warehouse_id;

    @ColumnWidth(10)
    @ExcelProperty("state")
    private Integer state;

}