package cn.zmdxd.xddesign.entity;

import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

import java.util.List;

/**
 * @author 曹涛
 * @date 2021/1/19 9:14
 * @description: 客户实体类
 */
@Data
public class Customer {

    private Integer id;
    private String username;        //客户姓名
    private String mobile;          //手机号，登陆账号
    private String pwd;             //登陆密码
    @TableField("`code`")
    private Integer code;           //客户类别码
    @TableField("`desc`")
    private String desc;            //客户类别-参照@CustomerEnum枚举类
    private String demand;          //客户需求
    private String address;         //客户联系地址
//    private String city;

    private Boolean delSign;        //是否删除(假删除)

    @TableField(exist = false)
    private List<House> houseList; //客户房子列表

    @TableField(exist = false)
    private User design;

    private String cusReserve1;    //保留字段
    private String cusReserve2;

}
