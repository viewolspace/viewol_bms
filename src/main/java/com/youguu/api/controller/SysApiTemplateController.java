package com.youguu.api.controller;

import com.alibaba.fastjson.JSONObject;
import com.youguu.api.pojo.SysApiTemplate;
import com.youguu.api.response.TemplateResponse;
import com.youguu.api.service.SysApiTemplateService;
import com.youguu.asteroid.ad.pojo.AdWall;
import com.youguu.common.BaseResponse;
import com.youguu.common.GridBaseResponse;
import com.youguu.core.util.PageHolder;
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
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

@Controller
@RequestMapping("template")
public class SysApiTemplateController {

    @Resource
    private SysApiTemplateService sysApiTemplateService;

    /**
     * API模板列表
     * @param page
     * @param limit
     * @return
     */
    @RequestMapping(value = "/templateList", method = RequestMethod.POST)
    @ResponseBody
    public GridBaseResponse templateList(@RequestParam(value = "page", defaultValue = "1") int page,
                                    @RequestParam(value = "limit", defaultValue = "10") int limit) {

        GridBaseResponse rs = new GridBaseResponse();
        rs.setCode(0);
        rs.setMsg("ok");

        PageHolder<SysApiTemplate> pageHolder = sysApiTemplateService.querySysApiTemplateByPage(page, limit);
        if(null != pageHolder.getList()){
            rs.setData(pageHolder.getList());
            rs.setCount(pageHolder.getTotalCount());
        }

        return rs;
    }


    @RequestMapping(value = "/getTemplate")
    @ResponseBody
    public TemplateResponse getTemplate(@RequestParam(value = "id", defaultValue = "-1") int id) {

        TemplateResponse rs = new TemplateResponse();
        rs.setStatus(true);
        rs.setMsg("ok");

        SysApiTemplate sysApiTemplate = sysApiTemplateService.getSysApiTemplate(id);

        if(null != sysApiTemplate){
           rs.setData(sysApiTemplate);
        } else {
            rs.setStatus(false);
            rs.setMsg("模板不存在");
        }

        return rs;
    }


    @RequestMapping(value = "/updateTemplate", method = RequestMethod.POST)
    @ResponseBody
    @MethodLog(module = Constants.API, desc = "修改API模板")
    @Repeat
    public BaseResponse updateTemplate(@RequestParam(value = "id", defaultValue = "-1") int id,
                                 @RequestParam(value = "name", defaultValue = "") String name,
                                 @RequestParam(value = "contentText") String contentText) {

        BaseResponse rs = new BaseResponse();

        SysApiTemplate sysApiTemplate = sysApiTemplateService.getSysApiTemplate(id);
        if(null == sysApiTemplate){
            rs.setStatus(false);
            rs.setMsg("模板不存在");
            return rs;
        }

        sysApiTemplate.setName(name);
        sysApiTemplate.setContentText(contentText);
        sysApiTemplate.setUpdateTime(new Date());

        int result = sysApiTemplateService.updateSysApiTemplate(sysApiTemplate);
        if(result>0){
            rs.setStatus(true);
            rs.setMsg("修改成功");
        } else {
            rs.setStatus(false);
            rs.setMsg("修改失败");
        }

        return rs;
    }

    @RequestMapping(value = "/addTemplate", method = RequestMethod.POST)
    @ResponseBody
    @MethodLog(module = Constants.API, desc = "增加API模板")
    @Repeat
    public BaseResponse addTemplate(@RequestParam(value = "name", defaultValue = "") String name,
                                       @RequestParam(value = "contentText") String contentText) {

        BaseResponse rs = new BaseResponse();

        SysApiTemplate sysApiTemplate = new SysApiTemplate();
        sysApiTemplate.setName(name);
        sysApiTemplate.setContentText(contentText);
        sysApiTemplate.setUpdateTime(new Date());
        sysApiTemplate.setCreateTime(new Date());

        int result = sysApiTemplateService.saveSysApiTemplate(sysApiTemplate);
        if(result>0){
            rs.setStatus(true);
            rs.setMsg("保存成功");
        } else {
            rs.setStatus(false);
            rs.setMsg("保存失败");
        }

        return rs;
    }

    @RequestMapping(value = "/deleteTemplate")
    @ResponseBody
    @MethodLog(module = Constants.API, desc = "删除模板")
    @Repeat
    public BaseResponse deleteTemplate(int id) {
        BaseResponse rs = new BaseResponse();
        int result = sysApiTemplateService.deleteSysApiTemplate(id);
        if(result>0){
            rs.setStatus(true);
            rs.setMsg("删除成功");
        } else {
            rs.setStatus(false);
            rs.setMsg("删除失败");
        }

        return rs;
    }


    /**
     * 加载模板下拉选择框
     * @return
     */
    @RequestMapping(value = "/getTemplateSelect")
    @ResponseBody
    public RoleComboResponse getTemplateSelect() {

        RoleComboResponse rs = new RoleComboResponse();
        PageHolder<SysApiTemplate> pageHolder = sysApiTemplateService.querySysApiTemplateByPage(1, 20);

        List<SysApiTemplate> templateList = pageHolder.getList();
        if(null == templateList){
            rs.setStatus(false);
            rs.setMsg("加载模板下拉框异常");
            return rs;
        }

        List<JSONObject> templateSelect = new ArrayList<>();
        for(SysApiTemplate template : templateList){
            JSONObject option = new JSONObject();
            option.put("key", template.getId());
            option.put("value", template.getName());
            templateSelect.add(option);
        }

        rs.setStatus(true);
        rs.setMsg("ok");
        rs.setData(templateSelect);
        return rs;
    }
}
