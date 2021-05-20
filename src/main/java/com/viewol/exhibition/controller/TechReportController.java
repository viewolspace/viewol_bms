package com.viewol.exhibition.controller;

import com.viewol.common.BaseResponse;
import com.viewol.common.GridBaseResponse;
import com.viewol.exhibition.response.TechReportResponse;
import com.viewol.pojo.TechReport;
import com.viewol.pojo.query.TechReportQuery;
import com.viewol.service.ITechReportService;
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

import javax.annotation.Resource;
import java.util.Date;

@Controller
@RequestMapping("techReport")
public class TechReportController {
    @Resource
    private ITechReportService techReportService;

    @RequestMapping(value = "/techReportList", method = RequestMethod.POST)
    @ResponseBody
    public GridBaseResponse techReportList(@RequestParam(value = "page", defaultValue = "1") int page,
                                           @RequestParam(value = "limit", defaultValue = "10") int limit,
                                           @RequestParam(value = "companyName", defaultValue = "") String companyName) {

        GridBaseResponse rs = new GridBaseResponse();
        rs.setCode(0);
        rs.setMsg("ok");

        TechReportQuery techReportQuery = new TechReportQuery();
        techReportQuery.setCompanyName(companyName);

        PageHolder<TechReport> pageHolder = techReportService.queryTechReport(techReportQuery);
        if (null != pageHolder && null != pageHolder.getList() && pageHolder.getList().size() > 0) {

            rs.setData(pageHolder.getList());
            rs.setCount(pageHolder.getTotalCount());
        }

        return rs;
    }

    @RequestMapping(value = "/addTechReport", method = RequestMethod.POST)
    @ResponseBody
    @Repeat
    public BaseResponse addTechReport(
            @RequestParam(value = "phone", defaultValue = "") String phone,
            @RequestParam(value = "email", defaultValue = "") String email,
            @RequestParam(value = "openId", defaultValue = "") String openId,
            @RequestParam(value = "title", defaultValue = "") String title,
            @RequestParam(value = "name", defaultValue = "") String name,
            @RequestParam(value = "postion", defaultValue = "") String postion,
            @RequestParam(value = "summary", defaultValue = "") String summary,
            @RequestParam(value = "forumTitle", defaultValue = "") String forumTitle,
            @RequestParam(value = "forumRoom", defaultValue = "") String forumRoom,
            @RequestParam(value = "forumTime", defaultValue = "") String forumTime,
            @RequestParam(value = "forumNum", defaultValue = "") int forumNum) {

        BaseResponse rs = new BaseResponse();

        TechReport techReport = new TechReport();
        techReport.setPhone(phone);
        techReport.setEmail(email);
        techReport.setOpenId(openId);
        techReport.setTitle(title);
        techReport.setName(name);
        techReport.setPostion(postion);
        techReport.setSummary(summary);
        techReport.setForumTitle(forumTitle);
        techReport.setForumRoom(forumRoom);
        techReport.setForumTime(forumTime);
        techReport.setForumNum(forumNum);
        techReport.setCreateTime(new Date());
//        techReport.setCompanyId(TokenManager.getCompanyId());
        techReport.setCompanyName(TokenManager.getRealName());
        int result = techReportService.addTechReport(techReport);
        if (result > 0) {
            rs.setStatus(true);
            rs.setMsg("数据提交成功");
        } else {
            rs.setStatus(false);
            rs.setMsg("数据提交失败");
        }

        return rs;
    }

    @RequestMapping(value = "/updateTechReport", method = RequestMethod.POST)
    @ResponseBody
    @Repeat
    public BaseResponse updateTechReport(
            @RequestParam(value = "id", defaultValue = "") int id,
            @RequestParam(value = "phone", defaultValue = "") String phone,
            @RequestParam(value = "email", defaultValue = "") String email,
            @RequestParam(value = "openId", defaultValue = "") String openId,
            @RequestParam(value = "title", defaultValue = "") String title,
            @RequestParam(value = "name", defaultValue = "") String name,
            @RequestParam(value = "postion", defaultValue = "") String postion,
            @RequestParam(value = "summary", defaultValue = "") String summary,
            @RequestParam(value = "forumTitle", defaultValue = "") String forumTitle,
            @RequestParam(value = "forumRoom", defaultValue = "") String forumRoom,
            @RequestParam(value = "forumTime", defaultValue = "") String forumTime,
            @RequestParam(value = "forumNum", defaultValue = "") int forumNum) {

        BaseResponse rs = new BaseResponse();

        TechReport techReport = techReportService.getTechReport(id);
        techReport.setPhone(phone);
        techReport.setEmail(email);
        techReport.setOpenId(openId);
        techReport.setTitle(title);
        techReport.setName(name);
        techReport.setPostion(postion);
        techReport.setSummary(summary);
        techReport.setForumTitle(forumTitle);
        techReport.setForumRoom(forumRoom);
        techReport.setForumTime(forumTime);
        techReport.setForumNum(forumNum);
        techReport.setUpdateTime(new Date());

        int result = techReportService.updateTechReport(techReport);
        if (result > 0) {
            rs.setStatus(true);
            rs.setMsg("数据提交成功");
        } else {
            rs.setStatus(false);
            rs.setMsg("数据提交失败");
        }

        return rs;
    }


    @RequestMapping(value = "/deleteTechReport")
    @ResponseBody
    @MethodLog(module = Constants.EXHIBITION, desc = "删除技术报告会申请")
    @Repeat
    public BaseResponse deleteTechReport(@RequestParam(value = "id", defaultValue = "") int id) {
        BaseResponse rs = new BaseResponse();

        int result = techReportService.deleteTechReport(id);
        if (result > 0) {
            rs.setStatus(true);
            rs.setMsg("删除成功");
        } else {
            rs.setStatus(false);
            rs.setMsg("删除失败");
        }
        return rs;
    }

    @RequestMapping(value = "/getTechReport", method = RequestMethod.POST)
    @ResponseBody
    public TechReportResponse getTechReport(@RequestParam(value = "id", defaultValue = "0") int id) {
        TechReportResponse rs = new TechReportResponse();
        TechReport techReport = techReportService.getTechReport(id);

        if (null != techReport) {
            rs.setStatus(true);
            rs.setMsg("ok");
            rs.setData(techReport);
        } else {
            rs.setStatus(false);
            rs.setMsg("无此申请");
        }
        return rs;
    }

}
