package com.youguu.sys.response;

import com.alibaba.fastjson.JSONArray;
import com.youguu.common.BaseResponse;

/**
 * Created by leo on 2017/11/30.
 */
public class MenuPermissionResponse extends BaseResponse {

	private JSONArray data;

	public JSONArray getData() {
		return data;
	}

	public void setData(JSONArray data) {
		this.data = data;
	}
}
