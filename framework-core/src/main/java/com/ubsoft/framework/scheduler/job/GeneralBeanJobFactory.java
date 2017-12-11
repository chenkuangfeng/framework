package com.ubsoft.framework.scheduler.job;

import org.quartz.spi.TriggerFiredBundle;
import org.springframework.beans.factory.config.AutowireCapableBeanFactory;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;
import org.springframework.scheduling.quartz.SpringBeanJobFactory;

/**
 * 
* @ClassName: GeneralBeanJobFactory
* @Description: TODO
* @author chenkf
* @date 2017-2-21 下午04:16:55
* @version V1.0
 */
public final class GeneralBeanJobFactory extends SpringBeanJobFactory
		implements ApplicationContextAware {

	private transient AutowireCapableBeanFactory beanFactory;

	
	public void setApplicationContext(final ApplicationContext context) {
		beanFactory = context.getAutowireCapableBeanFactory();
	}

	
	protected Object createJobInstance(final TriggerFiredBundle bundle)
			throws Exception {
		final Object job = super.createJobInstance(bundle);
		beanFactory.autowireBean(job);		
		return job;
	}
}
