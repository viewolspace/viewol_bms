package com.viewol.exhibitor.response;

import com.viewol.common.BaseResponse;
import com.viewol.exhibitor.vo.ExhibitorCategoryVO;

public class ExhibitorCategoryResponse extends BaseResponse {
    private ExhibitorCategoryVO data;

    public ExhibitorCategoryVO getData() {
        return data;
    }

    public void setData(ExhibitorCategoryVO data) {
        this.data = data;
    }
}
