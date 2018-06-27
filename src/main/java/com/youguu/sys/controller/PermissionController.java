package com.youguu.sys.controller;

import com.alibaba.fastjson.JSONObject;
import com.youguu.common.BaseResponse;
import com.youguu.shiro.token.TokenManager;
import com.youguu.sys.interceptor.Repeat;
import com.youguu.sys.log.annotation.MethodLog;
import com.youguu.sys.pojo.SysPermission;
import com.youguu.sys.pojo.SysRolePermission;
import com.youguu.sys.pojo.SysUserRole;
import com.youguu.sys.response.ButtonPermissionResponse;
import com.youguu.sys.response.AllPermissionResponse;
import com.youguu.sys.response.MenuPermissionResponse;
import com.youguu.sys.service.SysPermissionService;
import com.youguu.sys.service.SysRolePermissionService;
import com.youguu.sys.service.SysUserRoleService;
import com.youguu.sys.utils.Constants;
import com.youguu.sys.vo.MenuVO;
import com.youguu.sys.vo.PermissionVO;
import com.youguu.sys.vo.Tree;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.annotation.Resource;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by leo on 2017/11/29.
 */
@Controller
@RequestMapping("permission")
public class PermissionController {

	@Resource
	private SysPermissionService sysPermissionService;
	@Resource
	private SysUserRoleService sysUserRoleService;
	@Resource
	private SysRolePermissionService sysRolePermissionService;

	/**
	 * 查询权限列表，全部
	 * @return
	 */
	@RequestMapping(value = "/queryAllPermission")
	@ResponseBody
	public AllPermissionResponse queryAllPermission(@RequestParam(value = "groupId", defaultValue = "0") int roleId) {
		AllPermissionResponse rs = new AllPermissionResponse();
		rs.setStatus(true);
		rs.setMsg("ok");
		int appId = TokenManager.getAppId();

		List<SysPermission> list = null;
		//给其他角色分配权限时，分配权限的范围不能超过自己。
		if(appId>0){
			list = sysPermissionService.findSysPermissionByAppid(TokenManager.getRoleId(), TokenManager.getAppId());
		} else {
//			list = sysPermissionService.findSysPermissionByAppid(TokenManager.getRoleId(), 0);
			list = sysPermissionService.queryAllSysPermission();
		}
		if(list!=null && list.size()>0){
			List<PermissionVO> volist = new ArrayList<>();
			for(SysPermission permission : list){
				PermissionVO vo = new PermissionVO();
				vo.setAuthValue("");
				vo.setBtnKey(permission.getBtnKey());
				vo.setChecked(false);
				vo.setId(permission.getId());
				vo.setLevels(permission.getLevels());
				vo.setMenuName(permission.getName());
				vo.setMenuUrl(permission.getUrl());
				vo.setParentId(permission.getParentId());
				vo.setType(permission.getType());
				volist.add(vo);
			}
			rs.setData(volist);
		}
		return rs;
	}

	@RequestMapping(value = "/addSysPermission", method = RequestMethod.POST)
	@ResponseBody
	@MethodLog(module = Constants.SYS_PERMISSION, desc = "添加权限")
	@Repeat
	public BaseResponse addSysPermission(String fatherName, String menuName, String menuUrl, int parentId, int type, String btnKey) {
		SysPermission sysPermission = new SysPermission();
		sysPermission.setParentId(parentId);
		sysPermission.setName(menuName);
		sysPermission.setUrl(menuUrl);
		sysPermission.setType(type);
		sysPermission.setBtnKey(btnKey);

		int result = sysPermissionService.saveSysPermission(sysPermission);
		BaseResponse rs = new BaseResponse();
		if(result>0){
			rs.setStatus(true);
			rs.setMsg("添加成功");
		} else {
			rs.setStatus(false);
			rs.setMsg("添加失败");
		}

		return rs;
	}

	@RequestMapping(value = "/updateSysPermission", method = RequestMethod.POST)
	@ResponseBody
	@MethodLog(module = Constants.SYS_PERMISSION, desc = "修改权限")
	@Repeat
	public BaseResponse updateSysPermission(int id, String fatherName, String menuName, String menuUrl, int parentId, int type, String btnKey) {
		BaseResponse rs = new BaseResponse();

		SysPermission sysPermission = sysPermissionService.getSysPermission(id);
		if(null == sysPermission){
			rs.setStatus(false);
			rs.setMsg("节点不存在");
			return rs;
		}

		sysPermission.setParentId(parentId);
		sysPermission.setName(menuName);
		sysPermission.setUrl(menuUrl);
		sysPermission.setType(type);
		sysPermission.setBtnKey(btnKey);

		int result = sysPermissionService.updateSysPermission(sysPermission);

		if(result>0){
			rs.setStatus(true);
			rs.setMsg("修改成功");
		} else {
			rs.setStatus(false);
			rs.setMsg("修改失败");
		}

		return rs;
	}

	@RequestMapping(value = "/deleteSysPermission", method = RequestMethod.POST)
	@ResponseBody
	@MethodLog(module = Constants.SYS_PERMISSION, desc = "删除权限")
	@Repeat
	public BaseResponse deleteSysPermission(int id) {
		int result = sysPermissionService.deleteSysPermission(id);
		BaseResponse rs = new BaseResponse();

		if(result>0){
			rs.setStatus(true);
			rs.setMsg("删除成功");
		} else {
			rs.setStatus(false);
			rs.setMsg("删除失败");
		}

		return rs;
	}

	/**
	 * 加载导航权限
	 * @return
	 */
	@RequestMapping(value = "/queryMenuPermission")
	@ResponseBody
	public MenuPermissionResponse queryMenuPermission(int id) {
		MenuPermissionResponse rs = new MenuPermissionResponse();
		rs.setStatus(true);
		rs.setMsg("ok");
		SysUserRole sysUserRole = sysUserRoleService.findSysUserRoleByUid(id);

		List<SysPermission> allList = sysPermissionService.findSysPermissionByRoleid(sysUserRole.getRid());

		Tree tree = new Tree(allList);

		rs.setData(tree.buildTree());

		return rs;
	}


	/**
	 * 加载按钮权限
	 * @param userId 用户ID
	 * @param id menuId
	 * @return
	 */
	@RequestMapping(value = "/queryBtnPermission")
	@ResponseBody
	public ButtonPermissionResponse queryBtnPermission(int userId, int id) {

		SysUserRole sysUserRole = sysUserRoleService.findSysUserRoleByUid(userId);

		List<SysPermission> permissionList = sysPermissionService.findSysPermissionByRoleidAndPermissionId(sysUserRole.getRid(), id);

		ButtonPermissionResponse rs = new ButtonPermissionResponse();
		rs.setStatus(true);
		rs.setMsg("ok");

		List<JSONObject> list = new ArrayList<>();

		if(null != permissionList){
			for(SysPermission permission : permissionList){
				JSONObject btn = new JSONObject();
				btn.put("btnKey", permission.getBtnKey());
				list.add(btn);
			}
		}

		rs.setData(list);
		return rs;
	}
}
