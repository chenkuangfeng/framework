package com.ubsoft.framework.system.entity;

import java.io.Serializable;

import com.ubsoft.framework.core.dal.annotation.Column;
import com.ubsoft.framework.core.dal.annotation.Table;
import com.ubsoft.framework.core.dal.entity.BaseEntity;

@Table(name="SA_PERMISSION")  
public class Permission extends BaseEntity implements Serializable {
	private static final long serialVersionUID = 1L;
	@Column(name="PERMKEY",length=100, unique=true,nullable=false) 	
	private String permKey;	
	@Column(name="PERMNAME",length=100,nullable=false) 
	private String permName;	
	@Column(name="PERMMODULE",length=100)
	private String permModule;		
	@Column(name="PERMTYPE",length=100,nullable=false) 
	private String permType;	
	@Column(name="PARAMS",length=100) 
	private String params;	
	
	@Column(name="ICON",length=30) 
	private String icon;	
	

	@Column(name="PARENTPERMKEY",length=50,nullable=false) 
	private String parentPermKey;
	
	@Column(name="PERMGROUP",length=10) 
	private String permGroup;	
	
	@Column(name="SEQ") 

	private Integer seq;
	
	public String getPermKey() {
		return permKey;
	}
	public void setPermKey(String permKey) {
		this.permKey = permKey;
	}
	public String getPermName() {
		return permName;
	}
	public void setPermName(String permName) {
		this.permName = permName;
	}
	public String getPermModule() {
		return permModule;
	}
	public void setPermModule(String permModule) {
		this.permModule = permModule;
	}
	
	
	public String getPermType() {
		return permType;
	}
	public void setPermType(String permType) {
		this.permType = permType;
	}
	

	
	public String getPermGroup() {
		return permGroup;
	}
	public void setPermGroup(String permGroup) {
		this.permGroup = permGroup;
	}
	public String getParams() {
		return params;
	}
	
	public void setParams(String params) {
		this.params = params;
	}
	
	public Integer getSeq() {
		return seq;
	}
	public void setSeq(Integer seq) {
		this.seq = seq;
	}
	
	public String getParentPermKey() {
		return parentPermKey;
	}
	
	public void setParentPermKey(String parentPermKey) {
		this.parentPermKey = parentPermKey;
	}
	
	public String getIcon() {
		return icon;
	}
	
	public void setIcon(String icon) {
		this.icon = icon;
	}	
	
	
	
}