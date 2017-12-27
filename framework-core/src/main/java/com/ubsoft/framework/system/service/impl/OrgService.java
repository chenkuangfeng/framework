package com.ubsoft.framework.system.service.impl;

import java.util.List;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.ubsoft.framework.core.dal.util.SQLUtil;
import com.ubsoft.framework.core.service.impl.BaseService;
import com.ubsoft.framework.system.entity.Org;
import com.ubsoft.framework.system.entity.Region;
import com.ubsoft.framework.system.service.IOrgService;

@Service("orgService")
@Transactional
public class OrgService extends BaseService<Org> implements IOrgService {

	@Override
	public List<Org> getDimensionOrgList() {
		String sql = "select * from {SA_ORG:T}";
		sql = SQLUtil.getDimensionSql(sql);// 加入数据纬度权限
		sql += " order by T.orgKey ";
		return this.dataSession.select(sql, new Object[] {}, Org.class);

	}

	@Override
	public List<Region> getRegionList() {
		List<Region> regionList=dataSession.gets(Region.class, "status=?", "seq", new Object[]{"1"});
		return regionList;
	}

	

}
