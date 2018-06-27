package com.youguu.api.controller;

import com.alibaba.fastjson.JSONObject;
import com.youguu.api.pojo.ResourceVO;
import com.youguu.api.pojo.SysApiResource;
import com.youguu.api.response.AllResourceResponse;
import com.youguu.api.service.SysApiResourceService;
import com.youguu.common.BaseResponse;
import com.youguu.shiro.token.TokenManager;
import com.youguu.sys.interceptor.Repeat;
import com.youguu.sys.log.annotation.MethodLog;
import com.youguu.sys.response.RoleComboResponse;
import com.youguu.sys.utils.Constants;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.annotation.Resource;
import java.util.ArrayList;
import java.util.List;

@Controller
@RequestMapping("resource")
public class SysApiResourceController {

    @Resource
    private SysApiResourceService sysApiResourceService;

    /**
     * 查询全部项目资源
     * @return
     */
    @RequestMapping(value = "/queryAllResource")
    @ResponseBody
    public AllResourceResponse queryAllResource() {
        AllResourceResponse rs = new AllResourceResponse();
        rs.setStatus(true);
        rs.setMsg("ok");
        int appId = TokenManager.getAppId();

        List<SysApiResource> list = sysApiResourceService.queryAllSysApiResource();

        if(list!=null && list.size()>0){
            List<ResourceVO> volist = new ArrayList<>();
            for(SysApiResource sysApiResource : list){
                ResourceVO vo = new ResourceVO();
                vo.setId(sysApiResource.getId());
                vo.setLevels(sysApiResource.getLevels());
                vo.setName(sysApiResource.getName());
                vo.setParentId(sysApiResource.getParentId());
                vo.setType(sysApiResource.getType());
                volist.add(vo);
            }
            rs.setData(volist);
        }
        return rs;
    }

    @RequestMapping(value = "/addResource", method = RequestMethod.POST)
    @ResponseBody
    @MethodLog(module = Constants.SYS_PERMISSION, desc = "添加权限")
    @Repeat
    public BaseResponse addResource(String name, int parentId, String type) {
        SysApiResource sysApiResource = new SysApiResource();
        sysApiResource.setParentId(parentId);
        sysApiResource.setName(name);
        sysApiResource.setType(type);

        int result = sysApiResourceService.saveSysApiResource(sysApiResource);
        BaseResponse rs = new BaseResponse();
        if(result>0){
            rs.setStatus(true);
            rs.setMsg("添加成功");
        } else {
            rs.setStatus(false);
            rs.setMsg("添加失败");
        }

        return rs;
    }

    @RequestMapping(value = "/updateResource", method = RequestMethod.POST)
    @ResponseBody
    @MethodLog(module = Constants.SYS_PERMISSION, desc = "修改权限")
    @Repeat
    public BaseResponse updateResource(int id, String name, int parentId, String type) {
        BaseResponse rs = new BaseResponse();

        SysApiResource sysApiResource = sysApiResourceService.getSysApiResource(id);
        if(null == sysApiResource){
            rs.setStatus(false);
            rs.setMsg("节点不存在");
            return rs;
        }

        sysApiResource.setParentId(parentId);
        sysApiResource.setName(name);
        sysApiResource.setType(type);

        int result = sysApiResourceService.updateSysApiResource(sysApiResource);

        if(result>0){
            rs.setStatus(true);
            rs.setMsg("修改成功");
        } else {
            rs.setStatus(false);
            rs.setMsg("修改失败");
        }

        return rs;
    }

    @RequestMapping(value = "/deleteResource", method = RequestMethod.POST)
    @ResponseBody
    @MethodLog(module = Constants.SYS_PERMISSION, desc = "删除权限")
    @Repeat
    public BaseResponse deleteResource(int id) {
        int result = sysApiResourceService.deleteSysApiResource(id);
        BaseResponse rs = new BaseResponse();

        if(result>0){
            rs.setStatus(true);
            rs.setMsg("删除成功");
        } else {
            rs.setStatus(false);
            rs.setMsg("删除失败");
        }

        return rs;
    }


    @RequestMapping(value = "/getResourceSelect")
    @ResponseBody
    public RoleComboResponse getResourceSelect(int pid) {

        RoleComboResponse rs = new RoleComboResponse();

        List<SysApiResource> resourceList = sysApiResourceService.findSysApiResourceByParent(pid);
        if(null == resourceList){
            rs.setStatus(false);
            rs.setMsg("加载项目资源下拉框异常");
            return rs;
        }

        List<JSONObject> resourceSelect = new ArrayList<>();
        for(SysApiResource sysApiResource : resourceList){
            JSONObject option = new JSONObject();
            option.put("key", sysApiResource.getId());
            option.put("value", sysApiResource.getName());
            resourceSelect.add(option);
        }

        rs.setStatus(true);
        rs.setMsg("ok");
        rs.setData(resourceSelect);
        return rs;
    }
}

