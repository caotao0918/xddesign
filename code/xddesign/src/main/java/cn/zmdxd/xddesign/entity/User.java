package cn.zmdxd.xddesign.entity;

import com.baomidou.mybatisplus.annotation.TableField;
import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Data;

import java.sql.Timestamp;

/**
 * @author 曹涛
 * @date 2021/1/17 15:04
 * @description: 用户实体类
 */
@Data
public class User {

    private Integer id;        //主键id
    private String username;   //用户名
    private String mobile;     //手机号，登陆账号
    private String pwd;        //登陆密码
    private Timestamp addTime; //管理员添加此账号时间
    private Timestamp lastTime;//上次登陆时间
    private Boolean delSign;   //删除标志
    @TableField(exist = false)
    private Role role;         //用户角色
    private String reserve1;   //保留字段
    private String reserve2;   //保留字段
    private String reserve3;   //保留字段
    private String reserve4;   //保留字段
    private String reserve5;   //保留字段

    @JsonFormat(timezone = "GMT+8", pattern = "yyyy-MM-dd HH:mm:ss")
    public Timestamp getAddTime() {
        return addTime;
    }

    @JsonFormat(timezone = "GMT+8", pattern = "yyyy-MM-dd HH:mm:ss")
    public Timestamp getLastTime() {
        return lastTime;
    }

}
