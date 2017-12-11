package com.ubsoft.framework.system.entity;

import java.io.Serializable;

import com.ubsoft.framework.core.dal.annotation.Column;
import com.ubsoft.framework.core.dal.annotation.Table;


@Table(name = "SA_USER_PERMISSION")
public class UserPermission implements Serializable {
	private static final long serialVersionUID = 1L;	
	@Column(name="ID",length=32,nullable=false)  
	protected String id;
	@Column(name = "USERKEY", length = 32, nullable = false)
	private String userKey;

	@Column(name = "PERMKEY", length = 100, nullable = false)
	private String permKey;

	public String getUserKey() {
		return userKey;
	}

	public void setUserKey(String userKey) {
		this.userKey = userKey;
	}

	public String getPermKey() {
		return permKey;
	}

	public void setPermKey(String permKey) {
		this.permKey = permKey;
	}

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}
	
}
