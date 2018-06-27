package com.viewol.sys.response;

import com.viewol.common.BaseResponse;
import com.viewol.sys.pojo.SysUser;

import java.util.List;

/**
 * Created by leo on 2017/11/29.
 */
public class UserListResponse extends BaseResponse {

	private List<SysUser> data;
	private int recordsFiltered;
	private int recordsTotal;

	public List<SysUser> getData() {
		return data;
	}

	public void setData(List<SysUser> data) {
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
