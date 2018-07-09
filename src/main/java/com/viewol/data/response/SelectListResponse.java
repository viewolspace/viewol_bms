package com.viewol.data.response;

import com.viewol.common.BaseResponse;
import com.viewol.data.vo.Option;

import java.util.List;

public class SelectListResponse extends BaseResponse {
	private List<Option> data;

	public List<Option> getData() {
		return data;
	}

	public void setData(List<Option> data) {
		this.data = data;
	}
}
