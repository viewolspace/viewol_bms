package com.youguu.sys.pojo;

import java.io.Serializable;
import java.util.Date;

/**
 * 角色权限对应关系
 */
public class SysRolePermission implements Serializable {
	private static final long serialVersionUID = 1L;
	private int id;
	private int rid;//角色ID
	private int pid;//权限ID
	private Date createTime;

	public SysRolePermission() {
	}

	public SysRolePermission(int rid, int pid) {
		this.rid = rid;
		this.pid = pid;
	}

	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public int getRid() {
		return rid;
	}

	public void setRid(int rid) {
		this.rid = rid;
	}

	public int getPid() {
		return pid;
	}

	public void setPid(int pid) {
		this.pid = pid;
	}

	public Date getCreateTime() {
		return createTime;
	}

	public void setCreateTime(Date createTime) {
		this.createTime = createTime;
	}
}