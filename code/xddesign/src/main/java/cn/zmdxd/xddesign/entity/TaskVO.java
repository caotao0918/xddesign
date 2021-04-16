package cn.zmdxd.xddesign.entity;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Data;

import java.sql.Timestamp;

/**
 * @author 曹涛
 * @date 2021/4/12 14:36
 * @description: 施工列表VO类
 */
@Data
public class TaskVO {

    private Integer soluId;
    private String cusName;
    private String cusMobile;
    private String address;
    private String designName;
    private String designMobile;
    private Timestamp workTime;
    private String soluName;
    private String workerName;

    @JsonFormat(timezone = "GMT+8", pattern = "yyyy-MM-dd HH:mm:ss")
    private Timestamp getWorkTime() {
        return workTime;
    }

}
