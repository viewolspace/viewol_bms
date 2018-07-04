package com.viewol.exhibitor.vo;

import java.util.Date;

public class ExhibitorVO {
    private int id;
    private String name;//展商名称
    private String  shortName; //简称
    private String logo;//展商logo
    private String banner;//展商形象图片
    private String image;//展商图片
    private String place;//展商位置
    private String placeSvg;//展商svg位置
    private Integer productNum;//允许上传产品的数量
    private Integer canApply;//1 允许申请活动  0 不允许
    private Integer isRecommend;//0 非推荐  1 推荐
    private Integer recommendNum;//显示推荐的顺序
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

    public String getShortName() {
        return shortName;
    }

    public void setShortName(String shortName) {
        this.shortName = shortName;
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

    public Integer getProductNum() {
        return productNum;
    }

    public void setProductNum(Integer productNum) {
        this.productNum = productNum;
    }

    public Integer getCanApply() {
        return canApply;
    }

    public void setCanApply(Integer canApply) {
        this.canApply = canApply;
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

    public int getIsSameRecommend() {
        return isSameRecommend;
    }

    public void setIsSameRecommend(int isSameRecommend) {
        this.isSameRecommend = isSameRecommend;
    }
}
