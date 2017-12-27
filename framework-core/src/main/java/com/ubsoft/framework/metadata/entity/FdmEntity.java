package com.ubsoft.framework.metadata.entity;

import com.ubsoft.framework.core.dal.annotation.Column;
import com.ubsoft.framework.core.dal.annotation.Table;
import com.ubsoft.framework.core.dal.entity.BaseEntity;

import java.io.Serializable;

@Table(name="META_FDM")
public class FdmEntity extends  BaseEntity implements Serializable {
	private static final long serialVersionUID = 1L;
	@Column
	private String fdmKey;
	@Column
	private String fdmName;
	@Column
	private String fdmXml;
	@Column
	private String remarks;
	

	public String getRemarks() {
		return remarks;
	}

	public void setRemarks(String remarks) {
		this.remarks = remarks;
	}

	public String getFdmKey() {
		return fdmKey;
	}

	public void setFdmKey(String fdmKey) {
		this.fdmKey = fdmKey;
	}

	public String getFdmName(){
		return fdmName;
	}

	public void setFdmName(String fdmName){
		this.fdmName = fdmName;
	}

	public String getFdmXml(){
		return fdmXml;
	}

	public void setFdmXml(String fdmXml){
		this.fdmXml = fdmXml;
	}
}