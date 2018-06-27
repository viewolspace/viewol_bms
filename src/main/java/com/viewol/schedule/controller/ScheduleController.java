package com.viewol.schedule.controller;

import com.viewol.category.vo.CategoryVO;
import com.viewol.common.BaseResponse;
import com.viewol.common.GridBaseResponse;
import com.viewol.common.UploadResponse;
import com.viewol.shiro.token.TokenManager;
import com.viewol.sys.interceptor.Repeat;
import com.viewol.sys.log.annotation.MethodLog;
import com.viewol.sys.utils.Constants;
import com.youguu.core.pojo.Response;
import com.youguu.core.util.HttpUtil;
import com.youguu.core.util.PageHolder;
import com.youguu.core.zookeeper.pro.ZkPropertiesHelper;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.context.ContextLoader;
import org.springframework.web.context.WebApplicationContext;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.ServletContext;
import java.io.File;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Properties;
import java.util.Random;

/**
 * 日程(活动)管理
 */
@Controller
@RequestMapping("category")
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
        if (TokenManager.getAppId() > 0) {
            appId = TokenManager.getAppId();
        }

        PageHolder<CategoryVO> pageHolder = null;
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
        if (TokenManager.getAppId() > 0) {
            appId = TokenManager.getAppId();
        }

        PageHolder<CategoryVO> pageHolder = null;
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
