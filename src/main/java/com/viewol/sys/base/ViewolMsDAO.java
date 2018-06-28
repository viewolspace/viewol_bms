package com.viewol.sys.base;

import com.youguu.core.dao.SqlDAO;
import org.apache.ibatis.session.SqlSessionFactory;

import javax.annotation.Resource;

public class ViewolMsDAO<T> extends SqlDAO<T> {
	public ViewolMsDAO() {
		super();
		setUseSimpleName(true);
	}

	@Resource(name = "viewolMsSessionFactory")
	@Override
	public void setSqlSessionFactory(SqlSessionFactory sqlSessionFactory) {
		super.setSqlSessionFactory(sqlSessionFactory);
	}

}
