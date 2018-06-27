package com.viewol.sys.controller;

import com.alibaba.fastjson.JSONObject;
import com.viewol.common.BaseResponse;
import com.youguu.core.util.MD5;
import com.viewol.shiro.token.TokenManager;
import com.viewol.sys.pojo.SysUser;
import com.viewol.sys.pojo.SysUserRole;
import com.viewol.sys.response.LoginResponse;
import com.viewol.sys.service.SysPermissionService;
import com.viewol.sys.service.SysUserRoleService;
import com.viewol.sys.service.SysUserService;
import com.viewol.sys.utils.SecurityCode;
import com.viewol.sys.utils.SecurityImage;
import org.apache.shiro.authc.AccountException;
import org.apache.shiro.authc.DisabledAccountException;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.OutputStream;
import java.util.Date;

@Controller
@RequestMapping("login")
public class LoginController {
	@Resource
	private SysPermissionService sysPermissionService;
	@Resource
	private SysUserService sysUserService;
	@Resource
	private SysUserRoleService sysUserRoleService;


	/**
	 * 跳转到登录页面
	 *
	 * @return
	 */
	@RequestMapping(value = "/toLogin")
	public String toLogin() {
		// 跳转到/page/login.jsp页面
		return "login";
	}

	/**
	 * 实现用户登录
	 *
	 * @param userName
	 * @param password
	 * @return
	 */
	@RequestMapping(value = "/submitLogin", method = RequestMethod.POST)
	@ResponseBody
	public LoginResponse Login(HttpServletRequest request, String userName, String password, String rememberMe, String checkcode) {
		LoginResponse rs = new LoginResponse();
		rs.setStatus(true);
		rs.setMsg("登录成功");

		String securityCode = (String)request.getSession().getAttribute("securityCode");
		if(null ==securityCode || !securityCode.equals(checkcode)){
			rs.setStatus(false);
			rs.setMsg("验证码输入错误");
			return rs;
		}

		password = new MD5().getMD5ofStr(password).toLowerCase();
		SysUser user = sysUserService.findSysUserByUserName(userName);
		user.setUserName(userName);
		user.setPswd(password);

		boolean remember = false;
		if("on".equals(rememberMe)){
			remember = true;
		}
		try{
			TokenManager.login(user, true);
		} catch (DisabledAccountException e){
			rs.setStatus(false);
			rs.setMsg(e.getMessage());
		} catch (AccountException e){
			rs.setStatus(false);
			rs.setMsg(e.getMessage());
		}

		if(rs.isStatus()){
			JSONObject userJson = new JSONObject();
			userJson.put("id", user.getId());
			userJson.put("name", user.getRealName());
			userJson.put("userName", userName);
			userJson.put("roleCode", user.getRoleCode());

			rs.setData(userJson);
		}
		return rs;
	}


	/**
	 * 解锁校验用户名，密码
	 *
	 * @param userName
	 * @param password
	 * @return
	 */
	@RequestMapping(value = "/unlock", method = RequestMethod.POST)
	@ResponseBody
	public LoginResponse unlock(HttpServletRequest request, String userName, String password) {
		LoginResponse rs = new LoginResponse();
		rs.setStatus(true);
		rs.setMsg("解锁成功");

		password = new MD5().getMD5ofStr(password).toLowerCase();
		SysUser user = sysUserService.findSysUserByUserName(userName);
		if(null == user){
			rs.setStatus(false);
			rs.setMsg("Session失效，请重新登录");
			return rs;
		}

		if(!password.equals(user.getPswd())){
			rs.setStatus(false);
			rs.setMsg("密码不正确，请重新输入");
		}

		return rs;
	}

	/**
	 * 登录时获取图形验证码
	 * @param request
	 * @param response
	 * @param type 1-登录验证码；2-注册验证码
	 */
	@RequestMapping("/getValidImg")
	public synchronized void getValidImg(HttpServletRequest request, HttpServletResponse response, int type){
		request.getSession().removeAttribute("repeatData");
		String securityCode = SecurityCode.getSecurityCode();

		if(type == 1){
			request.getSession().setAttribute("securityCode", securityCode);
		} else {
			request.getSession().setAttribute("registerCode", securityCode);
		}

		byte[] image = SecurityImage.getImageAsInputStream(securityCode.replaceAll("", " "));

		try {
			OutputStream os = response.getOutputStream();
			os.write(image);
			os.flush();
			os.close();
		} catch (IOException e) {
			//TODO
		}
	}

	/**
	 * 退出
	 *
	 * @return
	 */
	@RequestMapping(value = "logout", method = RequestMethod.GET)
	@ResponseBody
	public BaseResponse logout(HttpServletRequest request) {
		BaseResponse rs = new BaseResponse();
		try {
			TokenManager.logout();

			rs.setStatus(true);
			rs.setMsg("OK");
		} catch (Exception e) {
			rs.setStatus(false);
			rs.setMsg("退出异常");
		}
		return rs;
	}

	/**
	 * 注册并自动登录
	 * @param request
	 * @param userName
	 * @param password
	 * @param repassword
	 * @param email
	 * @param phone
	 * @param realName
	 * @param checkcode
	 * @return
	 */
	@RequestMapping(value = "/register", method = RequestMethod.POST)
	@ResponseBody
	public LoginResponse register(HttpServletRequest request, String userName, String password, String repassword,
								  String email, String phone, String realName, String checkcode) {
		LoginResponse rs = new LoginResponse();
		rs.setStatus(true);
		rs.setMsg("注册成功");

		String registerCode = (String)request.getSession().getAttribute("registerCode");
		if(null ==registerCode || !registerCode.equals(checkcode)){
			rs.setStatus(false);
			rs.setMsg("验证码输入错误");
			return rs;
		}

		//注册用户
		SysUser sysUser = new SysUser();
		sysUser.setUserName(userName);
		sysUser.setRealName(realName);
		sysUser.setEmail(email);
		sysUser.setPhone(phone);
		sysUser.setPswd(new MD5().getMD5ofStr(password).toLowerCase());
		sysUser.setUserStatus(1);
		sysUser.setCreateTime(new Date());
		int result = sysUserService.saveSysUser(sysUser);

		if(result>0){
			SysUserRole userRole = new SysUserRole();
			userRole.setUid(result);
			userRole.setRid(2);//只注册了，没有开发权限
			userRole.setCreateTime(new Date());
			sysUserRoleService.saveSysUserRole(userRole);

			rs.setStatus(true);
			rs.setMsg("注册成功");
		} else {
			rs.setStatus(false);
			rs.setMsg("注册失败");
		}


		if(!password.equals(repassword)){
			rs.setStatus(false);
			rs.setMsg("两次输入密码不一致");
			return rs;
		}

		try{
			TokenManager.login(sysUser, true);
		} catch (DisabledAccountException e){
			rs.setStatus(false);
			rs.setMsg(e.getMessage());
		} catch (AccountException e){
			rs.setStatus(false);
			rs.setMsg(e.getMessage());
		}

		if(rs.isStatus()){
			JSONObject userJson = new JSONObject();
			userJson.put("id", sysUser.getId());
			userJson.put("name", sysUser.getRealName());
			userJson.put("userName", userName);
			userJson.put("roleCode", sysUser.getRoleCode());
			rs.setData(userJson);
		}
		return rs;
	}
}