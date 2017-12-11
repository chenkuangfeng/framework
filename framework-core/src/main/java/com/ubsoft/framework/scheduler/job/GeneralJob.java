package com.ubsoft.framework.scheduler.job;

import java.sql.Timestamp;
import java.util.UUID;

import org.quartz.DisallowConcurrentExecution;
import org.quartz.Job;
import org.quartz.JobExecutionContext;
import org.quartz.JobExecutionException;
import org.quartz.PersistJobDataAfterExecution;
import org.springframework.beans.factory.annotation.Autowired;

import com.ubsoft.framework.core.conf.AppConfig;
import com.ubsoft.framework.core.service.ITransactionService;
import com.ubsoft.framework.core.support.util.IPUtil;
import com.ubsoft.framework.esb.cache.MemoryEndpoint;
import com.ubsoft.framework.esb.entity.Endpoint;
import com.ubsoft.framework.esb.model.Exchange;
import com.ubsoft.framework.esb.model.Message;
import com.ubsoft.framework.esb.service.IEsbEngine;
import com.ubsoft.framework.scheduler.cache.MemoryTask;
import com.ubsoft.framework.scheduler.entity.Task;
import com.ubsoft.framework.scheduler.entity.TaskLog;
import com.ubsoft.framework.scheduler.service.ITaskLogService;

/**
 * 
 * @ClassName: GeneralJob
 * @Description: 任务调度基础Job，ESB有对应的EsbJob，BI有对应的BI Job
 * @author chenkf
 * @date 2017-2-22 下午04:48:20
 * @version V1.0
 */
@PersistJobDataAfterExecution
@DisallowConcurrentExecution
public class GeneralJob implements Job {

	private static String ip = IPUtil.getLocalIP();
	@Autowired
	ITaskLogService taskLogService;
	@Autowired
	ITransactionService transaciontService;
	@Autowired
	IEsbEngine engine;

	@Override
	public void execute(JobExecutionContext context) throws JobExecutionException {
		String jobName = context.getJobDetail().getKey().getName();
		// job比初始化缓存先执行,解决此问题
		while (!AppConfig.hadInitCache) {
			// System.out.println(".......");
		}
		Task task = MemoryTask.getInstance().get(jobName);// 根据从内存中获取
		Timestamp start = new Timestamp(System.currentTimeMillis());
		Timestamp end = null;
		String serviceName = task.getServiceName();
		try {
			// JobDataMap jobDataMap = context.getJobDetail().getJobDataMap();
			if (task != null) {
				if (task.getTaskType().equals("SQL")) {
					serviceName = "sqlJobService";
				} 
				if (task.getTaskType().equals("API")) {
					Endpoint ep = MemoryEndpoint.getInstance().get(serviceName);
					Exchange ex = new Exchange();
					ex.setEndpoint(ep);
					Message msg = new Message();
					msg.setMessageId(task.getTaskKey());
					ex.setIn(msg);
					engine.process(ep, ex);
					end = new Timestamp(System.currentTimeMillis());
				} else {
					transaciontService.execute(task.getUnitName(), serviceName, "execute", new Object[] { task });
					end = new Timestamp(System.currentTimeMillis());
				}
				if (task.isLog()) {
					this.log(task, start, end, "INFOR", "成功");
				}
			}
		} catch (Exception ex) {
			if (task != null) {
				this.log(task, start, end, "ERROR", ex.getMessage());
			}
		}

	}
	private void log(Task task, Timestamp start, Timestamp end, String level, String msg) {
		TaskLog log = new TaskLog();
		end = new Timestamp(System.currentTimeMillis());
		log.setTaskKey(task.getTaskKey());
		log.setStartTime(start);
		log.setEndTime(end);
		log.setServer(ip);
		log.setLogLevel(level);
		log.setMsg(msg);
		taskLogService.save(log);
	}
}
