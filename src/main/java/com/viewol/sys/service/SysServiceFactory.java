package com.viewol.sys.service;

import com.viewol.sys.base.ContextLoader;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;

public class SysServiceFactory {

	private static SysPermissionService sysPermissionService = null;
	private static SysRolePermissionService sysRolePermissionService = null;
	private static SysRoleService sysRoleService = null;
	private static SysUserRoleService sysUserRoleService = null;
	private static SysUserService sysUserService = null;

	public static synchronized SysPermissionService getSysPermissionService() {
		if (sysPermissionService == null) {
			try {
				sysPermissionService = new AnnotationConfigApplicationContext(
						ContextLoader.class).getBean("sysPermissionService", SysPermissionService.class);
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
		return sysPermissionService;
	}

	public static synchronized SysRolePermissionService getSysRolePermissionService() {
		if (sysRolePermissionService == null) {
			try {
				sysRolePermissionService = new AnnotationConfigApplicationContext(
						ContextLoader.class).getBean("sysRolePermissionService", SysRolePermissionService.class);
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
		return sysRolePermissionService;
	}

	public static synchronized SysRoleService getSysRoleService() {
		if (sysRoleService == null) {
			try {
				sysRoleService = new AnnotationConfigApplicationContext(
						ContextLoader.class).getBean("sysRoleService", SysRoleService.class);
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
		return sysRoleService;
	}

	public static synchronized SysUserRoleService getSysUserRoleService() {
		if (sysUserRoleService == null) {
			try {
				sysUserRoleService = new AnnotationConfigApplicationContext(
						ContextLoader.class).getBean("sysUserRoleService", SysUserRoleService.class);
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
		return sysUserRoleService;
	}

	public static synchronized SysUserService getSysUserService() {
		if (sysUserService == null) {
			try {
				sysUserService = new AnnotationConfigApplicationContext(ContextLoader.class).getBean("sysUserService", SysUserService.class);
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
		return sysUserService;
	}
}
