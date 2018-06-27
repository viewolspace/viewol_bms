package com.youguu.api.service.impl;

import com.youguu.api.dao.SysApiTemplateDAO;
import com.youguu.api.pojo.SysApiTemplate;
import com.youguu.api.service.SysApiTemplateService;
import com.youguu.core.util.PageHolder;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;

@Service("sysApiTemplateService")
public class SysApiTemplateServiceImpl implements SysApiTemplateService {

    @Resource
    private SysApiTemplateDAO sysApiTemplateDAO;

    @Override
    public PageHolder<SysApiTemplate> querySysApiTemplateByPage(int pageIndex, int pageSize) {
        return sysApiTemplateDAO.querySysApiTemplateByPage(pageIndex, pageSize);
    }

    @Override
    public SysApiTemplate getSysApiTemplate(int id) {
        return sysApiTemplateDAO.getSysApiTemplate(id);
    }

    @Override
    public int saveSysApiTemplate(SysApiTemplate sysApiTemplate) {
        return sysApiTemplateDAO.saveSysApiTemplate(sysApiTemplate);
    }

    @Override
    public int updateSysApiTemplate(SysApiTemplate sysApiTemplate) {
        return sysApiTemplateDAO.updateSysApiTemplate(sysApiTemplate);
    }

    @Override
    public int deleteSysApiTemplate(int id) {
        return sysApiTemplateDAO.deleteSysApiTemplate(id);
    }
}
