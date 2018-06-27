package com.viewol.shiro.filter;

import com.viewol.shiro.utils.ShiroFilterUtils;
import com.viewol.sys.response.SessionResponse;
import org.apache.shiro.subject.Subject;
import org.apache.shiro.web.filter.AccessControlFilter;
import org.apache.shiro.web.util.WebUtils;

import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletRequest;

/**
 * 权限校验 Filter
 */
public class PermissionFilter extends AccessControlFilter {
	@Override
	protected boolean isAccessAllowed(ServletRequest request,
									  ServletResponse response, Object mappedValue) throws Exception {

		//先判断带参数的权限判断
		Subject subject = getSubject(request, response);
		if (null != mappedValue) {
			String[] arra = (String[]) mappedValue;
			for (String permission : arra) {
				if (subject.isPermitted(permission)) {
					return Boolean.TRUE;
				}
			}
		}
		HttpServletRequest httpRequest = ((HttpServletRequest) request);

		String uri = httpRequest.getRequestURI();//获取URI
		String basePath = httpRequest.getContextPath();//获取basePath
		if (null != uri && uri.startsWith(basePath)) {
			uri = uri.replaceFirst(basePath, "");
		}
		if (subject.isPermitted(uri)) {
			return Boolean.TRUE;
		}

		if(ShiroFilterUtils.isAjax(request)){
			SessionResponse rs = new SessionResponse();
			rs.setStatus(false);
			rs.setMsg("当前用户没有访问权限!");
			rs.setSessionStatus(3000);
			ShiroFilterUtils.out(response, rs);
		}

		return Boolean.FALSE;
	}

	@Override
	protected boolean onAccessDenied(ServletRequest request,
									 ServletResponse response) throws Exception {

		Subject subject = getSubject(request, response);
		if (null == subject.getPrincipal()) {//表示没有登录，重定向到登录页面
			saveRequest(request);
			WebUtils.issueRedirect(request, response, "toLogin.do");
		} else {
			WebUtils.issueRedirect(request, response, ShiroFilterUtils.UNAUTHORIZED);
		}
		return Boolean.FALSE;
	}

}
