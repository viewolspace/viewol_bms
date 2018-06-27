package com.viewol.sys.pojo;

import java.io.Serializable;
import java.util.Date;

/**
 * 用户角色对应关系
 */
public class SysUserRole implements Serializable {
	private static final long serialVersionUID = 1L;
	private int id;
	private int uid;
	private int rid;
	private Date createTime;

	public SysUserRole(int uid, int rid) {
		this.uid = uid;
		this.rid = rid;
	}

	public SysUserRole() {
	}

	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public int getUid() {
		return uid;
	}

	public void setUid(int uid) {
		this.uid = uid;
	}

	public int getRid() {
		return rid;
	}

	public void setRid(int rid) {
		this.rid = rid;
	}

	public Date getCreateTime() {
		return createTime;
	}

	public void setCreateTime(Date createTime) {
		this.createTime = createTime;
	}
}