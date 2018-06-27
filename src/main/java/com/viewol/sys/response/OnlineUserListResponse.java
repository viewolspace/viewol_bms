package com.viewol.sys.response;

import com.viewol.common.BaseResponse;
import com.viewol.sys.pojo.OnlineSysUser;

import java.util.List;

/**
 * Created by leo on 2017/11/29.
 */
public class OnlineUserListResponse extends BaseResponse {

	private List<OnlineSysUser> data;
	private int recordsFiltered;
	private int recordsTotal;

	public List<OnlineSysUser> getData() {
		return data;
	}

	public void setData(List<OnlineSysUser> data) {
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
