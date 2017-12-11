package com.ubsoft.framework.esb.web;

import javax.servlet.ServletContextEvent;
import javax.servlet.ServletContextListener;

import org.springframework.context.ApplicationContext;
import org.springframework.web.context.support.WebApplicationContextUtils;

import com.ubsoft.framework.esb.service.IEsbEngine;

/**
 * 
 * @ClassName: EsbListener
 * @Description: 用于web程序来初始化ESB信息
 * @author chenkf
 * @date 2017-2-23 上午10:57:39
 * @version V1.0
 */
public class EsbListener implements ServletContextListener {
	@Override
	public void contextDestroyed(ServletContextEvent arg0) {
		
	}

	@Override
	public void contextInitialized(ServletContextEvent event) {
		ApplicationContext context = WebApplicationContextUtils.getWebApplicationContext(event.getServletContext());
		IEsbEngine engine = (IEsbEngine) context.getBean("esbEngine");
		engine.initEngine();
		

	}

}
