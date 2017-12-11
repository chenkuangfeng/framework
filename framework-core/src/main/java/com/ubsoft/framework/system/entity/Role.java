package com.ubsoft.framework.system.entity;

import java.io.Serializable;

import com.ubsoft.framework.core.dal.annotation.Column;
import com.ubsoft.framework.core.dal.annotation.Table;
import com.ubsoft.framework.core.dal.entity.BaseEntity;




@Table(name="SA_ROLE")  
public class Role extends BaseEntity implements Serializable {
	private static final long serialVersionUID = 1L;

	@Column(name="ROLEKEY",length=32,unique=true,nullable=false) 
	private String roleKey;		
	@Column(name="ROLENAME",length=100,nullable=false) 	
	private String roleName;	
	@Column(name="ROLETYPE",length=10) 
	private String roleType;
	
	@Column(name="ORGKEY",nullable=false) 
	private String orgKey;		
	
	@Column(name="REMARKS",length=255) 
	private String remarks;
	
	
	public String getRemarks() {
		return remarks;
	}
	public void setRemarks(String remarks) {
		this.remarks = remarks;
	}
	public String getRoleKey() {
		return roleKey;
	}
	public void setRoleKey(String roleKey) {
		this.roleKey = roleKey;
	}
	public String getRoleName() {
		return roleName;
	}
	public void setRoleName(String roleName) {
		this.roleName = roleName;
	}
	
	public String getRoleType() {
		return roleType;
	}
	
	public void setRoleType(String roleType) {
		this.roleType = roleType;
	}
	public String getOrgKey() {
		return orgKey;
	}
	public void setOrgKey(String orgKey) {
		this.orgKey = orgKey;
	}
	

	
	
	
	
	

   

}