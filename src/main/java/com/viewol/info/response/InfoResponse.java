package com.viewol.info.response;

import com.viewol.common.BaseResponse;
import com.viewol.info.vo.InfoVO;

public class InfoResponse extends BaseResponse {
    private InfoVO data;

    public InfoVO getData() {
        return data;
    }

    public void setData(InfoVO data) {
        this.data = data;
    }
}
