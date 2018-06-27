package com.youguu.cloud.controller;

import com.alibaba.fastjson.JSONObject;
import com.youguu.common.BaseResponse;
import com.youguu.common.GridBaseResponse;
import com.youguu.core.util.PageHolder;
import com.youguu.member.client.service.MemberRpcService;
import com.youguu.member.common.pojo.CloudAuth;
import com.youguu.shiro.token.TokenManager;
import com.youguu.sys.interceptor.Repeat;
import com.youguu.sys.log.annotation.MethodLog;
import com.youguu.sys.pojo.SysRole;
import com.youguu.sys.response.RoleComboResponse;
import com.youguu.sys.utils.Constants;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.annotation.Resource;
import java.util.ArrayList;
import java.util.List;

@Controller
@RequestMapping("cloud_user")
public class CloudUserController {

    @Resource
    private MemberRpcService asyncMemberRpcService;

    @RequestMapping(value = "/cloudUserList", method = RequestMethod.POST)
    @ResponseBody
    public GridBaseResponse cloudUserList(@RequestParam(value = "appId", defaultValue = "-1") int appId,
                                          @RequestParam(value = "appName", defaultValue = "") String appName,
                                          @RequestParam(value = "page", defaultValue = "1") int page,
                                     @RequestParam(value = "limit", defaultValue = "10") int limit) {

        GridBaseResponse rs = new GridBaseResponse();
        rs.setCode(0);
        rs.setMsg("ok");

        PageHolder<CloudAuth> pageHolder = asyncMemberRpcService.queryCloudAuthByPate(appId, appName, page, limit);
        if(null != pageHolder.getList()){
            rs.setData(pageHolder.getList());
            rs.setCount(pageHolder.getTotalCount());
        }

        return rs;
    }

    /**
     * 创建云账号
     * @param appName
     * @param companyName
     * @return
     */
    @RequestMapping(value = "/addCloudAuth", method = RequestMethod.POST)
    @ResponseBody
    @MethodLog(module = Constants.SYS_USER, desc = "创建云账号")
    @Repeat
    public BaseResponse addCloudAuth(@RequestParam(value = "appName", defaultValue = "") String appName,
                                    @RequestParam(value = "companyName", defaultValue = "") String companyName) {

        BaseResponse rs = new BaseResponse();
        CloudAuth cloudAuth = asyncMemberRpcService.saveCloudAuth(appName, companyName);
        if(null != cloudAuth){
            rs.setStatus(true);
            rs.setMsg("ok");
        } else {
            rs.setStatus(false);
            rs.setMsg("创建云账号失败");
        }
        return rs;
    }

    @RequestMapping(value = "/getCloudAccountSelect")
    @ResponseBody
    public RoleComboResponse getCloudAccountSelect() {

        RoleComboResponse rs = new RoleComboResponse();
        PageHolder<CloudAuth> pageHolder = asyncMemberRpcService.queryCloudAuthByPate(-1, null, 1, 1000);
        List<CloudAuth> cloudAuthList = pageHolder.getList();
        if(null == cloudAuthList){
            rs.setStatus(false);
            rs.setMsg("加载云账号下拉框异常");
            return rs;
        }

        List<JSONObject> cloudSelect = new ArrayList<>();
        //超级管理员可以看到全部应用，应用管理员只能看到自己的应用
        if(TokenManager.getAppId()<=0){
            for(CloudAuth cloudAuth : cloudAuthList){
                JSONObject option = new JSONObject();
                option.put("key", cloudAuth.getAppId());
                option.put("value", cloudAuth.getAppName());
                cloudSelect.add(option);
            }
        } else {
            for(CloudAuth cloudAuth : cloudAuthList){
                if(TokenManager.getAppId() == cloudAuth.getAppId()){
                    JSONObject option = new JSONObject();
                    option.put("key", cloudAuth.getAppId());
                    option.put("value", cloudAuth.getAppName());
                    cloudSelect.add(option);
                }
            }
        }


        rs.setStatus(true);
        rs.setMsg("ok");
        rs.setData(cloudSelect);
        return rs;
    }
}
