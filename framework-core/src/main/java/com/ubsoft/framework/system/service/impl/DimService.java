package com.ubsoft.framework.system.service.impl;

import java.util.ArrayList;
import java.util.List;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.ubsoft.framework.core.dal.model.Bio;
import com.ubsoft.framework.core.service.impl.BaseService;
import com.ubsoft.framework.core.support.json.JsonHelper;
import com.ubsoft.framework.system.cache.MemoryDimension;
import com.ubsoft.framework.system.entity.Dimension;
import com.ubsoft.framework.system.entity.DimensionDetail;
import com.ubsoft.framework.system.service.IDimService;

@Service("dimService")
@Transactional
public class DimService extends BaseService<Dimension> implements IDimService {
	@Override
	public void deleteDetailAll(String[] ids) {
		for (String id : ids) {			
			DimensionDetail detail=dataSession.get(DimensionDetail.class,id);
			dataSession.delete(detail);
		}

	}
	@Override
	public List<DimensionDetail> saveDetails(String forms) {
		
		@SuppressWarnings("unchecked")
		List<DimensionDetail> lisDimensionDetailt = (List<DimensionDetail>) JsonHelper.json2Collection(forms, DimensionDetail.class);
		for (DimensionDetail item : lisDimensionDetailt) {
			dataSession.save(item);
		}
		return lisDimensionDetailt;		
	}

	@Override
	public List<DimensionDetail> selectDetail(String condition, String orderBy, Object[] params) {
		return dataSession.gets(DimensionDetail.class,condition,params,orderBy);
	
	}

	@Override
	public void initDimension() {
		MemoryDimension.getInstance().clear();
		String sql="SELECT DIMKEY,D.VALUEFIELD ,D.ENTITYNAME,D.TABLENAME FROM SA_DIMENSION H ,SA_DIMENSION_DTL D WHERE D.DIMID=H.ID";
		List<Bio> bios=dataSession.query(sql, null);
		for(Bio bio:bios){
			List<Bio> listBio=MemoryDimension.getInstance().get(bio.getString("ENTITYNAME"));
			if(listBio==null){
				  listBio=new ArrayList<Bio>();				
			}//备注信息
			listBio.add(bio);
			MemoryDimension.getInstance().put(bio.getString("ENTITYNAME"), listBio);
			MemoryDimension.getInstance().put(bio.getString("TABLENAME").toUpperCase(), listBio);
		}
	}

}
