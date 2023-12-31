package com.coy.gupaoedu.study.spring.easyexcel;

import com.alibaba.excel.annotation.ExcelIgnore;
import com.alibaba.excel.annotation.ExcelProperty;
import com.alibaba.excel.annotation.write.style.ColumnWidth;
import lombok.Data;

import java.util.Date;

/**
 * 基础数据类
 *
 * @author Jiaju Zhuang
 **/
@Data
public class DemoData {

    @ColumnWidth(10)
    @ExcelProperty("字符串标题")
    private String string;

    @ColumnWidth(20)
    @ExcelProperty("日期标题")
    private Date date;

    @ColumnWidth(10)
    @ExcelProperty("数字标题")
    private Double doubleData;

    /**
     * 忽略这个字段
     */
    @ExcelIgnore
    private String ignore;
}