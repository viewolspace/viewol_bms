package com.viewol.common;

import java.io.Serializable;
import java.util.Map;

/**
 * Created by leo on 2017/12/19.
 */
public class GridBaseResponse<T> implements Serializable {
	private Integer code;
	private String msg;
	private T data;
	private Integer count;
	private Map<String, Map<String, Object>> map;

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

	public T getData() {
		return data;
	}

	public void setData(T data) {
		this.data = data;
	}

	public Integer getCount() {
		return count;
	}

	public void setCount(Integer count) {
		this.count = count;
	}

	public Map<String, Map<String, Object>> getMap() {
		return map;
	}

	public void setMap(Map<String, Map<String, Object>> map) {
		this.map = map;
	}
}
