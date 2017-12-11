package com.ubsoft.framework.system.entity;

import java.io.Serializable;

import com.ubsoft.framework.core.dal.annotation.Column;
import com.ubsoft.framework.core.dal.annotation.Table;
import com.ubsoft.framework.core.dal.entity.BaseEntity;




@Table(name="SA_DIMENSION_DTL")  
public class DimensionDetail extends BaseEntity implements Serializable {
	private static final long serialVersionUID = 1L;
	
	@Column(name="DIMDTLKEY",length=32,nullable=false) 
	private String dimDtlKey;	
	@Column(name="DIMDTLNAME",length=100,nullable=false) 
	private String dimDtlName;	
	
	@Column(name="TABLENAME",length=50,nullable=false) 
	private String tableName;	
	
	@Column(name="ENTITYNAME",length=100,nullable=false) 
	private String entityName;	
	
	@Column(name="VALUEFIELD",length=60) 
	private String valueField;
	
	public String getValueField() {
		return valueField;
	}
	public void setValueField(String valueField) {
		this.valueField = valueField;
	}
	@Column(name="DIMID",length=32,nullable=false) 
	private String dimId;	
	
	
	public String getTableName() {
		return tableName;
	}
	public void setTableName(String tableName) {
		this.tableName = tableName;
	}
	public String getEntityName() {
		return entityName;
	}
	public void setEntityName(String entityName) {
		this.entityName = entityName;
	}
	
	public String getDimDtlKey() {
		return dimDtlKey;
	}
	public void setDimDtlKey(String dimDtlKey) {
		this.dimDtlKey = dimDtlKey;
	} 
	public String getDimDtlName() {
		return dimDtlName;
	}
	public void setDimDtlName(String dimDtlName) {
		this.dimDtlName = dimDtlName;
	}
	public String getDimId() {
		return dimId;
	}
	public void setDimId(String dimId) {
		this.dimId = dimId;
	}
		
	
	
	
	
}