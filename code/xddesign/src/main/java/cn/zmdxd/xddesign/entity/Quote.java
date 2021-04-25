package cn.zmdxd.xddesign.entity;

import com.alibaba.excel.annotation.ExcelIgnore;
import com.alibaba.excel.annotation.write.style.*;
import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import lombok.Data;

/**
 * @author 曹涛
 * @date 2021/1/28 8:47
 * @description: 方案报价表实体类
 */
@Data
@ContentRowHeight(28)//内容行高
@HeadRowHeight(28)//头行高
//@HeadStyle(wrapped = false, fillPatternType = FillPatternType.SOLID_FOREGROUND, fillForegroundColor = 18, borderTop = BorderStyle.MEDIUM,borderBottom = BorderStyle.MEDIUM,borderLeft = BorderStyle.MEDIUM,borderRight = BorderStyle.MEDIUM)
//@HeadFontStyle(fontHeightInPoints = 20, color = 9)//头字体大小
//@ContentStyle(horizontalAlignment = HorizontalAlignment.CENTER, verticalAlignment = VerticalAlignment.CENTER, borderTop = BorderStyle.MEDIUM,borderBottom = BorderStyle.MEDIUM,borderLeft = BorderStyle.MEDIUM,borderRight = BorderStyle.MEDIUM)
//@ContentFontStyle(fontHeightInPoints = 14, bold = true)//内容字体大小
public class Quote {

    @TableId(value = "quote_id",type = IdType.AUTO)
    @ExcelIgnore//忽略字段
    private Integer quoteId;

    @ExcelIgnore
    private Integer soluId;//方案id

    @TableField(exist = false)
//    @ExcelProperty(value = {"智能家居方案报价表","序号"})
    @ColumnWidth(10)
    private Integer id;//excel序号

//    @ExcelProperty(value = {"智能家居方案报价表","房间名称"})
    @ColumnWidth(20)
    private String roomName;//房间名称

//    @ExcelProperty(value = {"智能家居方案报价表","产品名称"})
    @ColumnWidth(38)
    private String productName;//产品名称

//    @ExcelProperty(value = {"智能家居方案报价表","产品数量/个"})
    @ColumnWidth(18)
    private Integer productNum;//产品数量

//    @ExcelProperty(value = {"智能家居方案报价表","产品单价/元"})
    @ColumnWidth(18)
    private Double price;//产品价格

    @TableField(exist = false)
    @ColumnWidth(18)
    private Double totalPrice;//单个产品合计价格

    @TableField(exist = false)
    @ColumnWidth(18)
    // 省级单个产品合计价格
    private Double provinceTotalPrice;
    @TableField(exist = false)
    @ColumnWidth(18)
    // 市级单个产品合计价格
    private Double cityTotalPrice;
    @TableField(exist = false)
    @ColumnWidth(18)
    // 县级单个产品合计价格
    private Double countyTotalPrice;

    @ExcelIgnore
    private Integer roomId;//房间id

    @ExcelIgnore
    // 产品id
    private Integer productId;

    // 省级代理价
    @ColumnWidth(18)
    @TableField(exist = false)
    private Double provincePrice;
    // 市级代理价
    @ColumnWidth(18)
    @TableField(exist = false)
    private Double cityPrice;
    // 县级代理价
    @ColumnWidth(18)
    @TableField(exist = false)
    private Double countyPrice;

}
