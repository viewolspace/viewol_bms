package com.viewol.exhibition.response;

import com.viewol.common.BaseResponse;
import com.viewol.exhibition.vo.ExhibitionVO;

public class ExhibitionResponse extends BaseResponse {
    private ExhibitionVO data;

    public ExhibitionVO getData() {
        return data;
    }

    public void setData(ExhibitionVO data) {
        this.data = data;
    }
}
