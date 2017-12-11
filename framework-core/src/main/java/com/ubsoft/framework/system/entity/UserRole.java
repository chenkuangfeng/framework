package com.ubsoft.framework.system.entity;

import java.io.Serializable;

import com.ubsoft.framework.core.dal.annotation.Column;
import com.ubsoft.framework.core.dal.annotation.Table;




@Table(name="SA_USER_ROLE")  
public class UserRole  implements Serializable {
	private static final long serialVersionUID = 1L;
	
	@Column(name="ID",length=32,nullable=false)  
	protected String id;
	@Column(name="USERKEY",length=32,nullable=false) 	
	private String userKey;	 
	@Column(name="ROLEKEY",length=100,nullable=false) 	
	private String roleKey;	

	public String getUserKey() {
		return userKey;
	}
	public void setUserKey(String userKey) {
		this.userKey = userKey;
	}
	public String getRoleKey() {
		return roleKey;
	}
	public void setRoleKey(String roleKey) {
		this.roleKey = roleKey;
	}
	public String getId() {
		if(id.equals("")){
			id=null;
		}
		return id;
	}
	public void setId(String id) {
		if(id.equals("")){
			id=null;
		}
		this.id = id;
	}
	
}