package com.youguu.api.response;

import com.youguu.api.pojo.ResourceVO;
import com.youguu.common.BaseResponse;

import java.util.List;

public class AllResourceResponse extends BaseResponse {

	private List<ResourceVO> data;

	public List<ResourceVO> getData() {
		return data;
	}

	public void setData(List<ResourceVO> data) {
		this.data = data;
	}
}
