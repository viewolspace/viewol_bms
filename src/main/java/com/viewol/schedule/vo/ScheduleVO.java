package com.viewol.schedule.vo;

import java.util.Date;

public class ScheduleVO {
    private int id;
    private int companyId;//主办方的id 如果是展会主板则为-1
    private int type;//0 主办方 1 展商
    private String companyName;//主办方的名称
    private String title;//主题
    private Date cTime;
    private int status;//0 待审 -1 打回 1 审核通过
    private Date sTime;//活动开始时间
    private Date eTime;//活动结束时间
    private String content;//活动内容
    private String place;//活动地点
    private String erCode;//小程序码
    private Integer bbs;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getCompanyId() {
        return companyId;
    }

    public void setCompanyId(int companyId) {
        this.companyId = companyId;
    }

    public int getType() {
        return type;
    }

    public void setType(int type) {
        this.type = type;
    }

    public String getCompanyName() {
        return companyName;
    }

    public void setCompanyName(String companyName) {
        this.companyName = companyName;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public Date getcTime() {
        return cTime;
    }

    public void setcTime(Date cTime) {
        this.cTime = cTime;
    }

    public int getStatus() {
        return status;
    }

    public void setStatus(int status) {
        this.status = status;
    }

    public Date getsTime() {
        return sTime;
    }

    public void setsTime(Date sTime) {
        this.sTime = sTime;
    }

    public Date geteTime() {
        return eTime;
    }

    public void seteTime(Date eTime) {
        this.eTime = eTime;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public String getPlace() {
        return place;
    }

    public void setPlace(String place) {
        this.place = place;
    }

    public String getErCode() {
        return erCode;
    }

    public void setErCode(String erCode) {
        this.erCode = erCode;
    }

    public Integer getBbs() {
        return bbs;
    }

    public void setBbs(Integer bbs) {
        this.bbs = bbs;
    }
}
