package cn.zmdxd.xddesign.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.Data;

import java.util.List;

/**
 * @author 曹涛
 * @date 2021/1/22 17:03
 * @description: 产品二级分类实体类
 */
@Data
@TableName("t_secondlevel")
public class SecondLevel {

    @TableId(value = "second_id", type = IdType.AUTO)
    private Integer secondId;
    private String secondName;   //二级分类名称
    private String secondDesc;   //描述

    @TableField(exist = false)
    private FirstLevel firstLevel;//一级分类

    @TableField(exist = false)
    private List<Property> propertyList;//属性列表

    @Override
    public String toString() {
        return "{" +
                "secondId:" + secondId +
                ", secondName:'" + secondName + '\'' +
                ", secondDesc:'" + secondDesc + '\'' +
                ", firstLevel:" + firstLevel +
                ", propertyList:" + propertyList +
                '}';
    }

}
