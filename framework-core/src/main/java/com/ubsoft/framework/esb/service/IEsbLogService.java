package com.ubsoft.framework.esb.service;

import com.ubsoft.framework.core.service.IBaseService;
import com.ubsoft.framework.esb.entity.EsbLog;

public interface IEsbLogService extends IBaseService<EsbLog>{
	public void info(EsbLog log);
	public void debug(EsbLog log);
	public void error(EsbLog log);
}
