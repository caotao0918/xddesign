package cn.zmdxd.xddesign.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import lombok.Data;

import java.util.List;

/**
 * @author 曹涛
 * @date 2021/1/19 9:36
 * @description: 房间实体类
 */
@Data
public class Room {

    @TableId(value = "room_id", type = IdType.AUTO)
    private Integer roomId;                    //房间id
    private String roomName;                   //房间名称
    private String roomDesc;                   //房间描述

    @TableField(exist = false)
    private List<ProductNum> productNumList;   //房间内产品列表

    private String roomReserve1;               //保留字段
    private String roomReserve2;
    private String roomReserve3;

    private Integer soluId;                    //方案id
    @TableField(exist = false)
    private Integer houseId;                   //房子id
    @TableField(exist = false)
    private Integer customerId;                //客户id

}
