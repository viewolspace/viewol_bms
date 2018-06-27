package com.youguu.api.service.impl;

import com.youguu.api.dao.SysApiDAO;
import com.youguu.api.pojo.SysApi;
import com.youguu.api.service.SysApiService;
import com.youguu.core.util.PageHolder;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;

@Service("sysApiService")
public class SysApiServiceImpl implements SysApiService {

    @Resource
    private SysApiDAO sysApiDAO;

    @Override
    public PageHolder<SysApi> querySysApiByPage(int projectId, int serviceId, String apiName, int pageIndex, int pageSize) {
        return sysApiDAO.querySysApiByPage(projectId, serviceId, apiName, pageIndex, pageSize);
    }

    @Override
    public int saveSysApi(SysApi sysApi) {
        return sysApiDAO.saveSysApi(sysApi);
    }

    @Override
    public int updateSysApi(SysApi sysApi) {
        return sysApiDAO.updateSysApi(sysApi);
    }

    @Override
    public int deleteSysApi(int id) {
        return sysApiDAO.deleteSysApi(id);
    }

    @Override
    public SysApi getSysApi(int id) {
        return sysApiDAO.getSysApi(id);
    }
}
