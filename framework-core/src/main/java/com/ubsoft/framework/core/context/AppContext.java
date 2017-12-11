package com.ubsoft.framework.core.context;

import java.util.Map;

import org.springframework.context.ApplicationContext;
import org.springframework.web.context.ContextLoader;

import com.ubsoft.framework.core.conf.AppConfig;

public class AppContext {

	public static Object getBean(String beanName) {
		try {
			ApplicationContext context = ContextLoader.getCurrentWebApplicationContext();
			if (context != null) {
				return ContextLoader.getCurrentWebApplicationContext().getBean(beanName);
			} else {
 				return AppConfig.sprintContext.getBean(beanName);
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		return null;

	}

	public static <T> T getBeanOfType(Class<T> type) {

		ApplicationContext context = ContextLoader.getCurrentWebApplicationContext();
		if (context != null) {
			Map beans = ContextLoader.getCurrentWebApplicationContext().getBeansOfType(type);
			if (beans.size() == 1) {
				return (T) beans.values().iterator().next();
			}

		} else {
			Map beans = AppConfig.sprintContext.getBeansOfType(type);
			if (beans.size() == 1) {
				return (T) beans.values().iterator().next();
			}
		}
		return null;
	}

}
