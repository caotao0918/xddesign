package cn.zmdxd.xddesign.util;

import lombok.Getter;
import lombok.Setter;

import java.io.Serializable;

/**
 * @author 曹涛
 * @date 2021/1/22 10:11
 * @description: 返回结果封装
 */
public class ReturnResult implements Serializable {

    private static final long serialVersionUID = 1L;
    private int status;
    private String msg;
    private Integer id;
    public int getStatus() {
        return status;
    }
    public void setStatus(int status) {
        this.status = status;
    }
    public String getMsg() {
        return msg;
    }
    public void setMsg(String msg) {
        this.msg = msg;
    }
    public Integer getId() {
        return id;
    }
    public void setId(Integer id) {
        this.id = id;
    }
    @Override
    public String toString() {
        return "ReturnResult [status=" + status + ", msg=" + msg + "]";
    }

    public static ReturnResult returnResult(boolean state) {
        ReturnResult result = new ReturnResult();
        if (state) {
            result.setStatus(1);
            result.setMsg("操作成功");
        }else {
            result.setMsg("操作失败");
            result.setStatus(0);
        }
        return result;
    }

    public static ReturnResult returnResult(boolean state, String msg) {
        ReturnResult result = new ReturnResult();
        if (state) {
            result.setStatus(1);
            result.setMsg("操作成功");
        }else {
            result.setMsg(msg);
            result.setStatus(0);
        }
        return result;
    }

}
