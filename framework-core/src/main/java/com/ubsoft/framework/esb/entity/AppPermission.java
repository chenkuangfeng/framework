package com.ubsoft.framework.esb.entity;

import com.ubsoft.framework.core.dal.annotation.Column;
import com.ubsoft.framework.core.dal.annotation.Table;
import com.ubsoft.framework.core.dal.entity.BaseEntity;


@Table(name="ESB_APP_PERMISSION") 
public class AppPermission extends BaseEntity{

	@Column(name="APPKEY",length=32,nullable=false) 
	private String appKey;
	@Column(name="EPKEY",length=32,nullable=false) 
	private String epKey;
	public String getAppKey() {
		return appKey;
	}
	public void setAppKey(String appKey) {
		this.appKey = appKey;
	}
	public String getEpKey() {
		return epKey;
	}
	public void setEpKey(String epKey) {
		this.epKey = epKey;
	}
	
	
}
