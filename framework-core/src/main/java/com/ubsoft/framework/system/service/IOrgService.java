package com.ubsoft.framework.system.service;

import java.util.List;

import com.ubsoft.framework.core.service.IBaseService;
import com.ubsoft.framework.system.entity.Org;
import com.ubsoft.framework.system.entity.Region;

public interface IOrgService extends IBaseService<Org>  {

	
	List<Org> getDimensionOrgList();
	List<Region> getRegionList();
	
}
