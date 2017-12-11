package com.ubsoft.framework.system.service;

import java.util.List;

import com.ubsoft.framework.core.service.IBaseService;
import com.ubsoft.framework.system.entity.Permission;

public interface IPermService extends IBaseService<Permission>  {

	
	List<Permission> getMenus();
	
	
}
