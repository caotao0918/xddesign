package cn.zmdxd.xddesign.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

/**
 * @author 曹涛
 * @date 2021/1/26 11:58
 * @description: 产品属性值实体类
 */
@Data
@TableName("t_property_value")
public class PropertyValue {

    @TableId(value = "value_id", type = IdType.AUTO)
    private Integer valueId;
    private String valueName;//属性值

    @TableField(exist = false)
    private Property property;//对应的属性

}
