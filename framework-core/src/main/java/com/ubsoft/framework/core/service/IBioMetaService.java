package com.ubsoft.framework.core.service;

import com.ubsoft.framework.core.dal.entity.BioMeta;


public interface IBioMetaService extends IBaseService<BioMeta>  {	
	void initBioMeta();
	BioMeta refreshBioMeta(String bioName);
	BioMeta getBioMeta(String bioName);
		
}
