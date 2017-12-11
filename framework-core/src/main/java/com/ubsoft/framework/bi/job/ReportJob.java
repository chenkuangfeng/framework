package com.ubsoft.framework.bi.job;

import org.quartz.DisallowConcurrentExecution;
import org.quartz.Job;
import org.quartz.JobExecutionContext;
import org.quartz.JobExecutionException;

import com.ubsoft.framework.bi.cache.MemoryReport;
import com.ubsoft.framework.bi.entity.Report;
import com.ubsoft.framework.core.support.util.IPUtil;

//@PersistJobDataAfterExecution
@DisallowConcurrentExecution
public class ReportJob implements Job {
	private static String ip = IPUtil.getLocalIP();
	
	@Override
	public void execute(JobExecutionContext context) throws JobExecutionException {

		try {
			String jobName = context.getJobDetail().getKey().getName();
			Report  rpt = MemoryReport.getInstance().get(jobName);
			try {
				
				
			} catch (Exception e) {
				e.printStackTrace();
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

}
