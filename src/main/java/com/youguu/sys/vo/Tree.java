package com.youguu.sys.vo;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.youguu.sys.pojo.SysPermission;

import java.util.ArrayList;
import java.util.List;

public class Tree {
	private JSONArray array = new JSONArray();
	private List<SysPermission> nodes;

	public Tree(List<SysPermission> nodes) {
		this.nodes = nodes;
	}

	public JSONArray buildTree() {
		for (SysPermission node : nodes) {
			if(node.getType()==1){
				continue;
			}
			if(node.getParentId()==0){
				JSONObject object = new JSONObject();
				object.put("id", node.getId());
				object.put("menuName", node.getName());
				object.put("menuUrl", node.getUrl());

				build(node, object);

				array.add(object);
			}
		}
		return array;
	}

	private void build(SysPermission node, JSONObject object) {
		List<SysPermission> children = getChildren(node);
		if (!children.isEmpty()) {
			JSONArray childArray = new JSONArray();
			for (SysPermission child : children) {
				JSONObject childObject = new JSONObject();
				childObject.put("id", child.getId());
				childObject.put("menuName", child.getName());
				childObject.put("menuUrl", child.getUrl());
				build(child, childObject);

				childArray.add(childObject);
			}
			object.put("children", childArray);
		}
	}

	private List<SysPermission> getChildren(SysPermission node) {
		List<SysPermission> children = new ArrayList<>();
		Integer id = node.getId();
		for (SysPermission child : nodes) {
			if (id==child.getParentId()) {
				children.add(child);
			}
		}
		return children;
	}
}  