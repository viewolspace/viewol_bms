package com.youguu.api.response;

import com.youguu.api.pojo.SysApiTemplate;
import com.youguu.common.BaseResponse;

public class TemplateResponse extends BaseResponse {

    private SysApiTemplate data;

    public SysApiTemplate getData() {
        return data;
    }

    public void setData(SysApiTemplate data) {
        this.data = data;
    }
}
