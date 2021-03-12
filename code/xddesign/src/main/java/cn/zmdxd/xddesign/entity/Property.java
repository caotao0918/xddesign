package cn.zmdxd.xddesign.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.Data;

import java.util.Arrays;
import java.util.List;

/**
 * @author 曹涛
 * @date 2021/1/26 9:27
 * @description: 产品属性实体类
 */
@Data
@JsonIgnoreProperties({"commonValueList"})
public class Property {

    @TableId(value = "property_id", type = IdType.AUTO)
    private Integer propertyId;
    private String propertyName;          //产品属性名称
    private String propertyDesc;          //产品属性描述
    private String commonValue;     //产品属性常用值   例如：属性名称是通讯方式，描述是产品连接手机的方式，常用值是WiFi、Zigbee

    @TableField(exist = false)
    private List<String> commonValueList;//封装常用值到list

    @TableField(exist = false)
    private SecondLevel secondLevel;
    @TableField(exist = false)
    private Integer firstId;

    public List<String> getCommonValueList() {
        String[] split = getCommonValue().split("，");
        return Arrays.asList(split);
    }

}
