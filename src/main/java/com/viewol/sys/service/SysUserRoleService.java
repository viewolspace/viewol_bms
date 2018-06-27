package com.viewol.sys.service;

import com.viewol.sys.pojo.SysUserRole;

/**
 * Created by leo on 2017/11/23.
 */
public interface SysUserRoleService {
	int saveSysUserRole(SysUserRole userRole);

	int updateSysUserRole(SysUserRole userRole);

	SysUserRole findSysUserRoleByUid(int uid);

	int deleteSysUserRoleByUid(int uid);
}
