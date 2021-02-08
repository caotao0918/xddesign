package cn.zmdxd.xddesign.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import lombok.Data;

/**
 * @author 曹涛
 * @date 2021/2/5 15:50
 * @description: 模板方案实体类
 */
@Data
public class Template {

    @TableId(value = "temp_id",type = IdType.AUTO)
    private Integer tempId;

    private String tempName;//模板方案名称
    private String tempDesc;//模板方案描述

    @TableField(exist = false)
    private HouseType houseType;//模板对应的户型

    @TableField(exist = false)
    private Solutions solutions;//模板对应的方案详情

    @TableField(exist = false)
    private User design;//模板对应的设计人员

    private String tempReserve1;//保留字段
    private String tempReserve2;//保留字段
    private String tempReserve3;//保留字段

}
