package com.viewol.schedule.controller;

import com.alibaba.fastjson.JSONObject;
import com.viewol.common.BaseResponse;
import com.viewol.common.GridBaseResponse;
import com.viewol.common.LayeditResponse;
import com.viewol.pojo.Schedule;
import com.viewol.pojo.ScheduleUser;
import com.viewol.pojo.query.RecommendScheduleQuery;
import com.viewol.pojo.query.ScheduleQuery;
import com.viewol.schedule.response.ErcodeResponse;
import com.viewol.schedule.response.ScheduleResponse;
import com.viewol.schedule.vo.RecommendScheduleVO;
import com.viewol.schedule.vo.ScheduleUserVO;
import com.viewol.schedule.vo.ScheduleVO;
import com.viewol.service.IScheduleService;
import com.viewol.shiro.token.TokenManager;
import com.viewol.sys.interceptor.Repeat;
import com.viewol.sys.log.annotation.MethodLog;
import com.viewol.sys.utils.Constants;
import com.viewol.sys.utils.HtmlUtil;
import com.youguu.core.pojo.Response;
import com.youguu.core.util.HttpUtil;
import com.youguu.core.util.PageHolder;
import com.youguu.core.util.PropertiesUtil;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.multipart.MultipartFile;

import javax.annotation.Resource;
import java.io.File;
import java.io.IOException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.*;

/**
 * 日程(活动)管理
 */
@Controller
@RequestMapping("schedule")
public class ScheduleController {

    @Resource
    private IScheduleService scheduleService;

    /**
     * 日程查询
     *
     * @return
     */
    @RequestMapping(value = "/scheduleList", method = RequestMethod.POST)
    @ResponseBody
    public GridBaseResponse scheduleList(@RequestParam(value = "time", defaultValue = "") String time,
                                         @RequestParam(value = "companyId", defaultValue = "") Integer companyId,
                                         @RequestParam(value = "type", defaultValue = "") Integer type,
                                         @RequestParam(value = "status", defaultValue = "") Integer status,
                                         @RequestParam(value = "keyword", defaultValue = "") String keyword,
                                         @RequestParam(value = "page", defaultValue = "1") int page,
                                         @RequestParam(value = "limit", defaultValue = "10") int limit) {

        GridBaseResponse rs = new GridBaseResponse();
        rs.setCode(0);
        rs.setMsg("ok");
        ScheduleQuery scheduleQuery = new ScheduleQuery();
        if (null != time && !"".equals(time)) {
            scheduleQuery.setTime(time);
        }
        scheduleQuery.setCompanyId(companyId);
        if(null != type && type!=999){
            scheduleQuery.setType(type);
        }

        if(null != status && status!=999){
            scheduleQuery.setStatus(status);
        }

        scheduleQuery.setKeyword(keyword);
        scheduleQuery.setPageIndex(page);
        scheduleQuery.setPageSize(limit);

        PageHolder<Schedule> pageHolder = scheduleService.querySchedule(scheduleQuery);

        List<ScheduleVO> voList = new ArrayList<>();
        if (null != pageHolder && null != pageHolder.getList() && pageHolder.getList().size() > 0) {
            for (Schedule schedule : pageHolder.getList()) {
                ScheduleVO vo = new ScheduleVO();
                vo.setId(schedule.getId());
                vo.setCompanyId(schedule.getCompanyId());
                vo.setType(schedule.getType());
                vo.setCompanyName(schedule.getCompanyName());
                vo.setTitle(schedule.getTitle());
                vo.setStatus(schedule.getStatus());
                vo.setContent(schedule.getContentView());
                vo.setPlace(schedule.getPlace());
                vo.setsTime(schedule.getsTime());
                vo.seteTime(schedule.geteTime());
                vo.setcTime(schedule.getcTime());
                vo.setErCode(getScheduleMaErCode(schedule.getId(), 100));
                voList.add(vo);
            }

            rs.setData(voList);
            rs.setCount(pageHolder.getTotalCount());
        }

        return rs;
    }


    /**
     * 推荐日程
     *
     * @param type         1-置顶；2-推荐
     * @param id
     * @param scheduleTime 时间范围
     * @return
     */
    @RequestMapping(value = "/recommendSchedule", method = RequestMethod.POST)
    @ResponseBody
    @MethodLog(module = Constants.SCHEDULE, desc = "推荐日程")
    @Repeat
    public BaseResponse recommendSchedule(@RequestParam(value = "type", defaultValue = "-1") int type,
                                          @RequestParam(value = "id", defaultValue = "-1") int id,
                                          @RequestParam(value = "scheduleTime", defaultValue = "") String scheduleTime) {

        BaseResponse rs = new BaseResponse();
        String[] time = scheduleTime.split("到");
        int result = scheduleService.addRecommendSchedule(type, id, time[0], time[1]);

        if (result > 0) {
            rs.setStatus(true);
            rs.setMsg("修改成功");
        } else {
            rs.setStatus(false);
            rs.setMsg("修改失败");
        }

        return rs;
    }

    @RequestMapping(value = "/reviewSchedule")
    @ResponseBody
    @MethodLog(module = Constants.SCHEDULE, desc = "审核日程")
    @Repeat
    public BaseResponse reviewSchedule(@RequestParam(value = "id", defaultValue = "-1") int id,
                                       @RequestParam(value = "status", defaultValue = "-1") int status) {
        BaseResponse rs = new BaseResponse();

        int result = scheduleService.updateStatus(id, status);

        if (result > 0) {
            rs.setStatus(true);
            rs.setMsg("修改成功");
        } else {
            rs.setStatus(false);
            rs.setMsg("修改失败");
        }

        return rs;
    }

    /**
     * 已推荐日程查询
     *
     * @return
     */
    @RequestMapping(value = "/recoScheduleList", method = RequestMethod.POST)
    @ResponseBody
    public GridBaseResponse recoScheduleList(@RequestParam(value = "time", defaultValue = "") String time,
                                             @RequestParam(value = "type", defaultValue = "") Integer type,
                                             @RequestParam(value = "page", defaultValue = "1") int page,
                                             @RequestParam(value = "limit", defaultValue = "10") int limit) {

        GridBaseResponse rs = new GridBaseResponse();
        rs.setCode(0);
        rs.setMsg("ok");
        RecommendScheduleQuery query = new RecommendScheduleQuery();
        if (null != time && !"".equals(time)) {
            query.setTime(time);
        }
        if(null != type && type != 999){
            query.setType(type);
        }

        query.setPageIndex(page);
        query.setPageSize(limit);

        PageHolder<com.viewol.pojo.ScheduleVO> pageHolder = scheduleService.queryRecommendSchedule(query);
        List<RecommendScheduleVO> voList = new ArrayList<>();

        if (null != pageHolder && null != pageHolder.getList() && pageHolder.getList().size() > 0) {
            for (com.viewol.pojo.ScheduleVO schedule : pageHolder.getList()) {
                RecommendScheduleVO vo = new RecommendScheduleVO();
                vo.setId(schedule.getRecommendScheduleId());
                vo.setScheduleId(schedule.getId());
                vo.setType(schedule.getvType());
                vo.setsTime(schedule.getRecommendSTime());
                vo.seteTime(schedule.getRecommendETime());
                vo.setcTime(schedule.getcTime());

                vo.setCompanyName(schedule.getCompanyName());
                vo.setTitle(schedule.getTitle());
                vo.setStartTime(schedule.getsTime());
                vo.setEndTime(schedule.geteTime());
                voList.add(vo);
            }

            rs.setData(voList);
            rs.setCount(pageHolder.getTotalCount());
        }

        return rs;
    }

    /**
     * 取消推荐日程
     *
     * @param id
     * @return
     */
    @RequestMapping(value = "/unRecommendSchedule", method = RequestMethod.POST)
    @ResponseBody
    @MethodLog(module = Constants.SCHEDULE, desc = "取消推荐日程")
    @Repeat
    public BaseResponse unRecommendSchedule(int id) {

        BaseResponse rs = new BaseResponse();
        int result = scheduleService.delRecommendSchedule(id);

        if (result > 0) {
            rs.setStatus(true);
            rs.setMsg("取消推荐成功");
        } else {
            rs.setStatus(false);
            rs.setMsg("取消推荐失败");
        }

        return rs;
    }

    @RequestMapping(value = "/addSchedule", method = RequestMethod.POST)
    @ResponseBody
    @MethodLog(module = Constants.SCHEDULE, desc = "添加日程")
    @Repeat
    public BaseResponse addSchedule(@RequestParam(value = "title", defaultValue = "") String title,
                                    @RequestParam(value = "sTime", defaultValue = "") String sTime,
                                    @RequestParam(value = "eTime", defaultValue = "") String eTime,
                                    @RequestParam(value = "content", defaultValue = "") String content,
                                    @RequestParam(value = "place", defaultValue = "") String place) {

        BaseResponse rs = new BaseResponse();

        int result = scheduleService.addSchedule(title, place, HtmlUtil.stringFilter(content), sTime, eTime);
        if (result > 0) {
            rs.setStatus(true);
            rs.setMsg("添加成功");
        } else {
            rs.setStatus(false);
            rs.setMsg("添加失败");
        }

        return rs;
    }

    @RequestMapping(value = "/updateSchedule", method = RequestMethod.POST)
    @ResponseBody
    @MethodLog(module = Constants.SCHEDULE, desc = "修改日程")
    @Repeat
    public BaseResponse updateSchedule(@RequestParam(value = "id", defaultValue = "-1") int id,
                                       @RequestParam(value = "title", defaultValue = "") String title,
                                       @RequestParam(value = "sTime", defaultValue = "") String sTime,
                                       @RequestParam(value = "eTime", defaultValue = "") String eTime,
                                       @RequestParam(value = "content", defaultValue = "") String content,
                                       @RequestParam(value = "place", defaultValue = "") String place) {

        BaseResponse rs = new BaseResponse();

        Schedule schedule = scheduleService.getSchedule(id);
        schedule.setTitle(title);
        SimpleDateFormat dft = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        try {
            schedule.setsTime(dft.parse(sTime));
            schedule.seteTime(dft.parse(eTime));
        } catch (ParseException e) {
            e.printStackTrace();
        }

        schedule.setContentView(HtmlUtil.stringFilter(content));
        schedule.setPlace(place);
        int result = scheduleService.updateSchedule(schedule);

        if (result > 0) {
            rs.setStatus(true);
            rs.setMsg("修改成功");
        } else {
            rs.setStatus(false);
            rs.setMsg("修改失败");
        }


        return rs;
    }

    @RequestMapping(value = "/deleteSchedule")
    @ResponseBody
    @MethodLog(module = Constants.SCHEDULE, desc = "删除日程")
    @Repeat
    public BaseResponse deleteSchedule(int id) {
        BaseResponse rs = new BaseResponse();
        int result = scheduleService.delSchedule(id);
        if (result > 0) {
            rs.setStatus(true);
            rs.setMsg("删除成功");
        } else {
            rs.setStatus(false);
            rs.setMsg("删除失败");
        }
        return rs;
    }

    @RequestMapping(value = "/getSchedule")
    @ResponseBody
    public ScheduleResponse getSchedule(@RequestParam(value = "id", defaultValue = "0") int id) {

        ScheduleResponse rs = new ScheduleResponse();
        Schedule schedule = scheduleService.getSchedule(id);
        if (schedule!=null) {
            rs.setStatus(true);
            rs.setMsg("查询成功");

            ScheduleVO vo = new ScheduleVO();
            vo.setId(schedule.getId());
            vo.setCompanyId(schedule.getCompanyId());
            vo.setType(schedule.getType());
            vo.setCompanyName(schedule.getCompanyName());
            vo.setTitle(schedule.getTitle());
            vo.setStatus(schedule.getStatus());
            vo.setContent(schedule.getContentView());
            vo.setPlace(schedule.getPlace());
            vo.setsTime(schedule.getsTime());
            vo.seteTime(schedule.geteTime());
            vo.setcTime(schedule.getcTime());

            rs.setData(vo);
        } else {
            rs.setStatus(false);
            rs.setMsg("查询失败");
        }

        return rs;
    }

    /**
     * 展品富文本上传图片
     * @param file
     * @return
     */
    @RequestMapping(value = "/uploadContentImage", method = RequestMethod.POST)
    @ResponseBody
    @MethodLog(module = Constants.SCHEDULE, desc = "展商富文本上传图片")
    @Repeat
    public LayeditResponse uploadContentImage(@RequestParam(value = "file", required = false) MultipartFile file) throws IOException {

        LayeditResponse rs = new LayeditResponse();

        if (null != file) {
            String myFileName = file.getOriginalFilename();// 文件原名称
            SimpleDateFormat dft = new SimpleDateFormat("yyyyMMddHHmmss");
            String fileName = dft.format(new Date()) + Integer.toHexString(new Random().nextInt()) + "." + myFileName.substring(myFileName.lastIndexOf(".") + 1);

            Properties properties = PropertiesUtil.getProperties("properties/config.properties");
            String path = properties.getProperty("img.path");
            String imageUrl = properties.getProperty("imageUrl");

            SimpleDateFormat yyyyMMdd = new SimpleDateFormat("yyyyMMdd");
            String midPath = yyyyMMdd.format(new Date());
            File fileDir = new File(path + midPath);
            if (!fileDir.exists()) { //如果不存在 则创建
                fileDir.mkdirs();
            }
            path = path + midPath + File.separator + fileName;
            File localFile = new File(path);
            try {
                file.transferTo(localFile);

                rs.setCode(0);
                rs.setMsg("上传成功");
                String httpUrl = imageUrl +File.separator+ midPath + File.separator + fileName;
                Map<String, String> map = new HashMap<>();
                map.put("src", httpUrl);
                rs.setData(map);
            } catch (IllegalStateException e) {
                rs.setCode(1);
                rs.setMsg("服务器异常");
            } catch (IOException e) {
                rs.setCode(1);
                rs.setMsg("服务器异常");
            }
        } else {
            rs.setCode(1);
            rs.setMsg("文件为空");
        }

        return rs;
    }

    /**
     * 日程报名查询
     *
     * @return
     */
    @RequestMapping(value = "/scheduleUserList", method = RequestMethod.POST)
    @ResponseBody
    public GridBaseResponse scheduleUserList(@RequestParam(value = "scheduleId", defaultValue = "-1") int scheduleId,
                                             @RequestParam(value = "page", defaultValue = "1") int page,
                                             @RequestParam(value = "limit", defaultValue = "10") int limit) {

        GridBaseResponse rs = new GridBaseResponse();
        rs.setCode(0);
        rs.setMsg("ok");

        PageHolder<ScheduleUser> pageHolder = scheduleService.queryScheduleUser(scheduleId, page, limit);

        List<ScheduleUserVO> voList = new ArrayList<>();
        if (null != pageHolder && null != pageHolder.getList() && pageHolder.getList().size() > 0) {
            for (ScheduleUser scheduleUser : pageHolder.getList()) {
                ScheduleUserVO vo = new ScheduleUserVO();
                vo.setUserId(scheduleUser.getUserId());
                vo.setUserName(scheduleUser.getUserName());
                vo.setPhone(scheduleUser.getPhone());
                vo.setCompany(scheduleUser.getCompany());
                vo.setPosition(scheduleUser.getPosition());
                vo.setEmail(scheduleUser.getEmail());
                vo.setAge(scheduleUser.getAge());
                vo.setReminderTime(scheduleUser.getReminderTime());
                vo.setcTime(scheduleUser.getcTime());
                vo.setReminderFlag(scheduleUser.getReminderFlag());
                voList.add(vo);
            }

            rs.setData(voList);
            rs.setCount(pageHolder.getTotalCount());
        }

        return rs;
    }

    /**
     * 获取展商小程序码
     * @return
     */
    public String getScheduleMaErCode(int id, int width) {
        Properties properties = null;
        String url = null;
        try {
            properties = PropertiesUtil.getProperties("properties/config.properties");
            url = properties.getProperty("schedule.ercode.url");
        } catch (IOException e) {
            e.printStackTrace();
        }

        Map<String, String> params = new HashMap<>();
        params.put("page", "pages/index/index");
        params.put("scene", "1:"+id);
        params.put("width", String.valueOf(width));

        Response<String> response = HttpUtil.sendGet(url, params, "UTF-8");

        if("0000".equals(response.getCode())){
            String result = response.getT();
            JSONObject object = JSONObject.parseObject(result);
            if("0000".equals(object.getString("status"))){
                String ercode = object.getString("ercode");

                return "data:image/jpeg;base64,"+ercode;
            }
        }
        return null;
    }
}
