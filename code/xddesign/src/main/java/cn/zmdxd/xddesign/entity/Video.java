package cn.zmdxd.xddesign.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Data;

import java.sql.Timestamp;

/**
 * @author 曹涛
 * @date 2021/1/24 15:26
 * @description:产品视频实体类
 */
@Data
public class Video {

    @TableId(value = "video_id",type = IdType.AUTO)
    private Integer videoId;
    private String videoName;          //视频名称
    private String videoLink;          //视频链接
    private Timestamp videoAddTime;    //视频上传日期
    private String videoDesc;          //视频描述
    private String videoReserve;       //保留字段

    @JsonFormat(timezone = "GMT+8", pattern = "yyyy-MM-dd HH:mm:ss")
    public Timestamp getVideoAddTime() {
        return videoAddTime;
    }

}
