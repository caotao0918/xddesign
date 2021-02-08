package cn.zmdxd.xddesign.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import lombok.Data;

/**
 * @author 曹涛
 * @date 2021/2/5 15:56
 * @description: 户型实体类
 */
@Data
public class HouseType {

    @TableId(value = "type_id",type = IdType.AUTO)
    private Integer typeId;
    private String typeName;//户型名称
    private Integer typeNum;//户型数量
    private String typeDesc;//户型描述

}
