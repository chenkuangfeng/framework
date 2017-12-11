package com.ubsoft.framework.scheduler.entity;

import java.sql.Timestamp;

import com.ubsoft.framework.core.dal.annotation.Column;
import com.ubsoft.framework.core.dal.annotation.Table;
import com.ubsoft.framework.core.dal.entity.BaseEntity;

/**
 * 调度任务表
 * 
 * @author Administrator
 * 
 */

@Table(name = "SA_TASK")
public class Task extends BaseEntity {
	@Column(name="TASKKEY",length = 32, unique = true, nullable = false)
	private String taskKey;
	@Column(name="TASKNAME",length = 200, nullable = false)
	private String taskName;
	
	@Column(name="TASKTYPE",length = 100, nullable = false)
	private String taskType;
	@Column(name="CRONEXPRESSION",length = 50,nullable = false)
	private String cronExpression;
	@Column(name="SERVICENAME",length = 50, nullable = false)
	private String serviceName;
	
	@Column(name="UNITNAME",length = 50, nullable = false)
	private String unitName;	
	@Column(name="ARGS",length = 100)
	private String args;
	
	@Column(name="SQLVALUE")	
	private String sqlValue;
	@Column(name="STARTTIME")
	private Timestamp startTime;
	
	@Column(name="ENDTIME")
	private Timestamp endTime;
	
	@Column(name="REMARKS")
	private String remarks;
	
	/**
	 * 是否记录日志
	 */
	@Column(name="LOG")
	private boolean log;

	public String getTaskKey() {
		return taskKey;
	}

	public void setTaskKey(String taskKey) {
		this.taskKey = taskKey;
	}

	public String getTaskName() {
		return taskName;
	}

	public void setTaskName(String taskName) {
		this.taskName = taskName;
	}

	public String getCronExpression() {
		return cronExpression;
	}

	public void setCronExpression(String cronExpression) {
		this.cronExpression = cronExpression;
	}

	public String getServiceName() {
		return serviceName;
	}

	public void setServiceName(String serviceName) {
		this.serviceName = serviceName;
	}

	public String getUnitName() {
		return unitName;
	}

	public void setUnitName(String unitName) {
		this.unitName = unitName;
	}

	public String getArgs() {
		return args;
	}

	public void setArgs(String args) {
		this.args = args;
	}

	public Timestamp getStartTime() {
		return startTime;
	}

	public void setStartTime(Timestamp startTime) {
		this.startTime = startTime;
	}

	public Timestamp getEndTime() {
		return endTime;
	}
	public void setEndTime(Timestamp endTime) {
		this.endTime = endTime;
	}

	public String getTaskType() {
		return taskType;
	}

	public void setTaskType(String taskType) {
		this.taskType = taskType;
	}

	

	public String getSqlValue() {
		return sqlValue;
	}

	public void setSqlValue(String sqlValue) {
		this.sqlValue = sqlValue;
	}

	public boolean isLog() {
		return log;
	}

	public void setLog(boolean log) {
		this.log = log;
	}

	public String getRemarks() {
		return remarks;
	}

	public void setRemarks(String remarks) {
		this.remarks = remarks;
	}

	

	
	
	
}
