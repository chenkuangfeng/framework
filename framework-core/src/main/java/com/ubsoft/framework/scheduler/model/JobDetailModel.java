package com.ubsoft.framework.scheduler.model;

import java.io.Serializable;
import java.util.Date;
import java.util.Map;
/**
 * 
* @ClassName: JobDetailModel
* @Description: JobDetail配置VO
* @author chenkf
* @date 2017-2-22 上午09:44:05
* @version V1.0
 */
public class JobDetailModel implements Serializable {
	private String triggerName;// trigger名称
	
	private String triggerGroup = "DEFAULT";// trigger所属组
	
	private String jobName;// Job名称
	
	private String jobGroup = "DEFAULT";// Job所属组
	
	private String description;// Job描述
	
	private String jobClassName;// Job Class 路径

	private String cronExpression;// Cron表达式

	private Date startTime;// Job开始时间

	private Date endTime;// Job结束时间

	private Map<String, Object> jobMap;

	public String getTriggerName() {
		return triggerName;
	}

	public void setTriggerName(String triggerName) {
		this.triggerName = triggerName;
	}

	public String getTriggerGroup() {
		return triggerGroup;
	}

	public void setTriggerGroup(String triggerGroup) {
		this.triggerGroup = triggerGroup;
	}

	public String getJobName() {
		return jobName;
	}

	public void setJobName(String jobName) {
		this.jobName = jobName;
	}

	public String getJobGroup() {
		return jobGroup;
	}

	public void setJobGroup(String jobGroup) {
		this.jobGroup = jobGroup;
	}

	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}

	public String getJobClassName() {
		return jobClassName;
	}

	public void setJobClassName(String jobClassName) {
		this.jobClassName = jobClassName;
	}

	public String getCronExpression() {
		return cronExpression;
	}

	public void setCronExpression(String cronExpression) {
		this.cronExpression = cronExpression;
	}

	public Date getStartTime() {
		return startTime;
	}

	public void setStartTime(Date startTime) {
		this.startTime = startTime;
	}

	public Date getEndTime() {
		return endTime;
	}

	public void setEndTime(Date endTime) {
		this.endTime = endTime;
	}

	public Map<String, Object> getJobMap() {
		return jobMap;
	}

	public void setJobMap(Map<String, Object> jobMap) {
		this.jobMap = jobMap;
	}
}
