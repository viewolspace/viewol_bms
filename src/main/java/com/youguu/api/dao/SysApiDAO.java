package com.youguu.api.dao;

import com.youguu.api.pojo.SysApi;
import com.youguu.core.util.PageHolder;

public interface SysApiDAO {
    public PageHolder<SysApi> querySysApiByPage(int projectId, int serviceId, String apiName, int pageIndex, int pageSize);

    int saveSysApi(SysApi sysApi);

    int updateSysApi(SysApi sysApi);

    int deleteSysApi(int id);

    SysApi getSysApi(int id);
}
