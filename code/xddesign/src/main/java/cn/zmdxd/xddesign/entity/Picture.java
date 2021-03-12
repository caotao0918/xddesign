package cn.zmdxd.xddesign.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.Data;

import java.sql.Timestamp;

/**
 * @author 曹涛
 * @date 2021/1/27 9:20
 * @description: 产品图片实体类
 */
@Data
public class Picture {

    @TableId(value = "picture_id", type = IdType.AUTO)
    private Integer pictureId;
    private String pictureName;//图片名称
    private String pictureLink;//图片链接
    private Timestamp pictureAddTime;//图片上传日期
    private String pictureDesc;//图片描述

    private Boolean defaultPicture;//是否为默认图片

    private String pictureReserve;//保留字段

    @JsonFormat(timezone = "GMT+8", pattern = "yyyy-MM-dd HH:mm:ss")
    public Timestamp getPictureAddTime() {
        return pictureAddTime;
    }

}
