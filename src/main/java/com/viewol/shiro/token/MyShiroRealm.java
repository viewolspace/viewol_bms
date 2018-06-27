package com.viewol.shiro.token;

import com.viewol.sys.pojo.SysPermission;
import com.viewol.sys.pojo.SysUser;
import com.viewol.sys.pojo.SysUserRole;
import com.viewol.sys.service.SysPermissionService;
import com.viewol.sys.service.SysUserRoleService;
import com.viewol.sys.service.SysUserService;
import org.apache.shiro.authc.*;
import org.apache.shiro.authz.AuthorizationInfo;
import org.apache.shiro.authz.SimpleAuthorizationInfo;
import org.apache.shiro.realm.AuthorizingRealm;
import org.apache.shiro.subject.PrincipalCollection;
import org.springframework.beans.factory.annotation.Autowired;

import javax.annotation.Resource;
import java.util.*;

public class MyShiroRealm extends AuthorizingRealm {

	@Autowired
	private SysPermissionService sysPermissionService;
	@Autowired
	private SysUserService sysUserService;
	@Autowired
	private SysUserRoleService sysUserRoleService;


	/**
	 * 获取认证信息
	 */
	@Override
	protected AuthenticationInfo doGetAuthenticationInfo(AuthenticationToken authcToken) throws AuthenticationException {
		ShiroToken token = (ShiroToken) authcToken;

		// 通过表单接收的用户名
		String username = token.getUsername();
		String pwd = token.getPswd();

		SysUser user = sysUserService.findSysUserByUserName(username);
		if(null == user){
			throw new AccountException("帐号或密码不正确。");
		} else if(!pwd.equals(user.getPswd())){
			throw new AccountException("帐号或密码不正确。");
		} else if(0 == user.getUserStatus()){
			throw new DisabledAccountException("帐号已经禁止登录！");
		} else {
			sysUserService.updateLastLoginTime(username, new Date());
		}
		return new SimpleAuthenticationInfo(user, user.getPswd(), getName());
	}


	/**
	 * 获取授权信息
	 */
	@Override
	protected AuthorizationInfo doGetAuthorizationInfo(PrincipalCollection pc) {
		int userId = TokenManager.getUserId();

		SysUserRole sysUserRole = sysUserRoleService.findSysUserRoleByUid(userId);

		List<SysPermission> sysPermissionList = sysPermissionService.findSysPermissionByRoleid(sysUserRole.getRid());

		Set<String> permissions = new HashSet<>();
		if(null != sysPermissionList && sysPermissionList.size()>0){
			for(SysPermission permission : sysPermissionList){
				if(null == permission.getUrl() || "".equals(permission.getUrl())){
					continue;
				}
				permissions.add(permission.getUrl());
			}
		}
		SimpleAuthorizationInfo info = new SimpleAuthorizationInfo();
		info.setStringPermissions(permissions);


		return info;
	}
}