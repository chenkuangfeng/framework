package com.ubsoft.framework.scheduler.service;

import com.ubsoft.framework.scheduler.entity.Task;

/**
 * @ClassName: IJobService
 * @Description: 调度服务公共接口，所有实现调度的服务都要实现此接口。
 * @author chenkf
 * @date 2017-2-22 下午01:06:20
 * @version V1.0
 */
public interface IJobService {
	void execute(Task task);
}
