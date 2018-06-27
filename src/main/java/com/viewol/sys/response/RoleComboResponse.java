package com.viewol.sys.response;

import com.alibaba.fastjson.JSONObject;
import com.viewol.common.BaseResponse;

import java.util.List;

/**
 * Created by leo on 2017/12/1.
 */
public class RoleComboResponse extends BaseResponse {

	private List<JSONObject> data;

	public List<JSONObject> getData() {
		return data;
	}

	public void setData(List<JSONObject> data) {
		this.data = data;
	}
}
