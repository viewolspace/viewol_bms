package com.youguu.common;

/**
 * Created by leo on 2017/11/29.
 */
public class BaseResponse {
	/**
	 * 请求是否成功
	 */
	private boolean status;

	/**
	 * 吐丝提示
	 */
	private String msg;

	public boolean isStatus() {
		return status;
	}

	public void setStatus(boolean status) {
		this.status = status;
	}

	public String getMsg() {
		return msg;
	}

	public void setMsg(String msg) {
		this.msg = msg;
	}

}
