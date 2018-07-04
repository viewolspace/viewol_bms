package com.viewol.category.response;

import com.viewol.category.vo.CategoryTreeVO;
import com.viewol.common.BaseResponse;

import java.util.List;

public class CategoryTreeResponse extends BaseResponse {
    private List<CategoryTreeVO> data;

    public List<CategoryTreeVO> getData() {
        return data;
    }

    public void setData(List<CategoryTreeVO> data) {
        this.data = data;
    }
}
