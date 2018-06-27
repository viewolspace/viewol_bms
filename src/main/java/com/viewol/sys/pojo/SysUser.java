package com.viewol.sys.pojo;

import java.io.Serializable;
import java.util.Date;

/**
 * 系统用户
 */
public class SysUser implements Serializable {
	private static final long serialVersionUID = 1L;

	public static final Integer _0 = new Integer(0);//0:禁止登录
	public static final Integer _1 = new Integer(1);//1:有效

	private int id;
	private String userName;
	private String realName;
	private String email;
	private String phone;
	private String pswd;
	private Date createTime;
	private Date lastLoginTime;//最后登录时间
	private int userStatus;// 1:有效，0:禁止登录


	private int roleId;//角色ID
	private String roleCode;//角色编码
	private String roleName;//角色名称

	private int appId;//应用ID，目前设计一个用户最多只有一个应用数据查看权限

	public SysUser() {
	}

	public SysUser(SysUser user) {
		this.id = user.getId();
		this.userName = user.getUserName();
		this.realName = user.getRealName();
		this.email = user.getEmail();
		this.phone = user.getPhone();
		this.pswd = user.getPswd();
		this.createTime = user.getCreateTime();
		this.lastLoginTime = user.getLastLoginTime();
		this.userStatus = user.getUserStatus();
		this.appId = user.getAppId();
	}

	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public String getUserName() {
		return userName;
	}

	public void setUserName(String userName) {
		this.userName = userName;
	}

	public String getRealName() {
		return realName;
	}

	public void setRealName(String realName) {
		this.realName = realName;
	}

	public String getEmail() {
		return email;
	}

	public void setEmail(String email) {
		this.email = email;
	}

	public String getPhone() {
		return phone;
	}

	public void setPhone(String phone) {
		this.phone = phone;
	}

	public String getPswd() {
		return pswd;
	}

	public void setPswd(String pswd) {
		this.pswd = pswd;
	}

	public Date getCreateTime() {
		return createTime;
	}

	public void setCreateTime(Date createTime) {
		this.createTime = createTime;
	}

	public Date getLastLoginTime() {
		return lastLoginTime;
	}

	public void setLastLoginTime(Date lastLoginTime) {
		this.lastLoginTime = lastLoginTime;
	}

	public int getUserStatus() {
		return userStatus;
	}

	public void setUserStatus(int userStatus) {
		this.userStatus = userStatus;
	}

	public int getRoleId() {
		return roleId;
	}

	public void setRoleId(int roleId) {
		this.roleId = roleId;
	}

	public String getRoleCode() {
		return roleCode;
	}

	public void setRoleCode(String roleCode) {
		this.roleCode = roleCode;
	}

	public String getRoleName() {
		return roleName;
	}

	public void setRoleName(String roleName) {
		this.roleName = roleName;
	}

	public int getAppId() {
		return appId;
	}

	public void setAppId(int appId) {
		this.appId = appId;
	}
}