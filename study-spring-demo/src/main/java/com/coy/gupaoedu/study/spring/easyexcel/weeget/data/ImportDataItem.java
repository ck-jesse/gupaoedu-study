package com.coy.gupaoedu.study.spring.easyexcel.weeget.data;

import com.alibaba.excel.annotation.ExcelProperty;
import lombok.Data;

/**
 * @author chenck
 * @date 2021/3/22 11:43
 */
@Data
public class ImportDataItem {

    private String import_data_item_id;
    private String import_data_id;
    private String add_time;
    private String tip;
    private String status;
    private String row_number;
    private String finish_time;
    private String row_data;

}
