package com.ubsoft.framework.esb.job;

import java.util.UUID;

import org.quartz.DisallowConcurrentExecution;
import org.quartz.Job;
import org.quartz.JobExecutionContext;
import org.quartz.JobExecutionException;
import org.springframework.beans.factory.annotation.Autowired;

import com.ubsoft.framework.core.support.util.IPUtil;
import com.ubsoft.framework.esb.cache.MemoryEndpoint;
import com.ubsoft.framework.esb.entity.Endpoint;
import com.ubsoft.framework.esb.model.Exchange;
import com.ubsoft.framework.esb.model.Message;
import com.ubsoft.framework.esb.service.IEsbEngine;

//@PersistJobDataAfterExecution
@DisallowConcurrentExecution
public class EsbJob implements Job {
	private static String ip = IPUtil.getLocalIP();
	@Autowired
	IEsbEngine engine;
	@Override
	public void execute(JobExecutionContext context) throws JobExecutionException {
		try {
			String jobName = context.getJobDetail().getKey().getName();
			Endpoint job = MemoryEndpoint.getInstance().get(jobName);
			try {
				Exchange ex = new Exchange();
				ex.setEndpoint(job);
				Message msg = new Message();
				msg.setMessageId(UUID.randomUUID().toString());
				ex.setIn(msg);
				engine.process(job, ex);
			} catch (Exception e) {
				e.printStackTrace();
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

}
