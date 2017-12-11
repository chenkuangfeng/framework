package com.ubsoft.framework.scheduler.service.impl;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.ubsoft.framework.core.service.impl.BaseService;
import com.ubsoft.framework.scheduler.entity.Task;
import com.ubsoft.framework.scheduler.entity.TaskLog;
import com.ubsoft.framework.scheduler.service.IJobService;

@Service("sqlJobService")
@Transactional
public class SqlJobService extends BaseService<Task> implements IJobService {

	@Override
	public void execute(Task task) {
		String sql=task.getSqlValue();
		String [] sqlArray=sql.split(";");
		for(String s:sqlArray){
			dataSession.executeUpdate(s, new Object[]{});
		}
		
	}

	

}
