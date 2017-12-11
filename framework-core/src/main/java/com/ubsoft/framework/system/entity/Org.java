package com.ubsoft.framework.system.entity;

import java.io.Serializable;

import com.ubsoft.framework.core.dal.annotation.Column;
import com.ubsoft.framework.core.dal.annotation.Table;


@Table(name = "SA_ORG")
public class Org extends Unit implements Serializable {
	private static final long serialVersionUID = 1L;
	@Column(name = "ORGKEY", length = 32, unique = true, nullable = false)
	private String orgKey; // 组织代码
	@Column(name = "ORGNAME", length = 100, nullable = false)
	private String orgName; // 组织名称
	@Column(name = "OWNERKEY", length = 32, nullable = false)
	private String ownerKey;// 所属组织ID
	@Column(name = "ORGTYPE", length = 100, nullable = false)
	private String orgType;// 管理组织、部门。

	public String getOrgKey() {
		return orgKey;
	}

	public void setOrgKey(String orgKey) {
		this.orgKey = orgKey;
	}

	public String getOrgName() {
		return orgName;
	}

	public void setOrgName(String orgName) {
		this.orgName = orgName;
	}

	public String getOwnerKey() {
		return ownerKey;
	}

	public void setOwnerKey(String ownerKey) {
		this.ownerKey = ownerKey;
	}

	public String getOrgType() {
		return orgType;
	}

	public void setOrgType(String orgType) {
		this.orgType = orgType;
	}

}