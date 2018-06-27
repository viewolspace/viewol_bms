package com.viewol.sys.interceptor;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.viewol.common.BaseResponse;
import com.youguu.core.logging.Log;
import com.youguu.core.logging.LogFactory;
import org.springframework.core.Ordered;
import org.springframework.web.method.HandlerMethod;
import org.springframework.web.servlet.handler.HandlerInterceptorAdapter;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.PrintWriter;
import java.lang.reflect.Method;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

/**
 * 一个用户 相同url 同时提交 相同数据 验证
 * 主要通过 session中保存到的url 和 请求参数。如果和上次相同，则是重复提交表单
 */
public class RepeatInterceptor extends HandlerInterceptorAdapter implements Ordered {

	private static final Log logger = LogFactory.getLog(RepeatInterceptor.class);

	@Override
	public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws
			Exception {
		response.setHeader("Content-type", "text/html;charset=UTF-8");
		response.setCharacterEncoding("UTF-8");

		if (handler instanceof HandlerMethod) {
			HandlerMethod handlerMethod = (HandlerMethod) handler;
			Method method = handlerMethod.getMethod();
			Repeat annotation = method.getAnnotation(Repeat.class);
			if (annotation != null) {
				if (repeatDataValidator(request)){//如果重复相同数据
					BaseResponse baseResponse = new BaseResponse();
					baseResponse.setStatus(false);
					baseResponse.setMsg("重复提交");
					PrintWriter out = response.getWriter();
					out.write(JSON.toJSONString(baseResponse));
					out.flush();
					out.close();
					return false;
				} else{
					return true;
				}
			}
			return true;
		} else {
			return super.preHandle(request, response, handler);
		}
	}

	/**
	 * 验证同一个url数据是否相同提交  ,相同返回true
	 *
	 * @param httpServletRequest
	 * @return
	 */
	public boolean repeatDataValidator(HttpServletRequest httpServletRequest) {
		synchronized (RepeatInterceptor.class){
			long now = new Date().getTime();

			String url = httpServletRequest.getRequestURI();
			Map<String, String> map = new HashMap<>();
			map.put(url, JSON.toJSONString(httpServletRequest.getParameterMap()));
			map.put("time", String.valueOf(now));
			String nowUrlParams = JSON.toJSONString(map);

			if (httpServletRequest.getSession().getAttribute("repeatData") == null) {
				httpServletRequest.getSession().setAttribute("repeatData", nowUrlParams);
				return false;
			} else {

				Map preMap = (Map) JSONObject.parse(httpServletRequest.getSession().getAttribute("repeatData").toString());
				long preTime = Long.parseLong(preMap.get("time").toString());
				long diff = now - preTime;
				if (null != preMap.get(url) && preMap.get(url).equals(map.get(url)) && diff < 3000) {
					logger.error("重复提交，原参数："+httpServletRequest.getSession().getAttribute("repeatData")+", 新参数："+nowUrlParams);
					return true;
				} else {
					httpServletRequest.getSession().setAttribute("repeatData", nowUrlParams);

					return false;
				}

			}
		}
	}

	@Override
	public void afterCompletion(HttpServletRequest request, HttpServletResponse response, Object handler, Exception
			ex) throws Exception {

		response.setHeader("Content-type", "text/html;charset=UTF-8");
		response.setCharacterEncoding("UTF-8");

		if(null != ex){
			logger.error("系统异常", ex);

			BaseResponse baseResponse = new BaseResponse();
			baseResponse.setStatus(false);
			baseResponse.setMsg("请联系管理员：异常【"+ex.getMessage()+"】");
			PrintWriter out = response.getWriter();
			out.write(JSON.toJSONString(baseResponse));
			out.flush();
			out.close();
		}
	}

	@Override
	public int getOrder() {
		return 1;
	}
}