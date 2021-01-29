package cn.zmdxd.xddesign.entity;

import lombok.Data;

/**
 * @author 曹涛
 * @date 2021/1/25 15:00
 * @description: 常见问题实体类
 */
@Data
public class Question {

    private Integer id;
    private String question;     //问题
    private String answer;       //回答
    private String keyword;      //关键字
    private String reserve;      //保留字段

}
