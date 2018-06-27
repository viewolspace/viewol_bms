package com.youguu.sys.service;

import com.youguu.core.util.PageHolder;
import com.youguu.sys.pojo.SysLog;

/**
 * Created by leo on 2017/12/18.
 */
public interface SysLogService {
	PageHolder<SysLog> listSysLogByPage(String moduleName, String methodName, String userName, String ip, String startDate,
										String endDate,  int appId, int pageIndex, int pageSize);
}
