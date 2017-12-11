package com.ubsoft.framework.system.service;

import java.util.List;

import com.ubsoft.framework.core.service.IBaseService;
import com.ubsoft.framework.system.entity.Dimension;
import com.ubsoft.framework.system.entity.DimensionDetail;

public interface IDimService extends IBaseService<Dimension>  {
	List<DimensionDetail> selectDetail(String condition, String orderBy, Object[] params);
	List<DimensionDetail> saveDetails(String form);
	void deleteDetailAll(String[] ids);
	/**
	 * 加载dimension到缓存
	 */
	void initDimension();
	
}
