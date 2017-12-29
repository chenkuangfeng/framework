package com.ubsoft.framework.core.service.impl;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.ubsoft.framework.core.cache.MemoryBioMeta;
import com.ubsoft.framework.core.dal.entity.BioMeta;
import com.ubsoft.framework.core.exception.ComException;
import com.ubsoft.framework.core.service.IBioMetaService;
import com.ubsoft.framework.core.service.IFdmMetaService;
import com.ubsoft.framework.core.support.xml.XmlUtil;
import com.ubsoft.framework.metadata.cache.MemoryFdmMeta;
import com.ubsoft.framework.metadata.entity.FdmEntity;
import com.ubsoft.framework.metadata.model.form.fdm.DetailMeta;
import com.ubsoft.framework.metadata.model.form.fdm.FdmMeta;

@Service("fdmMetaService")
@Transactional
public class FdmMetaService extends BaseService<BioMeta> implements IFdmMetaService {

	@Autowired
	IBioMetaService bioMetaService;

	@Override
	public void initFdmMeta() {
		List<FdmEntity> fdmEntities = dataSession.gets(FdmEntity.class);
		for (FdmEntity fdmEntity : fdmEntities) {
			FdmMeta fdmMeta = genFdmMeta(fdmEntity, false);
			MemoryFdmMeta.getInstance().put(fdmEntity.getFdmKey(), fdmMeta);
		}

	}

	private FdmMeta genFdmMeta(FdmEntity fdmEntity, boolean refreshBioMeta) {
		try {
			String fdmXml = fdmEntity.getFdmXml();
			FdmMeta fdmMeta = XmlUtil.fromXML(fdmXml, FdmMeta.class);
			String masterBioName = fdmMeta.getMaster().getBio();
			BioMeta masterBioMeta = null;
			if (refreshBioMeta) {
				masterBioMeta = bioMetaService.refreshBioMeta(masterBioName);

			}
			masterBioMeta = MemoryBioMeta.getInstance().get(masterBioName);
			fdmMeta.getMaster().setBioMeta(masterBioMeta);
			if (fdmMeta.getDetails() != null) {
				for (DetailMeta detailMeta : fdmMeta.getDetails()) {
					String detailBioName = detailMeta.getBio();
					BioMeta detailBioMeta = null;
					if (refreshBioMeta) {
						detailBioMeta = bioMetaService.refreshBioMeta(detailBioName);
					} else {
						detailBioMeta = MemoryBioMeta.getInstance().get(detailBioName);
					}
					detailMeta.setBioMeta(detailBioMeta);

				}
			}
			return fdmMeta;
		} catch (Exception ex) {
			throw new ComException(ComException.MIN_ERROR_CODE_FDM + 100, ex);
		}

	}

	@Override
	public FdmMeta getFdmMeta(String fdmKey) {
		FdmEntity fdmEntity = dataSession.get(FdmEntity.class, "fdmKey", fdmKey);
		FdmMeta fdmMeta = genFdmMeta(fdmEntity, false);
		return fdmMeta;

	}

	@Override
	public FdmMeta refreshFdmMeta(String fdmKey) {
		FdmEntity fdmEntity = dataSession.get(FdmEntity.class, "fdmKey", fdmKey);
		FdmMeta fdmMeta = genFdmMeta(fdmEntity, true);
		MemoryFdmMeta.getInstance().put(fdmKey, fdmMeta);		
		return fdmMeta;

	}

}
