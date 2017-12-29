package com.ubsoft.framework.system.service.impl;

import java.util.List;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.ubsoft.framework.core.service.impl.BaseService;
import com.ubsoft.framework.system.entity.Permission;
import com.ubsoft.framework.system.service.IPermService;

@Service("permService")
@Transactional
public class PermService extends BaseService<Permission> implements IPermService {
	@Override
	public List<Permission> getMenus() {
		return this.gets("permType","MENU","seq");
	}

}
