package cn.zmdxd.xddesign.common.easyexcel;

import com.alibaba.excel.metadata.CellData;
import com.alibaba.excel.metadata.Head;
import com.alibaba.excel.util.StyleUtil;
import com.alibaba.excel.write.handler.CellWriteHandler;
import com.alibaba.excel.write.handler.RowWriteHandler;
import com.alibaba.excel.write.handler.SheetWriteHandler;
import com.alibaba.excel.write.handler.WriteHandler;
import com.alibaba.excel.write.metadata.WriteSheet;
import com.alibaba.excel.write.metadata.holder.WriteSheetHolder;
import com.alibaba.excel.write.metadata.holder.WriteTableHolder;
import com.alibaba.excel.write.metadata.holder.WriteWorkbookHolder;
import com.alibaba.excel.write.metadata.style.WriteCellStyle;
import com.alibaba.excel.write.metadata.style.WriteFont;
import org.apache.poi.ss.usermodel.*;

import java.util.List;

import static org.apache.poi.ss.usermodel.BorderStyle.MEDIUM;

/**
 * @author 曹涛
 * @date 2021/2/3 16:54
 * @description:
 */
public class MyWriteHandler implements CellWriteHandler {

    @Override
    public void beforeCellCreate(WriteSheetHolder writeSheetHolder, WriteTableHolder writeTableHolder, Row row, Head head, Integer integer, Integer integer1, Boolean aBoolean) {

    }

    @Override
    public void afterCellCreate(WriteSheetHolder writeSheetHolder, WriteTableHolder writeTableHolder, Cell cell, Head head, Integer integer, Boolean aBoolean) {

    }

    @Override
    public void afterCellDataConverted(WriteSheetHolder writeSheetHolder, WriteTableHolder writeTableHolder, CellData cellData, Cell cell, Head head, Integer integer, Boolean aBoolean) {

    }

    @Override
    public void afterCellDispose(WriteSheetHolder writeSheetHolder, WriteTableHolder writeTableHolder, List<CellData> list, Cell cell, Head head, Integer integer, Boolean aBoolean) {

        Sheet sheet = writeSheetHolder.getSheet();
        Workbook workbook = sheet.getWorkbook();

        //内容策略
        WriteCellStyle contentWriteCellStyle = new WriteCellStyle();
        //设置内容字体
        WriteFont contentWriteFont = new WriteFont();
        contentWriteFont.setBold(true);// 设置内容字体加粗
        contentWriteFont.setFontHeightInPoints((short)14);// 设置内容字体大小
        if (cell.getRowIndex() == 13) {
            contentWriteCellStyle.setFillForegroundColor(IndexedColors.GREY_25_PERCENT.getIndex()); //设置内容背景颜色
            contentWriteCellStyle.setFillPatternType(FillPatternType.SOLID_FOREGROUND);
            contentWriteFont.setColor(IndexedColors.WHITE.getIndex());// 设置内容字体颜色
        }

        contentWriteCellStyle.setWriteFont(contentWriteFont);
        //设置 水平居中
        contentWriteCellStyle.setHorizontalAlignment(HorizontalAlignment.CENTER);
        contentWriteCellStyle.setVerticalAlignment(VerticalAlignment.CENTER);
        //设置边框样式
        contentWriteCellStyle.setBorderLeft(MEDIUM);
        contentWriteCellStyle.setBorderBottom(MEDIUM);
        contentWriteCellStyle.setBorderRight(MEDIUM);
        contentWriteCellStyle.setBorderTop(MEDIUM);

        CellStyle cellStyle = StyleUtil.buildContentCellStyle(workbook, contentWriteCellStyle);
        cell.setCellStyle(cellStyle);

    }
}
