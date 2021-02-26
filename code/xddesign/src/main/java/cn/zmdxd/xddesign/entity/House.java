package cn.zmdxd.xddesign.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

import java.util.List;

/**
 * @author 曹涛
 * @date 2021/1/19 9:31
 * @description: 客户房子实体类
 */
@Data
public class House {

    @TableId(value = "house_id",type = IdType.AUTO)
    private Integer houseId;
    private String houseName;               //房子名称
    private String houseAddress;            //房子地址

    @TableField(exist = false)
    private HouseType houseType;               //房子户型（两室一厅/三室两厅）

    @TableField(exist = false)
    private List<Solutions> solutionsList;  //方案列表

    private String houseReserve1;                   //保留字段
    private String houseReserve2;
    private String houseReserve3;

    private Integer customerId;//客户id
    //这里是我没考虑周到，开始只定义一个 customerId, 后来才发现只定义个客户id不够，但是我又不想改代码了，于是我又增加个客户实体类字段
    @TableField(exist = false)
    private Customer customer;

}
