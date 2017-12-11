package com.ubsoft.framework.scheduler.service.impl;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.ubsoft.framework.core.service.impl.BaseService;
import com.ubsoft.framework.scheduler.entity.TaskLog;
import com.ubsoft.framework.scheduler.service.ITaskLogService;

@Service("taskLogService")
@Transactional
public class TaskLogService extends BaseService<TaskLog> implements ITaskLogService {

	

}
