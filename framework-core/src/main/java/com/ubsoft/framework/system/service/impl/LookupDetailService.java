package com.ubsoft.framework.system.service.impl;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.ubsoft.framework.core.service.impl.BaseService;
import com.ubsoft.framework.system.entity.LookupDetail;
import com.ubsoft.framework.system.service.ILookupDetailService;

@Service("lookupDetailService")
@Transactional
public class LookupDetailService extends BaseService<LookupDetail> implements ILookupDetailService {
	
}
