package com.viewol.schedule.vo;

import java.util.Date;

/**
 * 已推荐日程
 */
public class RecommendScheduleVO {
    private int id;//推荐ID
    private int scheduleId;//日程ID
    private int type;//推荐类型（1 置顶活动  2 推荐活动）
    private Date sTime;//推荐位开始时间
    private Date eTime;//推荐位结束时间
    private Date cTime;//创建时间

    //扩展字段
    private String companyName;//主办方的名称
    private String title;//主题
    private Date startTime;//活动开始时间
    private Date endTime;//活动结束时间

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getScheduleId() {
        return scheduleId;
    }

    public void setScheduleId(int scheduleId) {
        this.scheduleId = scheduleId;
    }

    public int getType() {
        return type;
    }

    public void setType(int type) {
        this.type = type;
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

    public Date getcTime() {
        return cTime;
    }

    public void setcTime(Date cTime) {
        this.cTime = cTime;
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

    public Date getStartTime() {
        return startTime;
    }

    public void setStartTime(Date startTime) {
        this.startTime = startTime;
    }

    public Date getEndTime() {
        return endTime;
    }

    public void setEndTime(Date endTime) {
        this.endTime = endTime;
    }
}
