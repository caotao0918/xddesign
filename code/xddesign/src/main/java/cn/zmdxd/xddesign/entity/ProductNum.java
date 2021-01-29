package cn.zmdxd.xddesign.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

/**
 * @author 曹涛
 * @date 2021/1/22 8:57
 * @description: 房间内产品及产品数量实体类
 */
@Data
@TableName("t_product_num")
public class ProductNum {

    @TableId(value = "pn_id", type = IdType.AUTO)
    private Integer pnId;       //主键id 注意：不是产品id
    private Integer pnNum;      //产品数量

    @TableField(exist = false)
    private Product product;    //对应产品

}
