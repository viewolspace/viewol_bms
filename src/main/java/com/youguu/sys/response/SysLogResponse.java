package com.youguu.sys.response;

import com.youguu.sys.pojo.SysLog;

import java.util.List;

/**
 * Created by leo on 2017/12/18.
 */
public class SysLogResponse {
	private Integer code;
	private String msg;
	private Integer count;
	private List<SysLog> data;

	public Integer getCode() {
		return code;
	}

	public void setCode(Integer code) {
		this.code = code;
	}

	public String getMsg() {
		return msg;
	}

	public void setMsg(String msg) {
		this.msg = msg;
	}

	public Integer getCount() {
		return count;
	}

	public void setCount(Integer count) {
		this.count = count;
	}

	public List<SysLog> getData() {
		return data;
	}

	public void setData(List<SysLog> data) {
		this.data = data;
	}
}
