package com.youguu.shiro.filter;

import com.youguu.shiro.token.TokenManager;
import com.youguu.shiro.utils.ShiroFilterUtils;
import com.youguu.sys.pojo.SysUser;
import com.youguu.sys.response.SessionResponse;
import org.apache.shiro.web.filter.AccessControlFilter;

import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;

/**
 * 判断登录
 */
public class LoginFilter extends AccessControlFilter {

	final static Class<LoginFilter> CLASS = LoginFilter.class;

	@Override
	protected boolean isAccessAllowed(ServletRequest request,
									  ServletResponse response, Object mappedValue) throws Exception {

		SysUser token = TokenManager.getToken();

		if (null != token || isLoginRequest(request, response)) {// && isEnabled()
			return Boolean.TRUE;
		}

		if (ShiroFilterUtils.isAjax(request)) {// ajax请求
			SessionResponse rs = new SessionResponse();
			rs.setStatus(false);
			rs.setMsg("当前用户没有登录!");
			rs.setSessionStatus(2000);
			ShiroFilterUtils.out(response, rs);
		}

		return Boolean.FALSE;

	}

	@Override
	protected boolean onAccessDenied(ServletRequest request, ServletResponse response)
			throws Exception {
		saveRequestAndRedirectToLogin(request, response);
		return Boolean.FALSE;
	}


}
