package com.viewol.sys.service.impl;

import com.youguu.core.util.PageHolder;
import com.viewol.sys.dao.SysUserDAO;
import com.viewol.sys.pojo.SysRole;
import com.viewol.sys.pojo.SysUser;
import com.viewol.sys.service.SysRoleService;
import com.viewol.sys.service.SysUserService;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.Date;

/**
 * Created by leo on 2017/11/23.
 */
@Service("sysUserService")
public class SysUserServiceImpl implements SysUserService {

	@Resource
	private SysUserDAO sysUserDAO;
	@Resource
	private SysRoleService sysRoleService;

	@Override
	public int saveSysUser(SysUser sysUser) {
		return sysUserDAO.saveSysUser(sysUser);
	}

	@Override
	public int updateSysUser(SysUser sysUser) {
		return sysUserDAO.updateSysUser(sysUser);
	}

	@Override
	public int deleteSysUser(int id) {
		return sysUserDAO.deleteSysUser(id);
	}

	@Override
	public SysUser getSysUser(int id) {
		return sysUserDAO.getSysUser(id);
	}

	@Override
	public PageHolder<SysUser> querySysUserByPage(int appId, int userId, String realName, int pageIndex, int pageSize) {
		return sysUserDAO.querySysUserByPage(appId, userId, realName, pageIndex, pageSize);
	}

	@Override
	public SysUser findSysUserByUserName(String username) {
		SysUser sysUser = sysUserDAO.findSysUserByUserName(username);
		if(null != sysUser){
			SysRole sysRole = sysRoleService.getSysRole(sysUser.getRoleId());
			sysUser.setAppId(sysRole.getAppId());
		}

		return sysUser;
	}

	@Override
	public int updateLastLoginTime(String userName, Date lastLoginTime) {
		return sysUserDAO.updateLastLoginTime(userName, lastLoginTime);
	}

	@Override
	public int updatePwd(String userName, String oldPwd, String newPwd) {
		return sysUserDAO.updatePwd(userName, oldPwd, newPwd);
	}

}
