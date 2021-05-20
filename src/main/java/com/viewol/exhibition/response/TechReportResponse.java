package com.viewol.exhibition.response;

import com.viewol.common.BaseResponse;
import com.viewol.pojo.TechReport;

public class TechReportResponse extends BaseResponse {
    private TechReport data;

    public TechReport getData() {
        return data;
    }

    public void setData(TechReport data) {
        this.data = data;
    }
}
