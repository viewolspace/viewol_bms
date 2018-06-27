package com.viewol.sys.service.impl;

import com.viewol.sys.dao.SysPermissionDAO;
import com.viewol.sys.pojo.SysPermission;
import com.viewol.sys.service.SysPermissionService;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;

/**
 * Created by leo on 2017/11/23.
 */
@Service("sysPermissionService")
public class SysPermissionServiceImpl implements SysPermissionService {

	@Resource
	private SysPermissionDAO sysPermissionDAO;

	@Override
	public List<String> findPermissionByUserName(String userName) {
		List<String> list = new ArrayList<>();
		list.add("/index/home.do");
		list.add("/page/home.jsp**");
		list.add("/page/403.jsp");
		list.add("/index/home");
		list.add("/403");

		return list;
	}

	@Override
	public int saveSysPermission(SysPermission permission) {
		return sysPermissionDAO.saveSysPermission(permission);
	}

	@Override
	public int updateSysPermission(SysPermission permission) {
		return sysPermissionDAO.updateSysPermission(permission);
	}

	@Override
	public int deleteSysPermission(int id) {
		return sysPermissionDAO.deleteSysPermission(id);
	}

	@Override
	public SysPermission getSysPermission(int id) {
		return sysPermissionDAO.getSysPermission(id);
	}

	@Override
	public List<SysPermission> queryAllSysPermission() {
		return sysPermissionDAO.queryAllSysPermission();
	}

	@Override
	public List<SysPermission> findSysPermissionByRoleidAndPermissionId(int roleId, int parentId) {
		return sysPermissionDAO.findSysPermissionByRoleidAndPermissionId(roleId, parentId);
	}

	@Override
	public List<SysPermission> findSysPermissionByRoleid(int roleId) {
		return sysPermissionDAO.findSysPermissionByRoleid(roleId);
	}

	@Override
	public List<SysPermission> findSysPermissionByAppid(int roleId, int appId) {
		return sysPermissionDAO.findSysPermissionByAppid(roleId, appId);
	}

}
