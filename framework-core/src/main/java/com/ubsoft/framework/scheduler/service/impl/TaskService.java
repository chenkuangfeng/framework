package com.ubsoft.framework.scheduler.service.impl;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.ubsoft.framework.core.service.impl.BaseService;
import com.ubsoft.framework.scheduler.cache.MemoryTask;
import com.ubsoft.framework.scheduler.entity.Task;
import com.ubsoft.framework.scheduler.model.JobDetailModel;
import com.ubsoft.framework.scheduler.service.IQrtzTaskService;
import com.ubsoft.framework.scheduler.service.ITaskService;

@Service("taskService")
@Transactional
public class TaskService extends BaseService<Task> implements ITaskService {

	@Autowired
	IQrtzTaskService qrtzTaskService;

	@Override
	public void pauseTasks(String[] taskIds) {
		for (String id : taskIds) {
			Task task = get(id);
			JobDetailModel jobDetail = getJobDetail(task);
			qrtzTaskService.removeJob(jobDetail);		
			MemoryTask.getInstance().put(task.getTaskKey(), task);
			
		}

	}

	@Override
	public void removeTasks(String[] taskIds) {
		for (String id : taskIds) {
			Task task = get(id);
			JobDetailModel jobDetail = getJobDetail(task);
			qrtzTaskService.removeJob(jobDetail);			
		}

	}

	@Override
	public void resumeTasks(String[] taskIds) {
		for (String id : taskIds) {
			Task task = get(id);
			JobDetailModel jobDetail = getJobDetail(task);
			qrtzTaskService.resumeJob(jobDetail);
		}

	}

	@Override
	public Task saveTask(Task task) {
		JobDetailModel jobDetail = getJobDetail(task);
		// qrtzTaskService.addJob(jobDetail);启动任务放在startTask里面
		task.setStatus("0");// 默认不启用
		this.save(task);
		// 放入缓存，供GeneralJob 用
		MemoryTask.getInstance().put(task.getTaskKey(), task);
		return task;
	}

	@Override
	public void startTasks(String[] taskIds) {
		for (String id : taskIds) {
			Task task = get(id);
			JobDetailModel jobDetail = getJobDetail(task);
			qrtzTaskService.startJob(jobDetail);		
		}

	}

	private JobDetailModel getJobDetail(Task task) {
		JobDetailModel jobDetail= new JobDetailModel();
		jobDetail.setCronExpression(task.getCronExpression());
		jobDetail.setDescription(task.getTaskName());
		jobDetail.setEndTime(task.getEndTime());
		jobDetail.setJobGroup(task.getTaskKey());
		jobDetail.setJobName(task.getTaskKey());
		jobDetail.setStartTime(task.getStartTime());
		jobDetail.setTriggerGroup(task.getTaskKey());
		jobDetail.setTriggerName(task.getTaskKey());;
		return jobDetail;
	}

	@Override
	public void initTask() {
		List<Task> tasks =gets();
		for (Task task : tasks) {
			MemoryTask.getInstance().put(task.getTaskKey(), task);
		}

	}

}
