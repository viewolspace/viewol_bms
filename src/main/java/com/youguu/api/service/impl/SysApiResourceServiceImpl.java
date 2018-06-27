package com.youguu.api.service.impl;

import com.youguu.api.dao.SysApiResourceDAO;
import com.youguu.api.pojo.SysApiResource;
import com.youguu.api.service.SysApiResourceService;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.List;

@Service("sysApiResourceService")
public class SysApiResourceServiceImpl implements SysApiResourceService {

    @Resource
    private SysApiResourceDAO sysApiResourceDAO;

    @Override
    public List<SysApiResource> queryAllSysApiResource() {
        return sysApiResourceDAO.queryAllSysApiResource();
    }

    @Override
    public int saveSysApiResource(SysApiResource sysApiResource) {
        return sysApiResourceDAO.saveSysApiResource(sysApiResource);
    }

    @Override
    public int updateSysApiResource(SysApiResource sysApiResource) {
        return sysApiResourceDAO.updateSysApiResource(sysApiResource);
    }

    @Override
    public int deleteSysApiResource(int id) {
        return sysApiResourceDAO.deleteSysApiResource(id);
    }

    @Override
    public SysApiResource getSysApiResource(int id) {
        return sysApiResourceDAO.getSysApiResource(id);
    }

    @Override
    public List<SysApiResource> findSysApiResourceByParent(int pid) {
        return sysApiResourceDAO.findSysApiResourceByParent(pid);
    }
}
