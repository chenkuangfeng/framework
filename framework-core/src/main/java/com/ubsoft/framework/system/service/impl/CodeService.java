package com.ubsoft.framework.system.service.impl;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.ubsoft.framework.core.service.impl.BaseService;
import com.ubsoft.framework.system.entity.Code;
import com.ubsoft.framework.system.service.ICodeService;

@Service("codeService")
@Transactional
public class CodeService extends BaseService<Code> implements ICodeService {

	
}