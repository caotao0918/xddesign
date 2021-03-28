package cn.zmdxd.xddesign.entity;

import lombok.Data;

/**
 * @author 曹涛
 * @date 2021/2/7 9:16
 * @description: 模板方案Vo类
 */
@Data
public class TemplateVo {

    private String username;//客户姓名
    private String mobile;//客户电话
    private String address;//客户联系地址
    private Integer typeId;//户型id
    private Integer soluId;//方案id
    private String typeName;// 户型名称


}
