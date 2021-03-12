package cn.zmdxd.xddesign.entity;

import com.alibaba.excel.annotation.ExcelIgnore;
import com.alibaba.excel.annotation.ExcelProperty;
import com.alibaba.excel.annotation.write.style.*;
import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import lombok.Data;
import org.apache.poi.ss.usermodel.BorderStyle;
import org.apache.poi.ss.usermodel.HorizontalAlignment;
import org.apache.poi.ss.usermodel.IndexedColors;
import org.apache.poi.ss.usermodel.VerticalAlignment;

/**
 * @author 曹涛
 * @date 2021/1/28 8:47
 * @description: 方案报价表实体类
 */
@Data
@ContentRowHeight(28)//内容行高
@HeadRowHeight(28)//头行高
@ContentFontStyle(fontHeightInPoints = 14, bold = true)//内容字体大小
@HeadFontStyle(fontHeightInPoints = 18,bold = true)//头字体大小
@HeadStyle(wrapped = false)
@ContentStyle(horizontalAlignment = HorizontalAlignment.CENTER, verticalAlignment = VerticalAlignment.CENTER, borderTop = BorderStyle.MEDIUM,borderBottom = BorderStyle.MEDIUM,borderLeft = BorderStyle.MEDIUM,borderRight = BorderStyle.MEDIUM)
public class Quote {

    @TableId(value = "quote_id",type = IdType.AUTO)
    @ExcelIgnore//忽略字段
    private Integer quoteId;

    @ExcelIgnore
    private Integer soluId;//方案id

    @TableField(exist = false)
    @ExcelProperty(value = {"智能家居方案报价表","序号"})
    @ColumnWidth(10)
    private Integer id;//excel序号

    @ExcelProperty(value = {"智能家居方案报价表","房间名称"})
    @ColumnWidth(20)
    private String roomName;//房间名称

    @ExcelProperty(value = {"智能家居方案报价表","产品名称"})
    @ColumnWidth(32)
    private String productName;//产品名称

    @ExcelProperty(value = {"智能家居方案报价表","产品数量/个"})
    @ColumnWidth(18)
    private Integer productNum;//产品数量

    @ExcelProperty(value = {"智能家居方案报价表","产品单价/元"})
    @ColumnWidth(18)
    private Double price;//产品价格

    @TableField(exist = false)
    @ExcelProperty(value = {"智能家居方案报价表","合计/元"})
    @ColumnWidth(18)
    private Double totalPrice;//单个产品合计价格

    @ExcelIgnore
    private Integer roomId;//房间id

}
