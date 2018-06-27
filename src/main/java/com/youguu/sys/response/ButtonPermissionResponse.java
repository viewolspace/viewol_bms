package com.youguu.sys.response;

import com.alibaba.fastjson.JSONObject;
import com.youguu.common.BaseResponse;

import java.util.List;

/**
 * 查询某个menu下权限按钮
 * Created by leo on 2017/11/29.
 */
public class ButtonPermissionResponse extends BaseResponse {

	private List<JSONObject> data;

	public List<JSONObject> getData() {
		return data;
	}

	public void setData(List<JSONObject> data) {
		this.data = data;
	}
}
