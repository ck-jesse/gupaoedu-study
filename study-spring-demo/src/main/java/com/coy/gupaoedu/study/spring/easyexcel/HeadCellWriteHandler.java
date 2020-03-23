package com.coy.gupaoedu.study.spring.easyexcel;

import com.alibaba.excel.metadata.CellData;
import com.alibaba.excel.metadata.Head;
import com.alibaba.excel.write.handler.CellWriteHandler;
import com.alibaba.excel.write.metadata.holder.WriteSheetHolder;
import com.alibaba.excel.write.metadata.holder.WriteTableHolder;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.List;

/**
 * 自定义拦截器，实现对单元格样式设置
 *
 * @author chenck
 * @date 2020/3/23 17:39
 */
public class HeadCellWriteHandler implements CellWriteHandler {
    private static final Logger LOGGER = LoggerFactory.getLogger(HeadCellWriteHandler.class);

    @Override
    public void beforeCellCreate(WriteSheetHolder writeSheetHolder, WriteTableHolder writeTableHolder, Row row, Head head, Integer columnIndex,
                                 Integer relativeRowIndex, Boolean isHead) {

    }

    @Override
    public void afterCellCreate(WriteSheetHolder writeSheetHolder, WriteTableHolder writeTableHolder, Cell cell, Head head,
                                Integer relativeRowIndex, Boolean isHead) {
    }

    // 在单元格上的所有操作完成后调用
    @Override
    public void afterCellDispose(WriteSheetHolder writeSheetHolder, WriteTableHolder writeTableHolder, List<CellData> cellDataList, Cell cell,
                                 Head head, Integer relativeRowIndex, Boolean isHead) {
        // 这里可以对cell进行任何操作
        if (isHead && cell.getColumnIndex() == 0) {
            LOGGER.info("第{}行，第{}列写入完成。isHead={}", cell.getRowIndex(), cell.getColumnIndex(), isHead);
            Sheet sheet = writeSheetHolder.getSheet();
            sheet.createFreezePane(0, 1, 0, 1); // 只冻结第一行
            // sheet.createFreezePane(1, 1, 1, 1);// 同时冻结第一行 和 冻结第一列
            // sheet.createFreezePane( 1, 0, 1, 0 ); // 只冻结第一列
        }
    }
}
