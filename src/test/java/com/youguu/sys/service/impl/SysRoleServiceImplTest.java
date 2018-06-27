package com.youguu.sys.service.impl;

import com.youguu.base.BaseTest;
import com.youguu.core.util.PageHolder;
import com.youguu.sys.pojo.SysRole;
import com.youguu.sys.service.SysRoleService;
import com.youguu.sys.service.SysUserService;
import org.junit.Test;

import static org.junit.Assert.*;

public class SysRoleServiceImplTest extends BaseTest {

	SysRoleService sysRoleService = (SysRoleService) getBean("sysRoleService");


	@Test
	public void testQuerySysRoleByPage() throws Exception {
		PageHolder<SysRole> pageHolder = sysRoleService.querySysRoleByPage(1,"", 1, 50);

		System.out.println(pageHolder.size());
	}
}