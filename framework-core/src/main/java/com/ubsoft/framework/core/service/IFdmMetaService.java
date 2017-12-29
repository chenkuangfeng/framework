package com.ubsoft.framework.core.service;

import com.ubsoft.framework.core.dal.entity.BioMeta;
import com.ubsoft.framework.metadata.model.form.fdm.FdmMeta;


public interface IFdmMetaService extends IBaseService<BioMeta>  {	
	void initFdmMeta();
	FdmMeta refreshFdmMeta(String fdmKey);
	FdmMeta getFdmMeta(String fdmKey);
	
}
