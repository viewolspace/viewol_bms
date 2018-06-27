package com.youguu.sys.dao.impl;

import com.youguu.sys.base.OpenMsDAO;
import com.youguu.sys.dao.SysUserRoleDAO;
import com.youguu.sys.pojo.SysUserRole;
import org.springframework.stereotype.Repository;

/**
 * Created by leo on 2017/11/23.
 */
@Repository
public class SysUserRoleDAOImpl extends OpenMsDAO<SysUserRole> implements SysUserRoleDAO {
	@Override
	public int saveSysUserRole(SysUserRole userRole) {
		return this.insert(userRole);
	}

	@Override
	public int updateSysUserRole(SysUserRole userRole) {
		return this.update(userRole);
	}

	@Override
	public SysUserRole findSysUserRoleByUid(int uid) {
		return this.findUniqueBy("selectByUid", uid);
	}

	@Override
	public int deleteSysUserRoleByUid(int uid) {
		return this.deleteBy("deleteByUid", uid);
	}
}
