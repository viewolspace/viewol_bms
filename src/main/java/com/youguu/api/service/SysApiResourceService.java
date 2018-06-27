package com.youguu.api.service;

import com.youguu.api.pojo.SysApiResource;

import java.util.List;

public interface SysApiResourceService {

    public List<SysApiResource> queryAllSysApiResource();

    public int saveSysApiResource(SysApiResource sysApiResource);

    public int updateSysApiResource(SysApiResource sysApiResource);

    public int deleteSysApiResource(int id);

    public SysApiResource getSysApiResource(int id);

    List<SysApiResource> findSysApiResourceByParent(int pid);

}
