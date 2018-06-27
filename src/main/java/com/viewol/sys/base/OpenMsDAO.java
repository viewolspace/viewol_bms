package com.viewol.sys.base;

import com.youguu.core.dao.SqlDAO;
import org.apache.ibatis.session.SqlSessionFactory;

import javax.annotation.Resource;

public class OpenMsDAO<T> extends SqlDAO<T> {
	public OpenMsDAO() {
		super();
		setUseSimpleName(true);
	}

	@Resource(name = "openMsSessionFactory")
	@Override
	public void setSqlSessionFactory(SqlSessionFactory sqlSessionFactory) {
		super.setSqlSessionFactory(sqlSessionFactory);
	}

}
