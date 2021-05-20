package com.viewol.exhibition.controller;

import com.viewol.common.BaseResponse;
import com.viewol.common.GridBaseResponse;
import com.viewol.exhibition.response.PerformanceResponse;
import com.viewol.pojo.Performance;
import com.viewol.pojo.query.PerformanceQuery;
import com.viewol.service.IPerformanceService;
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
@RequestMapping("performance")
public class PerformanceController {
    @Resource
    private IPerformanceService performanceService;

    @RequestMapping(value = "/performanceList", method = RequestMethod.POST)
    @ResponseBody
    public GridBaseResponse performanceList(@RequestParam(value = "page", defaultValue = "1") int page,
                                            @RequestParam(value = "limit", defaultValue = "10") int limit,
                                            @RequestParam(value = "companyName", defaultValue = "") String companyName) {

        GridBaseResponse rs = new GridBaseResponse();
        rs.setCode(0);
        rs.setMsg("ok");

        PerformanceQuery performanceQuery = new PerformanceQuery();
        performanceQuery.setCompanyName(companyName);

        PageHolder<Performance> pageHolder = performanceService.queryPerformance(performanceQuery);
        if (null != pageHolder && null != pageHolder.getList() && pageHolder.getList().size() > 0) {

            rs.setData(pageHolder.getList());
            rs.setCount(pageHolder.getTotalCount());
        }

        return rs;
    }

    @RequestMapping(value = "/addPerformance", method = RequestMethod.POST)
    @ResponseBody
    @Repeat
    public BaseResponse addPerformance(
            @RequestParam(value = "phone", defaultValue = "") String phone,
            @RequestParam(value = "email", defaultValue = "") String email,
            @RequestParam(value = "openId", defaultValue = "") String openId,
            @RequestParam(value = "performanceCategory", defaultValue = "") String performanceCategory,
            @RequestParam(value = "performanceProduct", defaultValue = "") String performanceProduct,
            @RequestParam(value = "feature", defaultValue = "") String feature,
            @RequestParam(value = "area", defaultValue = "") Double area,
            @RequestParam(value = "needHelp", defaultValue = "") String needHelp,
            @RequestParam(value = "performanceTime", defaultValue = "") String performanceTime,
            @RequestParam(value = "adPositon", defaultValue = "") String adPositon,
            @RequestParam(value = "adMethod", defaultValue = "") String adMethod) {

        BaseResponse rs = new BaseResponse();

        Performance performance = new Performance();
        performance.setPhone(phone);
        performance.setEmail(email);
        performance.setOpenId(openId);
        performance.setPerformanceCategory(performanceCategory);
        performance.setPerformanceProduct(performanceProduct);
        performance.setFeature(feature);
        performance.setArea(area);
        performance.setNeedHelp(needHelp);
        performance.setPerformanceTime(performanceTime);
        performance.setAdPositon(adPositon);
        performance.setAdMethod(adMethod);
//        performance.setCompanyId(TokenManager.getCompanyId());
        performance.setCompanyName(TokenManager.getRealName());
        performance.setCreateTime(new Date());

        int result = performanceService.addPerformance(performance);
        if (result > 0) {
            rs.setStatus(true);
            rs.setMsg("数据提交成功");
        } else {
            rs.setStatus(false);
            rs.setMsg("数据提交失败");
        }

        return rs;
    }

    @RequestMapping(value = "/updatePerformance", method = RequestMethod.POST)
    @ResponseBody
    @Repeat
    public BaseResponse updatePerformance(
            @RequestParam(value = "id", defaultValue = "") int id,
            @RequestParam(value = "phone", defaultValue = "") String phone,
            @RequestParam(value = "email", defaultValue = "") String email,
            @RequestParam(value = "openId", defaultValue = "") String openId,
            @RequestParam(value = "performanceCategory", defaultValue = "") String performanceCategory,
            @RequestParam(value = "performanceProduct", defaultValue = "") String performanceProduct,
            @RequestParam(value = "feature", defaultValue = "") String feature,
            @RequestParam(value = "area", defaultValue = "") Double area,
            @RequestParam(value = "needHelp", defaultValue = "") String needHelp,
            @RequestParam(value = "performanceTime", defaultValue = "") String performanceTime,
            @RequestParam(value = "adPositon", defaultValue = "") String adPositon,
            @RequestParam(value = "adMethod", defaultValue = "") String adMethod) {

        BaseResponse rs = new BaseResponse();

        Performance performance = performanceService.getPerformance(id);
        performance.setPhone(phone);
        performance.setEmail(email);
        performance.setOpenId(openId);
        performance.setPerformanceCategory(performanceCategory);
        performance.setPerformanceProduct(performanceProduct);
        performance.setFeature(feature);
        performance.setArea(area);
        performance.setNeedHelp(needHelp);
        performance.setPerformanceTime(performanceTime);
        performance.setAdPositon(adPositon);
        performance.setAdMethod(adMethod);
        performance.setUpdateTime(new Date());

        int result = performanceService.updatePerformance(performance);
        if (result > 0) {
            rs.setStatus(true);
            rs.setMsg("数据提交成功");
        } else {
            rs.setStatus(false);
            rs.setMsg("数据提交失败");
        }

        return rs;
    }

    @RequestMapping(value = "/deletePerformance")
    @ResponseBody
    @MethodLog(module = Constants.EXHIBITION, desc = "删除展演申请")
    @Repeat
    public BaseResponse deletePerformance(@RequestParam(value = "id", defaultValue = "") int id) {
        BaseResponse rs = new BaseResponse();

        int result = performanceService.deletePerformance(id);
        if (result > 0) {
            rs.setStatus(true);
            rs.setMsg("删除成功");
        } else {
            rs.setStatus(false);
            rs.setMsg("删除失败");
        }
        return rs;
    }

    @RequestMapping(value = "/getPerformance", method = RequestMethod.POST)
    @ResponseBody
    public PerformanceResponse getPerformance(@RequestParam(value = "id", defaultValue = "") int id) {
        PerformanceResponse rs = new PerformanceResponse();
        Performance performance = performanceService.getPerformance(id);

        if (null != performance) {
            rs.setStatus(true);
            rs.setMsg("ok");
            rs.setData(performance);
        } else {
            rs.setStatus(false);
            rs.setMsg("无此申请");
        }
        return rs;
    }

}
