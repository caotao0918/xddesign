package cn.zmdxd.xddesign.entity;

import com.baomidou.mybatisplus.annotation.TableField;
import lombok.Data;

import java.util.List;

/**
 * @author 曹涛
 * @date 2021/1/17 15:21
 * @description: 角色实体类
 */
@Data
public class Role {

    private Integer id;           //主键id
    @TableField("`name`")
    private String name;          //角色名称
    @TableField("`code`")
    private Integer code;         //角色码
    @TableField("`desc`")
    private String desc;          //角色描述
    private List<Integer> pidList;//资源路径id列表

}
