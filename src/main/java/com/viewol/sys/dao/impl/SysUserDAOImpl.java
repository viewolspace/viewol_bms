package com.viewol.sys.dao.impl;

import com.youguu.core.util.PageHolder;
import com.viewol.sys.base.OpenMsDAO;
import com.viewol.sys.dao.SysUserDAO;
import com.viewol.sys.pojo.SysUser;
import org.springframework.stereotype.Repository;

import java.util.Date;
import java.util.HashMap;
import java.util.Map;

/**
 * Created by leo on 2017/11/23.
 */
@Repository
public class SysUserDAOImpl extends OpenMsDAO<SysUser> implements SysUserDAO {
	@Override
	public int saveSysUser(SysUser sysUser) {
		this.insert(sysUser);
		return sysUser.getId();
	}

	@Override
	public int updateSysUser(SysUser sysUser) {
		return this.update(sysUser);
	}

	@Override
	public int deleteSysUser(int id) {
		return this.delete(id);
	}

	@Override
	public SysUser getSysUser(int id) {
		return this.get(id);
	}

	@Override
	public SysUser findSysUserByUserName(String username) {
		return this.findUniqueBy("findSysUserByUserName", username);
	}

	@Override
	public int updateLastLoginTime(String userName, Date lastLoginTime) {
		Map<String, Object> map = new HashMap<>();
		map.put("lastLoginTime", lastLoginTime);
		map.put("userName", userName);

		return this.updateBy("updateLastLoginTime", map);
	}

	@Override
	public int updatePwd(String userName, String oldPwd, String newPwd) {
		Map<String, Object> map = new HashMap<>();
		map.put("userName", userName);
		map.put("oldPwd", oldPwd);
		map.put("newPwd", newPwd);
		return this.updateBy("updatePwd", map);
	}

	@Override
	public PageHolder<SysUser> querySysUserByPage(int appId, int userId, String realName, int pageIndex, int pageSize) {
		Map<String, Object> map = new HashMap<>();
		map.put("userId", userId);
		map.put("realName", realName);
		map.put("appId", appId);
		return this.pagedQuery("querySysUserByPage", map, pageIndex, pageSize);
	}
}
