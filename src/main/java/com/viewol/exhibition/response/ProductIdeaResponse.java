package com.viewol.exhibition.response;

import com.viewol.common.BaseResponse;
import com.viewol.pojo.ProductIdea;

public class ProductIdeaResponse extends BaseResponse {
    private ProductIdea data;

    public ProductIdea getData() {
        return data;
    }

    public void setData(ProductIdea data) {
        this.data = data;
    }
}
