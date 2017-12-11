package com.ubsoft.framework.esb.entity;

import com.ubsoft.framework.core.dal.annotation.Column;
import com.ubsoft.framework.core.dal.annotation.Table;
import com.ubsoft.framework.core.dal.entity.BaseEntity;

/**
 * 接口APPKEY配置表，用来授权接口权限。
 * @author chenkf
 *
 */

@Table(name="ESB_APPKEY")  
public class AppKey extends BaseEntity {
	@Column(name="APPKEY",length=32,nullable=false) 
	private String appKey;
	
	@Column(name="APPNAME",length=100,nullable=false) 
	private String appName;
	
	@Column(name="SECRET",length=100,nullable=false) 
	private String secret;

	public String getAppKey() {
		return appKey;
	}

	public void setAppKey(String appKey) {
		this.appKey = appKey;
	}

	public String getAppName() {
		return appName;
	}

	public void setAppName(String appName) {
		this.appName = appName;
	}

	public String getSecret() {
		return secret;
	}

	public void setSecret(String secret) {
		this.secret = secret;
	}
	
	
	
	
}
