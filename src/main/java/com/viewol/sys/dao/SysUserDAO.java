package com.viewol.sys.dao;

import com.youguu.core.util.PageHolder;
import com.viewol.sys.pojo.SysUser;

import java.util.Date;

/**
 * Created by leo on 2017/11/23.
 */
public interface SysUserDAO {

	public int saveSysUser(SysUser sysUser);

	public int updateSysUser(SysUser sysUser);

	public int deleteSysUser(int id);

	public SysUser getSysUser(int id);

	public SysUser findSysUserByUserName(String username);

	public int updateLastLoginTime(String userName, Date lastLoginTime);

	int updatePwd(String userName, String oldPwd, String newPwd);

	public PageHolder<SysUser> querySysUserByPage(int appId, int userId, String realName, int pageIndex, int pageSize);
}
