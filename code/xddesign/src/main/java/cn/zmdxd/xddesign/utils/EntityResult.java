package cn.zmdxd.xddesign.utils;

import java.io.Serializable;

public class EntityResult<T> implements Serializable{

	private static final long serialVersionUID = 1L;
	private int status;
	private String msg;
	private T data;
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
	public T getData() {
		return data;
	}
	public void setData(T data) {
		this.data = data;
	}
	@Override
	public String toString() {
		return "UserResult [status=" + status + ", msg=" + msg + ", data=" + data + "]";
	}
	
}
