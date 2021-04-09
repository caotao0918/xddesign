package cn.zmdxd.xddesign.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Data;

import java.sql.Timestamp;
import java.util.List;

/**
 * @author 曹涛
 * @date 2021/1/19 9:32
 * @description: 方案实体类
 */
@Data
public class Solutions {

    @TableId(value = "solu_id",type = IdType.AUTO)
    private Integer soluId;
    private String soluName;    //方案名称

    @TableField(exist = false)
    private User design;          //设计人员

    private String state;       //方案状态
    private Timestamp addTime;  //方案设计时间
    private Boolean shareSign;  //是否共享为模板方案
    private String soluDesc;    //方案描述

    @TableField(exist = false)
    private List<Room> roomList;//方案内房间列表

    private String soluReserve1;//保留字段
    private String soluReserve2;
    private String soluReserve3;

    private Integer houseId;    //房子id
    @TableField(exist = false)
    private Integer customerId; //客户id

    @TableField(exist = false)
    private String customerName;// 客户姓名

    @TableField(exist = false)
    private String quoteNum;// 报价单号

    @JsonFormat(timezone = "GMT+8", pattern = "yyyy-MM-dd HH:mm:ss")
    private Timestamp getAddTime() {
        return addTime;
    }


}
