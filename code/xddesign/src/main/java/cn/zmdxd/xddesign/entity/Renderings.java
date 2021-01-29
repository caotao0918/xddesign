package cn.zmdxd.xddesign.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import lombok.Data;

/**
 * @author 曹涛
 * @date 2021/1/19 9:37
 * @description: 效果图实体类
 */
@Data
public class Renderings {

    @TableId(value = "rend_id", type = IdType.AUTO)
    private Integer rendId;
    private String rendName;//效果图名称
    private String rendPath;//路径
    private String rendDesc;//描述
    private Integer soluId;//方案id

}
