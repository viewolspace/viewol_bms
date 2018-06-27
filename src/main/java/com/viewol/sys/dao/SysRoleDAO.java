package com.viewol.sys.dao;

import com.youguu.core.util.PageHolder;
import com.viewol.sys.pojo.SysRole;

import java.util.List;

/**
 * Created by leo on 2017/11/23.
 */
public interface SysRoleDAO {
	int saveSysRole(SysRole role);

	int updateSysRole(SysRole role);

	int deleteSysRole(int id);

	SysRole getSysRole(int id);

	List<SysRole> listALLSysRole(int appId);

	PageHolder<SysRole> querySysRoleByPage(int appId, String name, int pageIndex, int pageSize);

	int relateApp(int id, int appId);

}
