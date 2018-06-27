package com.youguu.sys.service;

import com.youguu.sys.pojo.SysPermission;

import java.util.List;

/**
 * Created by leo on 2017/11/23.
 */
public interface SysPermissionService {

	/**
	 * 根据用户ID查询权限
	 * @param userName
	 * @return
	 */
	List<String> findPermissionByUserName(String userName);

	public int saveSysPermission(SysPermission permission);

	public int updateSysPermission(SysPermission permission);

	public int deleteSysPermission(int id);

	public SysPermission getSysPermission(int id);

	public List<SysPermission> queryAllSysPermission();

	List<SysPermission> findSysPermissionByRoleidAndPermissionId(int roleId, int parentId);

	public List<SysPermission> findSysPermissionByRoleid(int roleId);

	public List<SysPermission> findSysPermissionByAppid(int roleId, int appId);
}
