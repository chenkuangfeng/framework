package com.ubsoft.framework.system.entity;

import java.io.Serializable;

import com.ubsoft.framework.core.dal.annotation.Column;
import com.ubsoft.framework.core.dal.annotation.Table;
import com.ubsoft.framework.core.dal.entity.BaseEntity;


/**
 * 数据纬度表
 * @author chenkf
 *
 */
@Table(name="SA_DIMENSION")  
public class Dimension extends BaseEntity implements Serializable {
	private static final long serialVersionUID = 1L;
	
	@Column(name="DIMKEY",length=32,nullable=false) 
	private String dimKey;	
	@Column(name="DIMNAME",length=100,nullable=false) 
	private String dimName;	
	
	@Column(name="TABLENAME",length=50,nullable=false) 
	private String tableName;	
	
	@Column(name="ENTITYNAME",length=100,nullable=false) 
	private String entityName;	
	
	
	
	@Column(name="VALUEFIELD",length=60) 
	private String valueField;		
	
	@Column(name="TEXTFIELD",length=60) 
	private String textField;	
	
	@Column(name="OWNERDIMKEY",length=32) 
	private String ownerDimKey;	
	public String getDimKey() {
		return dimKey;
	}
	public void setDimKey(String dimKey) {
		this.dimKey = dimKey;
	}
	public String getDimName() {
		return dimName;
	}
	public void setDimName(String dimName) {
		this.dimName = dimName;
	}
	public String getTableName() {
		return tableName;
	}

	public String getOwnerDimKey() {
		return ownerDimKey;
	}
	public void setOwnerDimKey(String ownerDimKey) {
		this.ownerDimKey = ownerDimKey;
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
	public String getValueField() {
		return valueField;
	}
	public void setValueField(String valueField) {
		this.valueField = valueField;
	}
	public String getTextField() {
		return textField;
	}
	public void setTextField(String textField) {
		this.textField = textField;
	}
	
}