package com.youguu.sys.dao;

import com.youguu.sys.pojo.SysPermission;

import java.util.List;

/**
 * Created by leo on 2017/11/23.
 */
public interface SysPermissionDAO {

	public int saveSysPermission(SysPermission permission);

	public int updateSysPermission(SysPermission permission);

	public int deleteSysPermission(int id);

	public SysPermission getSysPermission(int id);

	public List<SysPermission> queryAllSysPermission();

	List<SysPermission> findSysPermissionByRoleidAndPermissionId(int roleId, int parentId);

	List<SysPermission> findSysPermissionByRoleid(int roleId);

	public List<SysPermission> findSysPermissionByAppid(int roleId, int appId);

}
