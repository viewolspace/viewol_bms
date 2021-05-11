//package com.viewol.sys.service.impl;
//
//import com.youguu.base.BaseTest;
//import com.youguu.core.util.PageHolder;
//import com.viewol.sys.pojo.SysRole;
//import com.viewol.sys.service.SysRoleService;
//import com.viewol.sys.service.SysUserService;
//import org.junit.Test;
//
//import static org.junit.Assert.*;
//
//public class SysRoleServiceImplTest extends BaseTest {
//
//	SysRoleService sysRoleService = (SysRoleService) getBean("sysRoleService");
//
//
//	@Test
//	public void testQuerySysRoleByPage() throws Exception {
//		PageHolder<SysRole> pageHolder = sysRoleService.querySysRoleByPage(1,"", 1, 50);
//
//		System.out.println(pageHolder.size());
//	}
//}