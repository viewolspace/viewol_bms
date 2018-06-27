package com.youguu.api.dao.impl;

import com.youguu.api.dao.SysApiDAO;
import com.youguu.api.pojo.SysApi;
import com.youguu.core.util.PageHolder;
import com.youguu.sys.base.OpenMsDAO;
import org.springframework.stereotype.Repository;

import java.util.HashMap;
import java.util.Map;

@Repository
public class SysApiDAOImpl extends OpenMsDAO<SysApi> implements SysApiDAO {

    @Override
    public PageHolder<SysApi> querySysApiByPage(int projectId, int serviceId, String apiName, int pageIndex, int pageSize) {

        Map<String, Object> map = new HashMap<>();
        map.put("projectId", projectId);
        map.put("serviceId", serviceId);
        map.put("apiName", apiName);
        return this.pagedQuery("querySysApiByPage", map, pageIndex, pageSize);
    }

    @Override
    public int saveSysApi(SysApi sysApi) {
        return this.insert(sysApi);
    }

    @Override
    public int updateSysApi(SysApi sysApi) {
        return this.update(sysApi);
    }

    @Override
    public int deleteSysApi(int id) {
        return this.delete(id);
    }

    @Override
    public SysApi getSysApi(int id) {
        return this.get(id);
    }
}
