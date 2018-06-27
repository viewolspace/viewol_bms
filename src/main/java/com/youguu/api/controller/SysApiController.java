package com.youguu.api.controller;

import com.youguu.api.pojo.SysApi;
import com.youguu.api.response.ApiResponse;
import com.youguu.api.service.SysApiService;
import com.youguu.common.BaseResponse;
import com.youguu.common.GridBaseResponse;
import com.youguu.core.util.PageHolder;
import com.youguu.sys.interceptor.Repeat;
import com.youguu.sys.log.annotation.MethodLog;
import com.youguu.sys.utils.Constants;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.annotation.Resource;

@Controller
@RequestMapping("api")
public class SysApiController {

    @Resource
    private SysApiService sysApiService;

    @RequestMapping(value = "/apiList", method = RequestMethod.POST)
    @ResponseBody
    public GridBaseResponse apiList(@RequestParam(value = "projectId", defaultValue = "-1") int projectId,
                                    @RequestParam(value = "serviceId", defaultValue = "-1") int serviceId,
                                    @RequestParam(value = "apiName", defaultValue = "") String apiName,
                                    @RequestParam(value = "page", defaultValue = "1") int page,
                                   @RequestParam(value = "limit", defaultValue = "10") int limit) {

        GridBaseResponse rs = new GridBaseResponse();
        rs.setCode(0);
        rs.setMsg("ok");

        PageHolder<SysApi> pageHolder = sysApiService.querySysApiByPage(projectId, serviceId, apiName, page, limit);
        if(null != pageHolder.getList()){
            rs.setData(pageHolder.getList());
            rs.setCount(pageHolder.getTotalCount());
        }

        return rs;
    }


    @RequestMapping(value = "/addApi", method = RequestMethod.POST)
    @ResponseBody
    @MethodLog(module = Constants.API, desc = "增加API")
    @Repeat
    public BaseResponse addApi(@RequestParam(value = "projectId", defaultValue = "-1") int projectId,
                               @RequestParam(value = "serviceId", defaultValue = "-1") int serviceId,
                                    @RequestParam(value = "apiName") String apiName,
                               @RequestParam(value = "apiUrl") String apiUrl,
                               @RequestParam(value = "requestMethod") String requestMethod,
                               @RequestParam(value = "apiDetail") String apiDetail,
                               @RequestParam(value = "apiDeveloper") String apiDeveloper,
                               @RequestParam(value = "apiStatus") int apiStatus) {

        BaseResponse rs = new BaseResponse();

       SysApi sysApi = new SysApi();

        int result = sysApiService.saveSysApi(sysApi);
        if(result>0){
            rs.setStatus(true);
            rs.setMsg("保存成功");
        } else {
            rs.setStatus(false);
            rs.setMsg("保存失败");
        }

        return rs;
    }

    @RequestMapping(value = "/updateApi", method = RequestMethod.POST)
    @ResponseBody
    @MethodLog(module = Constants.API, desc = "修改API")
    @Repeat
    public BaseResponse updateApi(@RequestParam(value = "id", defaultValue = "-1") int id,
                                  @RequestParam(value = "projectId", defaultValue = "-1") int projectId,
                               @RequestParam(value = "serviceId", defaultValue = "-1") int serviceId,
                               @RequestParam(value = "apiName") String apiName,
                               @RequestParam(value = "apiUrl") String apiUrl,
                               @RequestParam(value = "requestMethod") String requestMethod,
                               @RequestParam(value = "apiDetail") String apiDetail,
                               @RequestParam(value = "apiDeveloper") String apiDeveloper,
                               @RequestParam(value = "apiStatus") int apiStatus) {

        BaseResponse rs = new BaseResponse();

        SysApi sysApi = sysApiService.getSysApi(id);

        int result = sysApiService.updateSysApi(sysApi);
        if(result>0){
            rs.setStatus(true);
            rs.setMsg("修改成功");
        } else {
            rs.setStatus(false);
            rs.setMsg("修改失败");
        }

        return rs;
    }

    @RequestMapping(value = "/deleteApi")
    @ResponseBody
    @MethodLog(module = Constants.API, desc = "删除API")
    @Repeat
    public BaseResponse deleteApi(int id) {
        BaseResponse rs = new BaseResponse();
        int result = sysApiService.deleteSysApi(id);
        if(result>0){
            rs.setStatus(true);
            rs.setMsg("删除成功");
        } else {
            rs.setStatus(false);
            rs.setMsg("删除失败");
        }

        return rs;
    }

    @RequestMapping(value = "/getApi")
    @ResponseBody
    public ApiResponse getApi(@RequestParam(value = "id", defaultValue = "-1") int id) {

        ApiResponse rs = new ApiResponse();
        rs.setStatus(true);
        rs.setMsg("ok");

        SysApi sysApi = sysApiService.getSysApi(id);

        if(null != sysApi){
            rs.setData(sysApi);
        } else {
            rs.setStatus(false);
            rs.setMsg("API不存在");
        }

        return rs;
    }
}
