package com.viewol.schedule.controller;

import com.viewol.category.vo.CategoryVO;
import com.viewol.common.BaseResponse;
import com.viewol.common.GridBaseResponse;
import com.viewol.schedule.vo.RecommendScheduleVO;
import com.viewol.schedule.vo.ScheduleVO;
import com.viewol.shiro.token.TokenManager;
import com.viewol.sys.interceptor.Repeat;
import com.viewol.sys.log.annotation.MethodLog;
import com.viewol.sys.utils.Constants;
import com.youguu.core.util.PageHolder;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import java.util.Date;

/**
 * 日程(活动)管理
 */
@Controller
@RequestMapping("schedule")
public class ScheduleController {

    /**
     * 日程查询
     * @return
     */
    @RequestMapping(value = "/scheduleList", method = RequestMethod.POST)
    @ResponseBody
    public GridBaseResponse scheduleList(@RequestParam(value = "appId", defaultValue = "-1") int appId,
                                   @RequestParam(value = "page", defaultValue = "1") int page,
                                   @RequestParam(value = "limit", defaultValue = "10") int limit) {

        GridBaseResponse rs = new GridBaseResponse();
        rs.setCode(0);
        rs.setMsg("ok");

        PageHolder<ScheduleVO> pageHolder = new PageHolder<>();
        ScheduleVO vo = new ScheduleVO();
        vo.setCompanyId(1);
        vo.setCompanyName("中科院");
        vo.setTitle("讨论火箭上月球");
        vo.setsTime(new Date());
        vo.seteTime(new Date());
        vo.setcTime(new Date());
        pageHolder.add(vo);
        pageHolder.setTotalCount(1);

        if (null != pageHolder) {
            rs.setData(pageHolder.getList());
            rs.setCount(pageHolder.getTotalCount());
        }

        return rs;
    }


    @RequestMapping(value = "/recommendSchedule", method = RequestMethod.POST)
    @ResponseBody
    @MethodLog(module = Constants.AD, desc = "推荐日程")
    @Repeat
    public BaseResponse recommendSchedule(int id) {

        BaseResponse rs = new BaseResponse();


        return rs;
    }

    @RequestMapping(value = "/reviewSchedule")
    @ResponseBody
    @MethodLog(module = Constants.AD, desc = "审核日程")
    @Repeat
    public BaseResponse reviewSchedule(int id) {
        BaseResponse rs = new BaseResponse();
        rs.setStatus(true);
        rs.setMsg("删除成功");

        return rs;
    }

    /**
     * 已推荐日程查询
     * @return
     */
    @RequestMapping(value = "/recoScheduleList", method = RequestMethod.POST)
    @ResponseBody
    public GridBaseResponse recoScheduleList(@RequestParam(value = "appId", defaultValue = "-1") int appId,
                                         @RequestParam(value = "page", defaultValue = "1") int page,
                                         @RequestParam(value = "limit", defaultValue = "10") int limit) {

        GridBaseResponse rs = new GridBaseResponse();
        rs.setCode(0);
        rs.setMsg("ok");

        PageHolder<RecommendScheduleVO> pageHolder = new PageHolder<>();
        RecommendScheduleVO vo = new RecommendScheduleVO();
        vo.setId(1);
        vo.setScheduleId(1);
        vo.setCompanyName("百度");
        vo.setTitle("大数据未来应用场景");

        pageHolder.add(vo);
        pageHolder.setTotalCount(1);
        if (null != pageHolder) {
            rs.setData(pageHolder.getList());
            rs.setCount(pageHolder.getTotalCount());
        }

        return rs;
    }

    /**
     * 取消推荐日程
     * @param id
     * @return
     */
    @RequestMapping(value = "/unRecommendSchedule", method = RequestMethod.POST)
    @ResponseBody
    @MethodLog(module = Constants.AD, desc = "推荐日程")
    @Repeat
    public BaseResponse unRecommendSchedule(int id) {

        BaseResponse rs = new BaseResponse();


        return rs;
    }

}
