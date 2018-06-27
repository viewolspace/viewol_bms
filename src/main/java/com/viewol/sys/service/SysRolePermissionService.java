package com.viewol.sys.service;

import com.viewol.sys.pojo.SysRolePermission;

import java.util.List;

/**
 * Created by leo on 2017/11/23.
 */
public interface SysRolePermissionService {

	int batchSaveSysRolePermission(List<SysRolePermission> sysRolePermissionList);

	List<SysRolePermission> listSysRolePermissionByRole(Integer... roleId);
}
