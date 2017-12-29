package com.ubsoft.framework.core.service.impl;

import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.StringUtils;

import com.ubsoft.framework.core.cache.MemoryBioMeta;
import com.ubsoft.framework.core.dal.entity.BioMeta;
import com.ubsoft.framework.core.dal.entity.BioPropertyMeta;
import com.ubsoft.framework.core.exception.ComException;
import com.ubsoft.framework.core.service.IBioMetaService;
import com.ubsoft.framework.core.support.util.StringUtil;

@Service("bioMetaService")
@Transactional
public class BioMetaService extends BaseService<BioMeta> implements IBioMetaService {

	@Override
	public void initBioMeta() {
		List<BioMeta> bioMetas = dataSession.gets(BioMeta.class);
		for (BioMeta bioMeta : bioMetas) {
			setPropertyMeta(bioMeta);
			MemoryBioMeta.getInstance().put(bioMeta.getBioKey(), bioMeta);
		}

	}

	@Override
	public BioMeta getBioMeta(String bioKey) {
		BioMeta bioMeta = dataSession.get(BioMeta.class, "bioKey", bioKey);
		if (bioMeta == null) {
			throw new ComException(ComException.MIN_ERROR_CODE_FDM, "找不到BioMeta:" + bioKey);
		}
		setPropertyMeta(bioMeta);
		return bioMeta;
	}

	private void setPropertyMeta(BioMeta bioMeta) {
		List<BioPropertyMeta> propertyMeta = dataSession.gets(BioPropertyMeta.class, "bioKey",  bioMeta.getBioKey());
		Set<BioPropertyMeta> propertySet = new HashSet<BioPropertyMeta>();
		propertySet.addAll(propertyMeta);
		bioMeta.setPropertySet(propertySet);
		Map<String, BioPropertyMeta> propertyMap = new HashMap<String, BioPropertyMeta>();
		for (BioPropertyMeta meta : propertyMeta) {
			// 转换成大写
			String propertyKey = meta.getPropertyKey().toUpperCase();
			String columnKey = meta.getColumnKey().toUpperCase();
			propertyMap.put(propertyKey, meta);
			if (!propertyKey.equals(columnKey)) {
				propertyMap.put(columnKey, meta);
			}
			if(StringUtil.isTrue(meta.getPrimaryKey())){
				bioMeta.setPrimaryProperty(meta);
			
			}
			if(StringUtil.isTrue(meta.getVersionKey())){
				bioMeta.setVersionProperty(meta);
			}

		}
		bioMeta.setPropertyMap(propertyMap);
	}

	

	@Override
	public BioMeta refreshBioMeta(String bioName) {
		BioMeta bioMeta = getBioMeta(bioName);
		MemoryBioMeta.getInstance().put(bioName, bioMeta);
		return bioMeta;

	}

	
	

}
