package com.youguu.sys.log.service;

import com.alibaba.fastjson.JSON;
import com.youguu.shiro.token.TokenManager;
import com.youguu.sys.dao.SysLogDAO;
import com.youguu.sys.log.annotation.MethodLog;
import com.youguu.sys.pojo.SysLog;
import com.youguu.sys.pojo.SysUser;
import com.youguu.sys.utils.IpUtil;
import org.apache.shiro.SecurityUtils;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Pointcut;
import org.springframework.core.LocalVariableTableParameterNameDiscoverer;
import org.springframework.core.Ordered;
import org.springframework.stereotype.Service;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import java.lang.reflect.Method;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;


@Service("logService")
@Aspect
public class LogService implements Ordered {

	@Resource
	private SysLogDAO sysLogDAO;

	private static LocalVariableTableParameterNameDiscoverer parameterNameDiscovere = new
			LocalVariableTableParameterNameDiscoverer();


	public LogService() {
	}

	@Pointcut("@annotation(com.youguu.sys.log.annotation.MethodLog)")
	public void methodCachePointcut() {

	}

	@Around("methodCachePointcut()")
	public Object around(ProceedingJoinPoint point) throws Throwable {
		HttpServletRequest request = ((ServletRequestAttributes) RequestContextHolder.getRequestAttributes()).getRequest();
		String ip = IpUtil.getIpAddr(request);//操作IP
		SysUser user = (SysUser) SecurityUtils.getSubject().getPrincipal();
		String loginName;//操作人真是姓名
		Integer userId = -1;//操作人ID
		if (user != null) {
			loginName = user.getRealName();
			userId = user.getId();
		} else {
			loginName = "匿名用户";
		}

		Method method = getMethod(point);
		//获取注释信息
		MethodLog methodLogAnnotation = method.getAnnotation(MethodLog.class);
		String moduleName = methodLogAnnotation.module();
		String methodName = point.getSignature().getName();
		String methodDesc = methodLogAnnotation.desc();

		//获取方法中定义的参数名称
		String[] parameterNames = parameterNameDiscovere.getParameterNames(method);
		//获取参数实例对象
		Object[] args = point.getArgs();
		Map paramMap = new HashMap();
		for (int i = 0; i < parameterNames.length; i++) {
			paramMap.put(parameterNames[i], args[i]);
		}


		SysLog sysLog = new SysLog();
		sysLog.setIpAddress(ip);
		sysLog.setOperId(userId);
		sysLog.setUserName(loginName);
		sysLog.setModuleName(moduleName);
		sysLog.setMethodName(methodName);
		sysLog.setMethodDesc(methodDesc);
		try {
			sysLog.setOperContent(JSON.toJSONString(paramMap));
			if(sysLog.getOperContent().startsWith("{\"file\"")){
				sysLog.setOperContent("上传图片");
			}

		} catch (Exception e){

		}

		sysLog.setCreatetime(new Date());
		sysLog.setAppId(TokenManager.getAppId());
		sysLogDAO.saveSysLog(sysLog);
		return point.proceed();
	}

	/**
	 * 获取当前执行的方法
	 *
	 * @param joinPoint 连接点
	 * @return 方法
	 */
	private Method getMethod(ProceedingJoinPoint joinPoint) {
		//获取方法签名
		String methodName = joinPoint.getSignature().getName();
		//获取目标类的所有方法
		Method[] methods = joinPoint.getTarget().getClass().getMethods();
		Method resultMethod = null;
		//查询当前调用的方法
		for (Method method : methods) {
			if (method.getName().equals(methodName)) {
				//找到当前要执行的方法
				resultMethod = method;
				break;
			}
		}
		return resultMethod;
	}

	@Override
	public int getOrder() {
		return 2;
	}
}