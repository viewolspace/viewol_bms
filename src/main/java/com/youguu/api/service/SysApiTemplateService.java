package com.youguu.api.service;

import com.youguu.api.pojo.SysApiTemplate;
import com.youguu.core.util.PageHolder;

public interface SysApiTemplateService {
    public PageHolder<SysApiTemplate> querySysApiTemplateByPage(int pageIndex, int pageSize);

    SysApiTemplate getSysApiTemplate(int id);

    int saveSysApiTemplate(SysApiTemplate sysApiTemplate);

    int updateSysApiTemplate(SysApiTemplate sysApiTemplate);

    int deleteSysApiTemplate(int id);
}
