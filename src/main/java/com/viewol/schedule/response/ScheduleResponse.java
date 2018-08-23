package com.viewol.schedule.response;

import com.viewol.common.BaseResponse;
import com.viewol.schedule.vo.ScheduleVO;

public class ScheduleResponse extends BaseResponse {

    private ScheduleVO data;

    public ScheduleVO getData() {
        return data;
    }

    public void setData(ScheduleVO data) {
        this.data = data;
    }
}
