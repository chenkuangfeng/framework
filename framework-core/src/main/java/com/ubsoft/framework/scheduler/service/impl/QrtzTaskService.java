package com.ubsoft.framework.scheduler.service.impl;

import java.util.Date;
import java.util.Map;

import org.quartz.CronScheduleBuilder;
import org.quartz.CronTrigger;
import org.quartz.Job;
import org.quartz.JobBuilder;
import org.quartz.JobDetail;
import org.quartz.JobKey;
import org.quartz.Scheduler;
import org.quartz.SchedulerException;
import org.quartz.TriggerBuilder;
import org.quartz.TriggerKey;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import com.ubsoft.framework.core.context.AppContext;
import com.ubsoft.framework.scheduler.model.JobDetailModel;
import com.ubsoft.framework.scheduler.service.IQrtzTaskService;

@Service
public class QrtzTaskService implements IQrtzTaskService {

	
	private Scheduler getScheduler(){
		Scheduler  scheduler=(Scheduler)AppContext.getBean("quartzScheduler");
		return scheduler;
	}
	@Override
	public void addJob(JobDetailModel jobDetailModel) {
		try {
			// 这里把jobName与triggerName设置相同，便于操作,组名统一为"DEFAULT"
			String jobName = jobDetailModel.getJobName();
			String jobGroup = jobDetailModel.getJobGroup();
			String cronExpression = jobDetailModel.getCronExpression();
			String description = jobDetailModel.getDescription();
			String className = jobDetailModel.getJobClassName();
			Date startDate = jobDetailModel.getStartTime();
			Date endDate = jobDetailModel.getEndTime();
			if (StringUtils.isEmpty(jobName)) {
				throw new RuntimeException("job's name is empty!");
			}
			if (getScheduler().checkExists(TriggerKey.triggerKey(jobName, jobGroup))) {

				throw new RuntimeException("Job：" + jobName + " 已存在。");
			}
			if (StringUtils.isEmpty(jobGroup)) {
				jobGroup = Scheduler.DEFAULT_GROUP;
			}
			// 如果无className参数则使用默认GeneralJob
			if (className == null || className.equals("")) {
				className = "com.framework.scheduler.job.GeneralJob";
			}
			// JOB状态
			final Class<? extends Job> clazz = (Class<? extends Job>) Class.forName(className);
			// 构建job信息
			JobDetail jobDetail = JobBuilder.newJob(clazz).withIdentity(jobName, jobGroup).withDescription(description)
					.build();

			// 放入参数，运行时的方法可以获取
			if (jobDetailModel.getJobMap() != null) {
				for (Map.Entry<String, Object> entry : jobDetailModel.getJobMap().entrySet()) {
					jobDetail.getJobDataMap().put(entry.getKey(), entry.getValue());
				}
			}

			// 表达式调度构建器
			CronScheduleBuilder scheduleBuilder = CronScheduleBuilder.cronSchedule(cronExpression);
			CronTrigger trigger = null;
			// 按新的cronExpression表达式构建一个新的trigger
			if (startDate != null && endDate != null) {
				trigger = TriggerBuilder.newTrigger().startAt(startDate).endAt(startDate).withIdentity(jobName,
						jobGroup).withSchedule(scheduleBuilder).withDescription(description).build();
				
			} else if (startDate != null && endDate == null) {
				trigger = TriggerBuilder.newTrigger().startAt(startDate).withIdentity(jobName, jobGroup).withSchedule(
						scheduleBuilder).withDescription(description).build();
			} else if (startDate == null && endDate == null) {
				trigger = TriggerBuilder.newTrigger().withIdentity(jobName, jobGroup).withSchedule(scheduleBuilder)
						.withDescription(description).build();
			}
			// 绑定调度器
			getScheduler().scheduleJob(jobDetail, trigger);
		} catch (ClassNotFoundException e) {
			throw new RuntimeException(e);
		} catch (Exception e) {

			throw new RuntimeException(e);
		}

	}

	@Override
	public void pauseJob(JobDetailModel jobDetailModel) {
		try {
			String jobName = jobDetailModel.getJobName();
			String jobGroup = jobDetailModel.getJobGroup();
			TriggerKey triggerKey = TriggerKey.triggerKey(jobName, jobGroup);
			if (getScheduler().checkExists(triggerKey)) {
				getScheduler().pauseTrigger(triggerKey);
			} else {
				throw new RuntimeException("该任务不存在");
			}
		} catch (SchedulerException e) {

			throw new RuntimeException(e);
		}

	}

	@Override
	public void removeJob(JobDetailModel jobDetailModel) {
		try {
			String jobName = jobDetailModel.getJobName();
			String jobGroup = jobDetailModel.getJobGroup();
			JobKey jobKey = JobKey.jobKey(jobName, jobGroup);
			TriggerKey triggerKey = TriggerKey.triggerKey(jobName, jobName);

			if (getScheduler().checkExists(jobKey)) {
				// 考虑立即运行会产生simple型随机的触发器，以Jobkey作为参数删除
				getScheduler().pauseTrigger(triggerKey);// 停止触发器
				getScheduler().unscheduleJob(triggerKey);// 移除触发器
				getScheduler().deleteJob(jobKey);
			} else {
				throw new RuntimeException("该任务不存在");
			}

		} catch (Exception e) {
			throw new RuntimeException(e);
		}

	}

	@Override
	public void resumeJob(JobDetailModel jobDetail) {
		try {
			String jobName = jobDetail.getJobName();
			String jobGroup = jobDetail.getJobGroup();
			TriggerKey triggerKey = TriggerKey.triggerKey(jobName, jobGroup);
			if (getScheduler().checkExists(triggerKey)) {
				getScheduler().resumeTrigger(triggerKey);
			} else {
				throw new RuntimeException("该任务不存在");
			}
		} catch (SchedulerException e) {

			throw new RuntimeException(e);
		}

	}
	@Override
	public void startJob(JobDetailModel jobDetail) {
		try {
			String jobGroup = jobDetail.getJobGroup();
			String jobName = jobDetail.getJobName();
			TriggerKey triggerKey = TriggerKey.triggerKey(jobName, jobGroup);
			JobKey jobKey = JobKey.jobKey(jobName, jobGroup);
			if (getScheduler().checkExists(triggerKey)) {
				this.removeJob(jobDetail);
				this.addJob(jobDetail);
				//scheduler.triggerJob(jobKey);
			}else{
				this.addJob(jobDetail);
			}

		} catch (SchedulerException e) {
			throw new RuntimeException(e);
		}

	}

}
