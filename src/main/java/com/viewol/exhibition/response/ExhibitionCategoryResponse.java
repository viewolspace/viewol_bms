package com.viewol.exhibition.response;

import com.viewol.common.BaseResponse;
import com.viewol.exhibition.vo.ExhibitionCategoryVO;

public class ExhibitionCategoryResponse extends BaseResponse {
    private ExhibitionCategoryVO data;

    public ExhibitionCategoryVO getData() {
        return data;
    }

    public void setData(ExhibitionCategoryVO data) {
        this.data = data;
    }
}
