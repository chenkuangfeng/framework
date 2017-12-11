package com.ubsoft.framework.scheduler.web.listener;

import javax.servlet.ServletContextEvent;
import javax.servlet.ServletContextListener;

public class SchedulerListener implements ServletContextListener {
	@Override
	public void contextDestroyed(ServletContextEvent arg0) {

	}
	@Override
	public void contextInitialized(ServletContextEvent event) {
		//AppConfig.initTask();
		
	}

}
