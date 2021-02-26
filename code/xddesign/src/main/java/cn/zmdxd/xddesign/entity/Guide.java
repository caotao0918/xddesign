package cn.zmdxd.xddesign.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Data;

import java.sql.Timestamp;

/**
 * @author 曹涛
 * @date 2021/1/25 13:43
 * @description: 产品手册实体类
 */
@Data
public class Guide {

    @TableId(value = "guide_id",type = IdType.AUTO)
    private Integer guideId;
    private String guideName;     //产品手册名称
    private String guideDesc;     //产品手册描述
    private String guideLink;     //产品手册链接
    private Timestamp guideAddTime;//产品手册添加时间
    private String pictureLink;   //产品手册封面图片链接
    private String guideReserve;  //保留字段

    @JsonFormat(timezone = "GMT+8", pattern = "yyyy-MM-dd HH:mm:ss")
    public Timestamp getGuideAddTime() {
        return guideAddTime;
    }

}
