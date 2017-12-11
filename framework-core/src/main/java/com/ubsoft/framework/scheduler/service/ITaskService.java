package com.ubsoft.framework.scheduler.service;

import com.ubsoft.framework.core.service.IBaseService;
import com.ubsoft.framework.scheduler.entity.Task;

public interface ITaskService extends IBaseService<Task> {

	Task saveTask(Task task);
	
	void removeTasks(String[] taskIds);
	
	void resumeTasks(String[] taskIds);
	
	void pauseTasks(String[] taskIds);
	
	void startTasks(String[] taskIds);
	
	/**
	 * 加载task配置信息到内存中
	* @Title: initTask
	* @Description: TODO
	* @author chenkf
	* @date  2017-2-22 下午04:17:23
	 */
	void initTask();

}
