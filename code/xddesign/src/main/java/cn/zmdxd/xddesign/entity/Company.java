package cn.zmdxd.xddesign.entity;

import lombok.Data;

/**
 * @author 曹涛
 * @date 2021/4/13 14:57
 * @description: 公司信息类
 */
@Data
public class Company {

    private Integer id;
    private String logo;
    private String title;
    private String home;
    private String intro;
    private String contact;

}
