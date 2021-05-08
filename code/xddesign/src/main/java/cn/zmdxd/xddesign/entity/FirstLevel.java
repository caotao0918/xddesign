package cn.zmdxd.xddesign.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

import java.util.List;

/**
 * @author 曹涛
 * @date 2021/1/22 16:54
 * @description: 产品一级分类实体类
 */
@Data
@TableName("t_firstlevel")
public class FirstLevel {

    @TableId(value = "first_id",type = IdType.AUTO)
    private Integer firstId;
    // 一级分类名称
    private String firstName;
    // 描述
    private String firstDesc;

    // 二级分类列表
    @TableField(exist = false)
    private List<SecondLevel> secondLevelList;

    @Override
    public String toString() {
        return "{" +
                "firstId:" + firstId +
                ", firstName:'" + firstName + '\'' +
                ", firstDesc:'" + firstDesc + '\'' +
                '}';
    }

}
