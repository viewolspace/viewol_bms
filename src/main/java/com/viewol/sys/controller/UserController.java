package com.viewol.sys.controller;

import com.viewol.common.BaseResponse;
import com.viewol.common.GridBaseResponse;
import com.viewol.shiro.token.TokenManager;
import com.viewol.sys.interceptor.Repeat;
import com.viewol.sys.log.annotation.MethodLog;
import com.viewol.sys.pojo.OnlineSysUser;
import com.viewol.sys.pojo.SysUser;
import com.viewol.sys.pojo.SysUserRole;
import com.viewol.sys.response.OnlineUserResponse;
import com.viewol.sys.service.SysUserRoleService;
import com.viewol.sys.service.SysUserService;
import com.viewol.sys.utils.Constants;
import com.youguu.core.util.MD5;
import com.youguu.core.util.PageHolder;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.annotation.Resource;
import java.util.Date;
import java.util.Map;

/**
 * Created by leo on 2017/11/29.
 */
@Controller
@RequestMapping("user")
public class UserController {

	@Resource
	private SysUserService sysUserService;
	@Resource
	private SysUserRoleService sysUserRoleService;

	@RequestMapping(value = "/addUser", method = RequestMethod.POST)
	@ResponseBody
	@MethodLog(module = Constants.SYS_USER, desc = "添加用户")
	@Repeat
	public BaseResponse addUser(String userName, String password, String email, String phone, String realName, Integer
			roleId, Integer userStatus) {
		SysUser sysUser = new SysUser();
		sysUser.setUserName(userName);
		sysUser.setRealName(realName);
		sysUser.setEmail(email);
		sysUser.setPhone(phone);
		sysUser.setPswd(new MD5().getMD5ofStr(password).toLowerCase());
		sysUser.setUserStatus(userStatus);
		sysUser.setCreateTime(new Date());
		int result = sysUserService.saveSysUser(sysUser);

		BaseResponse rs = new BaseResponse();
		if(result>0){
			SysUserRole userRole = new SysUserRole();
			userRole.setUid(result);
			userRole.setRid(roleId);
			userRole.setCreateTime(new Date());
			sysUserRoleService.saveSysUserRole(userRole);

			rs.setStatus(true);
			rs.setMsg("添加成功");
		} else {
			rs.setStatus(false);
			rs.setMsg("添加失败");
		}

		return rs;
	}

	@RequestMapping(value = "/updateUser", method = RequestMethod.POST)
	@ResponseBody
	@MethodLog(module = Constants.SYS_USER, desc = "修改用户")
	@Repeat
	public BaseResponse updateUser(Integer id, String userName, String password, String email, String phone,
								   String realName, Integer roleId, Integer userStatus) {
		BaseResponse rs = new BaseResponse();
		if(TokenManager.getUserId() == id){
			rs.setStatus(false);
			rs.setMsg("无权限修改，请联系超级管理员");
			return rs;
		}
		SysUser sysUser = sysUserService.getSysUser(id);
		if(null==sysUser){
			rs.setStatus(false);
			rs.setMsg("用户不存在或系统异常");
		}

		sysUser.setUserName(userName);
		sysUser.setRealName(realName);
		sysUser.setEmail(email);
		sysUser.setPhone(phone);
//		sysUser.setPswd(password);
		sysUser.setUserStatus(userStatus);
		int result = sysUserService.updateSysUser(sysUser);

		if(result>0){
			SysUserRole userRole = new SysUserRole();
			userRole.setUid(id);
			userRole.setRid(roleId);
			sysUserRoleService.updateSysUserRole(userRole);

			rs.setStatus(true);
			rs.setMsg("修改成功");
		} else {
			rs.setStatus(false);
			rs.setMsg("修改失败");
		}

		return rs;
	}

	@RequestMapping(value = "/deleteUser")
	@ResponseBody
	@MethodLog(module = Constants.SYS_USER, desc = "删除用户")
	@Repeat
	public BaseResponse deleteUser(int id) {
		BaseResponse rs = new BaseResponse();
		if(TokenManager.getUserId() == id){
			rs.setStatus(false);
			rs.setMsg("无权限删除，请联系超级管理员");
			return rs;
		}

		int result = sysUserService.deleteSysUser(id);
		if(result>0){
			sysUserRoleService.deleteSysUserRoleByUid(id);
			rs.setStatus(true);
			rs.setMsg("删除成功");
		} else {
			rs.setStatus(false);
			rs.setMsg("删除失败");
		}

		return rs;
	}

	/**
	 * 重置密码
	 * @param id 用户ID
	 * @return
	 */
	@RequestMapping(value = "/resetPwd", method = RequestMethod.POST)
	@ResponseBody
	@MethodLog(module = Constants.SYS_USER, desc = "重置密码")
	@Repeat
	public BaseResponse resetPwd(int id) {
		BaseResponse rs = new BaseResponse();

		SysUser sysUser = sysUserService.getSysUser(id);
		if(null != sysUser){
			String new_password = new MD5().getMD5ofStr("123456").toLowerCase();
			int result = sysUserService.updatePwd(sysUser.getUserName(), sysUser.getPswd(), new_password);

			if(result>0){
				rs.setStatus(true);
				rs.setMsg("重置成功");
			} else {
				rs.setStatus(false);
				rs.setMsg("重置失败");
			}
		} else {
			rs.setStatus(false);
			rs.setMsg("用户不存在");
		}



		return rs;
	}


	@RequestMapping(value = "/userlist", method = RequestMethod.POST)
	@ResponseBody
	public GridBaseResponse userList(@RequestParam(value="userId", defaultValue="0") int userId,
									 String realName,
									 @RequestParam(value = "page", defaultValue = "1") int page,
									 @RequestParam(value = "limit", defaultValue = "10") int limit) {

		GridBaseResponse rs = new GridBaseResponse();
		rs.setCode(0);
		rs.setMsg("ok");

		PageHolder<SysUser> pageHolder = sysUserService.querySysUserByPage(TokenManager.getAppId(), userId, realName, page, limit);
		if(null != pageHolder.getList()){
			rs.setData(pageHolder.getList());
			rs.setCount(pageHolder.getTotalCount());
		}

		return rs;
	}


	@RequestMapping(value = "/updatePwd", method = RequestMethod.POST)
	@ResponseBody
	@MethodLog(module = Constants.SYS_USER, desc = "修改密码")
	@Repeat
	public BaseResponse updatePwd(String old_password, String new_password, String confirm_password) {
		BaseResponse rs = new BaseResponse();
		String userName = TokenManager.getUserName();
		if("admin".equals(userName)){
			rs.setStatus(false);
			rs.setMsg("管理员不能修改密码");
			return rs;
		}
		int userId = TokenManager.getUserId();
		SysUser sysUser = sysUserService.getSysUser(userId);
		old_password = new MD5().getMD5ofStr(old_password).toLowerCase();
		new_password = new MD5().getMD5ofStr(new_password).toLowerCase();
		confirm_password = new MD5().getMD5ofStr(confirm_password).toLowerCase();

		if(!old_password.equals(sysUser.getPswd())){
			rs.setStatus(false);
			rs.setMsg("旧密码输入错误");
			return rs;
		}

		if(!new_password.equals(confirm_password)){
			rs.setStatus(false);
			rs.setMsg("新密码和确认密码不一致");
			return rs;
		}

		sysUser.setPswd(new_password);
		int result = sysUserService.updatePwd(sysUser.getUserName(), old_password, new_password);


		if(result>0){
			sysUser = sysUserService.getSysUser(userId);
			TokenManager.login(sysUser, Boolean.TRUE);//重新登录
			rs.setStatus(true);
			rs.setMsg("密码修改成功");
		} else {
			rs.setStatus(false);
			rs.setMsg("密码修改失败");
		}

		return rs;
	}


	/**
	 * 查询在线用户
	 * @return
	 */
	@RequestMapping(value = "/onlineUserList")
	@ResponseBody
	public GridBaseResponse onlineUserList() {

		GridBaseResponse rs = new GridBaseResponse();
		rs.setCode(0);
		rs.setMsg("ok");

		return rs;
	}

	/**
	 * 查询在线用户(根据SessionId查询)
	 * @return
	 */
	@RequestMapping(value = "/getOnlineUser")
	@ResponseBody
	public OnlineUserResponse getOnlineUser(String sessionId) {

		OnlineUserResponse rs = new OnlineUserResponse();
		rs.setStatus(true);
		rs.setMsg("ok");

		return rs;
	}

	/**
	 * 剔出用户
	 * @return
	 */
	@RequestMapping(value = "/changeSessionStatus")
	@ResponseBody
	@MethodLog(module = Constants.SYS_USER, desc = "踢出用户")
	public OnlineUserResponse changeSessionStatus(String sessionId) {

		OnlineUserResponse rs = new OnlineUserResponse();


		return rs;
	}
}
