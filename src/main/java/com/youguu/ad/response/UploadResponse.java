package com.youguu.ad.response;

import com.youguu.common.BaseResponse;

public class UploadResponse extends BaseResponse {
    private String imageUrl;

    public String getImageUrl() {
        return imageUrl;
    }

    public void setImageUrl(String imageUrl) {
        this.imageUrl = imageUrl;
    }
}
