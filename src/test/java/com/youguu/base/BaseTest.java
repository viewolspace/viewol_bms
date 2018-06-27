package com.youguu.base;

import com.viewol.sys.base.ContextLoader;
import org.junit.Before;
import org.junit.BeforeClass;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;

/**
 * Created by leo on 2017/11/24.
 */
public abstract class BaseTest {
	protected static ApplicationContext ctx = new AnnotationConfigApplicationContext(ContextLoader.class);
	protected static String name;

	protected static Object getBean(String name) {
		return ctx.getBean(name);
	}

	protected static Object getBean(Class clazz) {
		return ctx.getBean(clazz);
	}

	@BeforeClass
	public static void setUpBeforeClass() throws Exception {
	}

	@Before
	public void setUp() throws Exception {
	}
}
