package com.youguu.sys.pojo;

import java.io.Serializable;
import java.util.Date;

/**
 * 权限模块
 */
public class SysPermission implements Serializable {
	private static final long serialVersionUID = 1L;
	private int id;
	private int parentId;//父ID
	private String name;//操作的名称
	private String url;//操作的url
	private int type;//类型
	private Date createTime;
	private String btnKey;//按钮类型
	private int levels;//层级

	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public String getUrl() {
		return url;
	}

	public void setUrl(String url) {
		this.url = url;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public int getType() {
		return type;
	}

	public void setType(int type) {
		this.type = type;
	}

	public Date getCreateTime() {
		return createTime;
	}

	public void setCreateTime(Date createTime) {
		this.createTime = createTime;
	}

	public int getParentId() {
		return parentId;
	}

	public void setParentId(int parentId) {
		this.parentId = parentId;
	}

	public String getBtnKey() {
		return btnKey;
	}

	public void setBtnKey(String btnKey) {
		this.btnKey = btnKey;
	}

	public int getLevels() {
		return levels;
	}

	public void setLevels(int levels) {
		this.levels = levels;
	}
}