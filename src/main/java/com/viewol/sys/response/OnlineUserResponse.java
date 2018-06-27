package com.viewol.sys.response;

import com.viewol.common.BaseResponse;
import com.viewol.sys.pojo.OnlineSysUser;

/**
 * Created by leo on 2017/11/29.
 */
public class OnlineUserResponse extends BaseResponse {

	private OnlineSysUser data;
	private int recordsFiltered;
	private int recordsTotal;

	public OnlineSysUser getData() {
		return data;
	}

	public void setData(OnlineSysUser data) {
		this.data = data;
	}

	public int getRecordsFiltered() {
		return recordsFiltered;
	}

	public void setRecordsFiltered(int recordsFiltered) {
		this.recordsFiltered = recordsFiltered;
	}

	public int getRecordsTotal() {
		return recordsTotal;
	}

	public void setRecordsTotal(int recordsTotal) {
		this.recordsTotal = recordsTotal;
	}
}
