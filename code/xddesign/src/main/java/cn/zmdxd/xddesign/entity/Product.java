package cn.zmdxd.xddesign.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.Data;

import java.util.List;

/**
 * @author 曹涛
 * @date 2021/1/22 9:02
 * @description: 产品实体类
 */
@Data
public class Product {

    @TableId(value = "product_id", type = IdType.AUTO)
    private Integer productId;
    private String productName;//产品名称
    private String productModels;//产品型号
    private String productLink;//产品官网链接
    private Double price;//产品价格
    private String productDesc;//产品描述
    private String productDetail;//产品详情

    @TableField(exist = false)
    private List<Picture> pictureList;//产品对应的图片列表

    @TableField(exist = false)
    private SecondLevel secondLevel;//产品的对应二级分类

    @TableField(exist = false)
    private List<ProductProperty> productPropertyList;//产品对应的属性值列表

    @TableField(exist = false)
    private Guide guide;//产品对应的产品手册

    @TableField(exist = false)
    private Video video;//产品对应的安装联网视频

    // 省级代理价
    private Double provincePrice;
    // 市级代理价
    private Double cityPrice;
    // 县级代理价
    private Double countyPrice;
    private String productReserve4;//保留字段
    private String productReserve5;//保留字段


}
