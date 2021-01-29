package cn.zmdxd.xddesign.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import lombok.Data;

/**
 * @author 曹涛
 * @date 2021/1/28 8:47
 * @description: 方案报价表实体类
 */
@Data
public class Quote {

    @TableId(value = "quote_id",type = IdType.AUTO)
    private Integer quoteId;
    private Integer soluId;//方案id
    private String roomName;//房间名称
    private String productName;//产品名称
    private String productConnection;//产品通讯方式
    private Double price;//产品价格

}
