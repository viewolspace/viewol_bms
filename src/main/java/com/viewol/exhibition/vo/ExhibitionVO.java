package com.viewol.exhibition.vo;

import java.util.Date;

public class ExhibitionVO {
    private Integer id;
    private Integer companyId;//展商id
    private String categoryId;//分类id
    private Integer status;//0 上架  1 下架
    private String name;//产品名称
    private String image;//产品图片-用于列表展示
    private String regImage;//产品图片-用于首页推荐
    private String content;//产品介绍
    private String pdfUrl;//产品说明书
    private String pdfName;//说明书的名字
    private Integer isRecommend;//0 非推荐  1 推荐
    private Integer recommendNum;//'显示推荐的顺序'
    private Date cTime;
    private Date mTime;
    private int topNum;//置顶顺序

    private Integer isSameRecommend;//是否同类推荐，0 非推荐  1 推荐

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public Integer getCompanyId() {
        return companyId;
    }

    public void setCompanyId(Integer companyId) {
        this.companyId = companyId;
    }

    public String getCategoryId() {
        return categoryId;
    }

    public void setCategoryId(String categoryId) {
        this.categoryId = categoryId;
    }

    public Integer getStatus() {
        return status;
    }

    public void setStatus(Integer status) {
        this.status = status;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getImage() {
        return image;
    }

    public void setImage(String image) {
        this.image = image;
    }

    public String getRegImage() {
        return regImage;
    }

    public void setRegImage(String regImage) {
        this.regImage = regImage;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public String getPdfUrl() {
        return pdfUrl;
    }

    public void setPdfUrl(String pdfUrl) {
        this.pdfUrl = pdfUrl;
    }

    public String getPdfName() {
        return pdfName;
    }

    public void setPdfName(String pdfName) {
        this.pdfName = pdfName;
    }

    public Integer getIsRecommend() {
        return isRecommend;
    }

    public void setIsRecommend(Integer isRecommend) {
        this.isRecommend = isRecommend;
    }

    public Integer getRecommendNum() {
        return recommendNum;
    }

    public void setRecommendNum(Integer recommendNum) {
        this.recommendNum = recommendNum;
    }

    public Date getcTime() {
        return cTime;
    }

    public void setcTime(Date cTime) {
        this.cTime = cTime;
    }

    public Date getmTime() {
        return mTime;
    }

    public void setmTime(Date mTime) {
        this.mTime = mTime;
    }

    public Integer getIsSameRecommend() {
        return isSameRecommend;
    }

    public void setIsSameRecommend(Integer isSameRecommend) {
        this.isSameRecommend = isSameRecommend;
    }

    public int getTopNum() {
        return topNum;
    }

    public void setTopNum(int topNum) {
        this.topNum = topNum;
    }
}
