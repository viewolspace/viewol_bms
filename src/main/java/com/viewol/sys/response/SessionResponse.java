package com.viewol.sys.response;

import com.viewol.common.BaseResponse;

/**
 * Created by leo on 2017/12/8.
 */
public class SessionResponse extends BaseResponse {
	/**
	 * 1000:您已经在其他地方登录，请重新登录！
	 * 2000:当前用户没有登录
	 * 3000:当前用户没有访问权限
	 * 4000:您已经被踢出，请重新登录！
	 */
	private Integer sessionStatus;

	public Integer getSessionStatus() {
		return sessionStatus;
	}

	public void setSessionStatus(Integer sessionStatus) {
		this.sessionStatus = sessionStatus;
	}
}
