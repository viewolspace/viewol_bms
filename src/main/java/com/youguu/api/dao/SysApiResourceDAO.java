package com.youguu.api.dao;

import com.youguu.api.pojo.SysApiResource;

import java.util.List;

public interface SysApiResourceDAO {
    List<SysApiResource> queryAllSysApiResource();

    int saveSysApiResource(SysApiResource sysApiResource);

    int updateSysApiResource(SysApiResource sysApiResource);

    int deleteSysApiResource(int id);

    SysApiResource getSysApiResource(int id);

    List<SysApiResource> findSysApiResourceByParent(int pid);
}
