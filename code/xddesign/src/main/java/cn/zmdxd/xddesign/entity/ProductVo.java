package cn.zmdxd.xddesign.entity;

import lombok.Data;

import java.util.List;
import java.util.Map;

/**
 * @author 曹涛
 * @date 2021/1/27 15:31
 * @description: 产品VO实体类
 */
@Data
public class ProductVo{

    private Integer productId;
    private String productName;//产品名称
    private String productModels;//产品型号
    private String productLink;//产品官网链接
    private Double price;//产品价格
    private String productDesc;//产品描述

    private List<Map<String,Object>> propertyValueList;

    private Double productReserve1;//保留字段
    private Double productReserve2;//保留字段
    private Double productReserve3;//保留字段
    private String productReserve4;//保留字段
    private String productReserve5;//保留字段

    private Integer propertyId;    //属性id
    private String propertyName;//属性名称
    private Integer valueId;//属性值id
    private String valueName;//属性值
    private Integer firstId;//产品一级分类id
    private Integer secondId;//产品二级分类id

    private Integer guideId;//产品手册id
    private Integer videoId;//产品视频id

}
