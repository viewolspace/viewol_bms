package com.viewol.exhibition.response;

import com.viewol.common.BaseResponse;
import com.viewol.pojo.ProductIdea;
import com.viewol.pojo.ProductIdeaNew;

public class ProductIdeaResponse extends BaseResponse {
    private ProductIdeaNew data;

    public ProductIdeaNew getData() {
        return data;
    }

    public void setData(ProductIdeaNew data) {
        this.data = data;
    }
}
