package cn.zmdxd.xddesign.common.easyexcel;

import com.alibaba.excel.metadata.CellData;
import com.alibaba.excel.metadata.Head;
import com.alibaba.excel.metadata.property.StyleProperty;
import com.alibaba.excel.util.StyleUtil;
import com.alibaba.excel.write.handler.CellWriteHandler;
import com.alibaba.excel.write.metadata.holder.WriteSheetHolder;
import com.alibaba.excel.write.metadata.holder.WriteTableHolder;
import com.alibaba.excel.write.metadata.style.WriteCellStyle;
import com.alibaba.excel.write.metadata.style.WriteFont;
import com.alibaba.excel.write.style.HorizontalCellStyleStrategy;
import org.apache.poi.ss.usermodel.*;

import java.util.List;

import static org.apache.poi.ss.usermodel.BorderStyle.MEDIUM;

/**
 * @author 曹涛
 * @date 2021/2/3 16:54
 * @description:
 */
public class MyWriteHandler2 implements CellWriteHandler {

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
    public void afterCellDispose(WriteSheetHolder writeSheetHolder, WriteTableHolder writeTableHolder, List<CellData> list, Cell cell, Head head, Integer integer, Boolean isHead) {
        Workbook workbook = writeSheetHolder.getSheet().getWorkbook();
        //头策略
//        WriteCellStyle headWriteCellStyle = new WriteCellStyle();
        CellStyle headWriteCellStyle = workbook.createCellStyle();

//        WriteFont headWriteFont = new WriteFont();
        Font headWriteFont = workbook.createFont();
        headWriteFont.setBold(true);// 设置头字体加粗
        headWriteFont.setFontHeightInPoints((short)14);// 设置头字体大小14
        if (cell.getRowIndex() == 1) {
            headWriteFont.setColor(IndexedColors.WHITE.getIndex());
            headWriteFont.setFontHeightInPoints((short) 24);
            headWriteCellStyle.setFillForegroundColor(IndexedColors.DARK_BLUE.getIndex());
        }
        if (cell.getRowIndex() == 5) {
            System.out.println("555555555555555555555");
            headWriteFont.setColor(IndexedColors.WHITE.getIndex());
            headWriteFont.setFontHeightInPoints((short) 18);
            headWriteCellStyle.setFillForegroundColor(IndexedColors.DARK_BLUE.getIndex());
        }
        headWriteCellStyle.setFont(headWriteFont);

        headWriteCellStyle.setFillForegroundColor(IndexedColors.WHITE.getIndex());// 设置头的背景颜色为白色
//        headWriteCellStyle.setFillPatternType(FillPatternType.SOLID_FOREGROUND);
//        headWriteCellStyle.setWrapped(false);
        //设置 水平居中
        headWriteCellStyle.setWrapText(false);
//        headWriteCellStyle.setHorizontalAlignment(HorizontalAlignment.LEFT);
        headWriteCellStyle.setVerticalAlignment(VerticalAlignment.CENTER);
        headWriteCellStyle.setAlignment(HorizontalAlignment.LEFT);
        headWriteCellStyle.setFillPattern(FillPatternType.SOLID_FOREGROUND);
        //设置边框样式
        headWriteCellStyle.setBorderLeft(MEDIUM);
        headWriteCellStyle.setBorderBottom(MEDIUM);
        headWriteCellStyle.setBorderRight(MEDIUM);
        headWriteCellStyle.setBorderTop(MEDIUM);

        cell.setCellStyle(headWriteCellStyle);
    }
}
