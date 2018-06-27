package com.viewol.common;

import java.io.IOException;

import javax.servlet.Filter;
import javax.servlet.FilterChain;
import javax.servlet.FilterConfig;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * 项目名称：gamems    
 * 类名称：CharsetFilter    
 * 类描述：字符编码过滤器   
 * 创建人：shilei    
 * 创建时间：2013-6-17 下午3:44:35    
 */
public class CharsetFilter implements Filter{

	@Override
	public void doFilter(ServletRequest req, ServletResponse rep, FilterChain chain) throws IOException, ServletException {

		HttpServletRequest request = (HttpServletRequest) req;
        HttpServletResponse response = (HttpServletResponse)rep;
        
        request.setCharacterEncoding("UTF-8");
        response.setCharacterEncoding("UTF-8");

		chain.doFilter(request, response);	
	}

	@Override
	public void init(FilterConfig arg0) throws ServletException {

	}

	@Override
	public void destroy() {

	}


}
