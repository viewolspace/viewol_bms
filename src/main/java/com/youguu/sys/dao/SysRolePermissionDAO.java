package com.youguu.sys.dao;

import com.youguu.sys.pojo.SysRolePermission;

import java.util.List;

/**
 * Created by leo on 2017/11/23.
 */
public interface SysRolePermissionDAO {

	int batchSaveSysRolePermission(List<SysRolePermission> sysRolePermissionList);

	int deleteSysRolePermissionByRole(int roleId);

	List<SysRolePermission> listSysRolePermissionByRole(Integer... roleId);
}
