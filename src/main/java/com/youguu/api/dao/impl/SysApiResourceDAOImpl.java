package com.youguu.api.dao.impl;

import com.youguu.api.dao.SysApiResourceDAO;
import com.youguu.api.pojo.SysApiResource;
import com.youguu.sys.base.OpenMsDAO;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public class SysApiResourceDAOImpl extends OpenMsDAO<SysApiResource> implements SysApiResourceDAO {
    @Override
    public List<SysApiResource> queryAllSysApiResource() {
        return this.getAll();
    }

    @Override
    public int saveSysApiResource(SysApiResource sysApiResource) {
        return this.insert(sysApiResource);
    }

    @Override
    public int updateSysApiResource(SysApiResource sysApiResource) {
        return this.update(sysApiResource);
    }

    @Override
    public int deleteSysApiResource(int id) {
        return this.delete(id);
    }

    @Override
    public SysApiResource getSysApiResource(int id) {
        return this.get(id);
    }

    @Override
    public List<SysApiResource> findSysApiResourceByParent(int pid) {
        return this.findBy("select_by_parentId", pid);
    }
}
