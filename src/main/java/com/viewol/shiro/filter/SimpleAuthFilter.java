package com.viewol.shiro.filter;

import com.viewol.shiro.session.CustomSessionManager;
import com.viewol.shiro.session.SessionStatus;
import com.viewol.shiro.utils.ShiroFilterUtils;
import com.viewol.sys.response.SessionResponse;
import org.apache.shiro.session.Session;
import org.apache.shiro.subject.Subject;
import org.apache.shiro.web.filter.AccessControlFilter;
import org.apache.shiro.web.util.WebUtils;

import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletRequest;

/**
 * 判断是否踢出
 */
public class SimpleAuthFilter extends AccessControlFilter {

	@Override
	protected boolean isAccessAllowed(ServletRequest request,
									  ServletResponse response, Object mappedValue) throws Exception {

		HttpServletRequest httpRequest = ((HttpServletRequest) request);
		String url = httpRequest.getRequestURI();
		if (url.startsWith("/open/")) {
			return Boolean.TRUE;
		}
		Subject subject = getSubject(request, response);
		Session session = subject.getSession();
		SessionStatus sessionStatus = (SessionStatus) session.getAttribute(CustomSessionManager.SESSION_STATUS);
		if (null != sessionStatus && !sessionStatus.isOnlineStatus()) {
			//判断是不是Ajax请求
			if (ShiroFilterUtils.isAjax(request)) {
				SessionResponse rs = new SessionResponse();
				rs.setStatus(false);
				rs.setMsg("您已经被踢出，请重新登录！");
				rs.setSessionStatus(4000);
				ShiroFilterUtils.out(response, rs);
			}
			return Boolean.FALSE;
		}
		return Boolean.TRUE;
	}

	@Override
	protected boolean onAccessDenied(ServletRequest request,
									 ServletResponse response) throws Exception {

		//先退出
		Subject subject = getSubject(request, response);
		subject.logout();

		WebUtils.saveRequest(request);
		//再重定向
//		WebUtils.issueRedirect(request, response, ShiroFilterUtils.KICKED_OUT);
		return false;
	}
}
