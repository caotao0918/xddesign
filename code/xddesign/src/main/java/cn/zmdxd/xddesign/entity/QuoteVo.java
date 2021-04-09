package cn.zmdxd.xddesign.entity;

import lombok.Data;

/**
 * @author 曹涛
 * @date 2021/4/8 9:11
 * @description: 报价VO类
 */
@Data
public class QuoteVo {

    private String quoteNum;  // 报价单号
    private String addTime;   // 日期
    private String designName; // 设计师名字
    private String designMobile;// 设计师电话
    private String cusName;    // 客户名字
    private String cusMobile;   // 客户电话
    private String descr;        // 备注

}
