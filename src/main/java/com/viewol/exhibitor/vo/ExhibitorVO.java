package com.viewol.exhibitor.vo;

import java.util.Date;

public class ExhibitorVO {
    private int id;
    private String name;//展商名称
    private String logo;//展商logo
    private String banner;//展商形象图片
    private String image;//展商图片
    private String place;//展商位置
    private String placeSvg;//展商svg位置
    private int productNum;//允许上传产品的数量
    private int canApply;//1 允许申请活动  0 不允许
    private int isRecommend;//0 非推荐  1 推荐
    private int recommendNum;//显示推荐的顺序
    private Date cTime;
    private Date mTime;


    private int isSameRecommend;//是否同类推荐，0 非推荐  1 推荐

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getLogo() {
        return logo;
    }

    public void setLogo(String logo) {
        this.logo = logo;
    }

    public String getBanner() {
        return banner;
    }

    public void setBanner(String banner) {
        this.banner = banner;
    }

    public String getImage() {
        return image;
    }

    public void setImage(String image) {
        this.image = image;
    }

    public String getPlace() {
        return place;
    }

    public void setPlace(String place) {
        this.place = place;
    }

    public String getPlaceSvg() {
        return placeSvg;
    }

    public void setPlaceSvg(String placeSvg) {
        this.placeSvg = placeSvg;
    }

    public int getProductNum() {
        return productNum;
    }

    public void setProductNum(int productNum) {
        this.productNum = productNum;
    }

    public int getCanApply() {
        return canApply;
    }

    public void setCanApply(int canApply) {
        this.canApply = canApply;
    }

    public int getIsRecommend() {
        return isRecommend;
    }

    public void setIsRecommend(int isRecommend) {
        this.isRecommend = isRecommend;
    }

    public int getRecommendNum() {
        return recommendNum;
    }

    public void setRecommendNum(int recommendNum) {
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

    public int getIsSameRecommend() {
        return isSameRecommend;
    }

    public void setIsSameRecommend(int isSameRecommend) {
        this.isSameRecommend = isSameRecommend;
    }
}
