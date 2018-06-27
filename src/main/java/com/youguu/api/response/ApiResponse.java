package com.youguu.api.response;

import com.youguu.api.pojo.SysApi;
import com.youguu.common.BaseResponse;

public class ApiResponse extends BaseResponse {

    private SysApi data;

    public SysApi getData() {
        return data;
    }

    public void setData(SysApi data) {
        this.data = data;
    }
}
