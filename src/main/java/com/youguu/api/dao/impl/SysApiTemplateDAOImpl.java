package com.youguu.api.dao.impl;

import com.youguu.api.dao.SysApiTemplateDAO;
import com.youguu.api.pojo.SysApiTemplate;
import com.youguu.core.util.PageHolder;
import com.youguu.sys.base.OpenMsDAO;
import org.springframework.stereotype.Repository;

@Repository
public class SysApiTemplateDAOImpl extends OpenMsDAO<SysApiTemplate> implements SysApiTemplateDAO {
    @Override
    public PageHolder<SysApiTemplate> querySysApiTemplateByPage(int pageIndex, int pageSize) {
        return this.pagedQuery("querySysApiTemplateByPage", null, pageIndex, pageSize);
    }

    @Override
    public SysApiTemplate getSysApiTemplate(int id) {
        return this.get(id);
    }

    @Override
    public int saveSysApiTemplate(SysApiTemplate sysApiTemplate) {
        return this.insert(sysApiTemplate);
    }

    @Override
    public int updateSysApiTemplate(SysApiTemplate sysApiTemplate) {
        return this.update(sysApiTemplate);
    }

    @Override
    public int deleteSysApiTemplate(int id) {
        return this.delete(id);
    }
}
