package com.viewol.category.response;

import com.viewol.category.vo.CategoryVO;
import com.viewol.common.BaseResponse;

import java.util.List;

public class CategoryResponse extends BaseResponse {

	private List<CategoryVO> data;

	public List<CategoryVO> getData() {
		return data;
	}

	public void setData(List<CategoryVO> data) {
		this.data = data;
	}
}
