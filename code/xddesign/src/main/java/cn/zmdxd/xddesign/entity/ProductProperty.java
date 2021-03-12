package cn.zmdxd.xddesign.entity;

import com.baomidou.mybatisplus.annotation.TableName;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.Data;

/**
 * @author 曹涛
 * @date 2021/1/27 9:07
 * @description: 产品-属性值实体类
 */
@Data
@TableName("t_product_property")
public class ProductProperty {

    private Integer id;

    private PropertyValue value;//对应属性值

}
