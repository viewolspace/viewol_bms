package com.viewol.sys.service.impl;

import com.viewol.sys.dao.SysUserRoleDAO;
import com.viewol.sys.pojo.SysUserRole;
import com.viewol.sys.service.SysUserRoleService;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;

/**
 * Created by leo on 2017/11/23.
 */
@Service("sysUserRoleService")
public class SysUserRoleServiceImpl implements SysUserRoleService {

	@Resource
	private SysUserRoleDAO sysUserRoleDAO;

	@Override
	public int saveSysUserRole(SysUserRole userRole) {
		return sysUserRoleDAO.saveSysUserRole(userRole);
	}

	@Override
	public int updateSysUserRole(SysUserRole userRole) {
		return sysUserRoleDAO.updateSysUserRole(userRole);
	}

	@Override
	public SysUserRole findSysUserRoleByUid(int uid) {
		return sysUserRoleDAO.findSysUserRoleByUid(uid);
	}

	@Override
	public int deleteSysUserRoleByUid(int uid) {
		return sysUserRoleDAO.deleteSysUserRoleByUid(uid);
	}
}
